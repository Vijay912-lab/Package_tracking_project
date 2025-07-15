package staff;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;

import ui.LandingPage;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.*;
import java.io.*;



class StaffMenu extends JFrame {

    public StaffMenu(String username, String password) {
        setTitle("Staff Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        JButton changeInfoButton = new JButton("Change Your Information");
        JButton changeStudentInfoButton = new JButton("Change Student Information");
        JButton loginPackageButton = new JButton("Log in a Package");
        JButton logoutPackageButton = new JButton("Log out a Package");
        JButton viewPackageDetailsButton = new JButton("View Package Details");
        JButton viewPackageHistoryButton = new JButton("View Package History");
        JButton logoutButton = new JButton("Logout");

        changeInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    new ChangeStaffInfoWindow(username,password);
                    dispose();
                }
        });

        
        changeStudentInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                SearchStudentWindow searchWindow = new SearchStudentWindow();
                
                
                searchWindow.setStudentFoundListener(new SearchStudentWindow.StudentFoundListener() {
                    @Override
                    public void onStudentFound(String username) {
                        
                        searchWindow.dispose();
                        
                        int choice = JOptionPane.showConfirmDialog(null, "Student found. Do you want to update the information?", "Update Confirmation", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            dispose();
                            // Open the UpdateStudentInfoWindow with the found username
                            UpdateStudentInfoWindow updateWindow = new UpdateStudentInfoWindow(username);
                        }
                    }
                });
            }
        });
        
        loginPackageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new LoginPackageWindow();
               dispose();
            }
        });

        logoutPackageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LogoutPackageWindow();
                dispose();
            }
        });

        viewPackageDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewPackageDetailsWindow();
                dispose();
            }
        });

        viewPackageHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new staffViewPackageHistoryWindow();
                dispose();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                JOptionPane.showMessageDialog(null, "Logged out successfully");
                new LandingPage();
            }
        });

        panel.add(changeInfoButton);
        panel.add(changeStudentInfoButton);
        panel.add(loginPackageButton);
        panel.add(logoutPackageButton);
        panel.add(viewPackageDetailsButton);
        panel.add(viewPackageHistoryButton);
        panel.add(logoutButton);

        add(panel);
        setVisible(true);
    }
}class ChangeStaffInfoWindow extends JFrame {

    private String currentUsername;

    public ChangeStaffInfoWindow(String username,String password) {
        this.currentUsername = username;

        setTitle("Change Staff Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JButton changeUsernameButton = new JButton("Change Username");
        JButton changePasswordButton = new JButton("Change Password");
        JButton backButton = new JButton("Back");

        panel.add(changeUsernameButton);
        panel.add(changePasswordButton);
        panel.add(backButton);

        changeUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUsername = JOptionPane.showInputDialog(ChangeStaffInfoWindow.this, "Enter New Username:");
                if (newUsername != null && !newUsername.isEmpty()) {
                    changeUsername(newUsername);
                }
            }
        });

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentPassword = JOptionPane.showInputDialog(ChangeStaffInfoWindow.this, "Enter Current Password:");
                if (currentPassword != null && !currentPassword.isEmpty()) {
                    String newPassword = JOptionPane.showInputDialog(ChangeStaffInfoWindow.this, "Enter New Password:");
                    if (newPassword != null && !newPassword.isEmpty()) {
                        changePassword(currentPassword, newPassword);
                    }
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StaffMenu(username,password); 
            }
        });

        add(panel);
        setVisible(true);
    }

    private void changeUsername(String newUsername) {
        try {
            File file = new File("staff.csv");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder inputBuffer = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].trim().equals(currentUsername)) {
                    parts[1] = newUsername; // Change username
                    line = String.join(",", parts);
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            reader.close();

            
            FileOutputStream fileOut = new FileOutputStream(file);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();

            JOptionPane.showMessageDialog(ChangeStaffInfoWindow.this, "Username changed successfully. Please login again.");
            dispose();
            new LandingPage(); // Logout
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ChangeStaffInfoWindow.this, "Error occurred while changing username", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changePassword(String currentPassword, String newPassword) {
        try {
            File file = new File("staff.csv");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder inputBuffer = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].trim().equals(currentUsername)) {
                    
                    if (parts.length >= 3 && parts[2].trim().equals(currentPassword)) {
                        parts[2] = newPassword; // Change password
                        line = String.join(",", parts);
                    } else {
                        JOptionPane.showMessageDialog(ChangeStaffInfoWindow.this, "Incorrect current password", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            reader.close();

            
            FileOutputStream fileOut = new FileOutputStream(file);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();

            JOptionPane.showMessageDialog(ChangeStaffInfoWindow.this, "Password changed successfully. Please login again.");
            dispose();
            new LandingPage(); // Logout
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ChangeStaffInfoWindow.this, "Error occurred while changing password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class SearchStudentWindow extends JFrame {

   
    public interface StudentFoundListener {
        void onStudentFound(String username);
    }

   
    private StudentFoundListener studentFoundListener;

    
    public void setStudentFoundListener(StudentFoundListener listener) {
        this.studentFoundListener = listener;
    }

    public SearchStudentWindow() {
        setTitle("Search Student");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JLabel titleLabel = new JLabel("Enter Student's Username:");
        JTextField usernameField = new JTextField();
        JButton searchButton = new JButton("Search");

        panel.add(titleLabel);
        panel.add(usernameField);
        panel.add(searchButton);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchUsername = usernameField.getText().trim();
                String studentInfo = searchStudent(searchUsername);
                if (studentInfo != null) {
                    int choice = JOptionPane.showConfirmDialog(SearchStudentWindow.this, "Student found. Do you want to update the information?", "Update Confirmation", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        if (studentFoundListener != null) {
                            studentFoundListener.onStudentFound(searchUsername);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(SearchStudentWindow.this, "Student not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(panel);
        setVisible(true);
    }

    private String searchStudent(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("students.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[1].trim().equals(username)) {
                    return "Name: " + parts[0] + "\nUsername: " + parts[1] + "\nPassword: " + parts[2] +
                            "\nPhone Number: " + parts[3] + "\nGmail ID: " + parts[4] + "\nRoom Number: " + parts[5];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while searching for student", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}

class UpdateStudentInfoWindow extends JFrame {

    private String currentUsername;

    public UpdateStudentInfoWindow(String username) {
        this.currentUsername = username;

        setTitle("Update Student Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        JLabel updateLabel = new JLabel("Select Information to Update:");
        JComboBox<String> updateOptions = new JComboBox<>(new String[]{"Name", "Username", "Password", "Gmail ID", "Phone Number", "Room Number"});
        JTextField newValueField = new JTextField();
        JButton updateButton = new JButton("Update");
        JButton backButton = new JButton("Back");
        panel.add(updateLabel);
        panel.add(updateOptions);
        panel.add(newValueField);
        panel.add(updateButton);
        panel.add(backButton);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String updateField = (String) updateOptions.getSelectedItem();
                String newValue = newValueField.getText().trim();
                if (!newValue.isEmpty()) {
                    boolean success = updateStudentInfo(updateField, newValue);
                    if (success) {
                        JOptionPane.showMessageDialog(UpdateStudentInfoWindow.this, "Information updated successfully.");
                    } else {
                        JOptionPane.showMessageDialog(UpdateStudentInfoWindow.this, "Error updating information.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(UpdateStudentInfoWindow.this, "Please enter a new value.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StaffMenu(username, "password");
            }
        });

        add(panel);
        setVisible(true);
    }

    private boolean updateStudentInfo(String field, String newValue) {
        try {
            File file = new File("students.csv");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder inputBuffer = new StringBuilder();
    
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[1].trim().equals(currentUsername)) {
                    switch (field) {
                        case "Name":
                            parts[0] = newValue;
                            break;
                        case "Username":
                            parts[1] = newValue;
                            break;
                        case "Password":
                            parts[2] = newValue;
                            break;
                        case "Gmail ID":
                            parts[3] = newValue;
                            break;
                        case "Phone Number":
                            parts[4] = newValue;
                            break;
                        case "Room Number":
                            parts[5] = newValue;
                            break;
                        default:
                            break;
                    }
                    line = String.join(",", parts);
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            reader.close();
    
            FileOutputStream fileOut = new FileOutputStream(file);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
    
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    } 
}

class LoginPackageWindow extends JFrame {
    private String selectedStudentName;
    private String phoneNumber;
    private String email;
    private String roomNumber;
    private String packageType;

    public LoginPackageWindow() {
        setTitle("Login Package");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 350);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        JTextField studentNameField = new JTextField();
        JButton searchButton = new JButton("Search");
        JComboBox<String> bestFitNamesComboBox = new JComboBox<>();
        JComboBox<String> packageTypeComboBox = new JComboBox<>(new String[]{"Bag", "Box", "Paper Mail","Other"});
        JButton loginButton = new JButton("Login Package");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Enter Student's Name:"));
        panel.add(studentNameField);
        panel.add(searchButton);
        panel.add(bestFitNamesComboBox);
        panel.add(packageTypeComboBox);
        panel.add(loginButton);
        panel.add(backButton);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchName = studentNameField.getText().trim();
                populateBestFitNamesComboBox(searchName, bestFitNamesComboBox);
            }
        });

        bestFitNamesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedStudentName = (String) bestFitNamesComboBox.getSelectedItem();
                loadStudentInfo(selectedStudentName);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StaffMenu("username", "password");
            }
        });

        add(panel);
        setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedStudentName != null) {
                    
                    if (validateStudentInfo()) {
                       
                        packageType = (String) packageTypeComboBox.getSelectedItem();
                        
                       
                        String tagNumber = savePackageInfo(selectedStudentName, phoneNumber, email, roomNumber, packageType);

                        
                        String oneTimePassword = generateOneTimePassword();

                        
                        saveNotification(selectedStudentName, oneTimePassword, tagNumber);

                        
                        JOptionPane.showMessageDialog(LoginPackageWindow.this, "Login package created successfully.\nTag Number: " + tagNumber);

                        
                        dispose();
                        new StaffMenu(oneTimePassword, oneTimePassword);

                    } else {
                        JOptionPane.showMessageDialog(LoginPackageWindow.this, "Entered information does not match student information.");
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginPackageWindow.this, "Please select a student.");
                }
            }
        });

        add(panel);
        setVisible(true);
    }



    private void populateBestFitNamesComboBox(String searchName, JComboBox<String> comboBox) {
        
        ArrayList<String> bestFitNames = searchBestFitNames(searchName);

       
        comboBox.removeAllItems();
        for (String name : bestFitNames) {
            comboBox.addItem(name);
        }
    }

    private ArrayList<String> searchBestFitNames(String searchName) {
        
        ArrayList<String> bestFitNames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("students.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String studentName = parts[0].trim();
                if (studentName.toLowerCase().contains(searchName.toLowerCase())) {
                    bestFitNames.add(studentName);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bestFitNames;
    }

    private void loadStudentInfo(String studentName) {
        
        try (BufferedReader reader = new BufferedReader(new FileReader("students.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0].trim();
                if (name.equals(studentName)) {
                    
                    phoneNumber = parts[4].trim();
                    email = parts[3].trim();
                    roomNumber = parts[5].trim();
                    return;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateStudentInfo() {
    
        if (phoneNumber != null && email != null && roomNumber != null) {
            return phoneNumber.equals(JOptionPane.showInputDialog(LoginPackageWindow.this, "Enter Phone Number:", phoneNumber)) &&
                    email.equals(JOptionPane.showInputDialog(LoginPackageWindow.this, "Enter Email:", email)) &&
                    roomNumber.equals(JOptionPane.showInputDialog(LoginPackageWindow.this, "Enter Room Number:", roomNumber));
        }
        return false;
    }

    private String savePackageInfo(String studentName, String phoneNumber, String email, String roomNumber, String packageType) {
       
        String tagNumber = generateTagNumber();
        String status = "notpicked";
        try (PrintWriter writer = new PrintWriter(new FileWriter("packages.csv", true))) {
            writer.println(studentName + "," + phoneNumber + "," + email + "," + roomNumber + "," + packageType + "," + tagNumber + "," + status);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return tagNumber;
    }
    

    private String generateTagNumber() {

        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }

    private String generateOneTimePassword() {
       
        return UUID.randomUUID().toString().substring(0, 3);
    }

    private void saveNotification(String username, String oneTimePassword, String tagNumber) {
       
        try (PrintWriter writer = new PrintWriter(new FileWriter("notifications.csv", true))) {
            writer.println(username + "," + oneTimePassword + "," + tagNumber);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
class LogoutPackageWindow extends JFrame {

    public LogoutPackageWindow() {
        setTitle("Logout Package");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextField tagNumberField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton logoutButton = new JButton("Logout");
        JButton backButton = new JButton("Back");

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputPanel.add(new JLabel("Enter Tag Number:"));
        inputPanel.add(tagNumberField);
        inputPanel.add(searchButton);
        inputPanel.add(logoutButton);
        inputPanel.add(backButton);

        String[] columns = {"Name", "Gmail", "Phone Number", "Tag Number", "Room Number", "Package Type"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tagNumber = tagNumberField.getText().trim();
                searchPackageByTagNumber(tagNumber, tableModel);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tagNumber = tagNumberField.getText().trim();
                String oneTimePassword = JOptionPane.showInputDialog(LogoutPackageWindow.this, "Enter One Time Password:");
                if (validateOneTimePassword(tagNumber, oneTimePassword)) {
                    updatePackageStatus(tagNumber);
                    dispose();
                    JOptionPane.showMessageDialog(LogoutPackageWindow.this, "The Package has been Logout Successfully");
                    new StaffMenu("username", "password");
                } else {
                    JOptionPane.showMessageDialog(LogoutPackageWindow.this, "Invalid One Time Password");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StaffMenu("username", "password");
            }
        });

        add(panel);
        setVisible(true);
    }

    private void searchPackageByTagNumber(String tagNumber, DefaultTableModel tableModel) {
        // Clear the table
        tableModel.setRowCount(0);

        try (BufferedReader reader = new BufferedReader(new FileReader("packages.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[5].trim().equals(tagNumber)) {
                    String name = parts[0].trim();
                    String gmail = parts[2].trim();
                    String phoneNumber = parts[1].trim();
                    String roomNumber = parts[3].trim();
                    String packageType = parts[4].trim();
                    tableModel.addRow(new Object[]{name, gmail, phoneNumber, tagNumber, roomNumber, packageType});
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateOneTimePassword(String tagNumber, String oneTimePassword) {
        try (BufferedReader reader = new BufferedReader(new FileReader("notifications.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[2].trim().equals(tagNumber) && parts[1].trim().equals(oneTimePassword)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void updatePackageStatus(String tagNumber) {
       
        try (BufferedReader reader = new BufferedReader(new FileReader("packages.csv"));
             BufferedWriter historyWriter = new BufferedWriter(new FileWriter("packagehistory.csv", true));
             PrintWriter packagesWriter = new PrintWriter(new FileWriter("packages_temp.csv"))) {
    
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[5].trim().equals(tagNumber)) {
                    parts[6] = "picked up"; 
                    
                    historyWriter.write(String.join(",", parts) + "\n");
                } else {
                    
                    packagesWriter.println(line);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    
        
        File packagesFile = new File("packages.csv");
        File tempFile = new File("packages_temp.csv");
        if (tempFile.renameTo(packagesFile)) {
            System.out.println("Package status updated and moved to history successfully.");
        } else {
            System.out.println("Failed to update package status and move to history.");
        }
    }
}

class ViewPackageDetailsWindow extends JFrame {

    public ViewPackageDetailsWindow() {
        setTitle("View Package Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");


        JTextArea packageDetailsTextArea = new JTextArea(20, 60);
        packageDetailsTextArea.setEditable(false);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(new JLabel("Search by Student Name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(backButton);
        String[] columns = {"Student Name", "Room Number", "Tag Number", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText().trim();
                searchPackagesByStudentName(searchText, tableModel);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StaffMenu("username","password");
            }
        });

        loadPackageDetails(tableModel);

        add(panel);
        setVisible(true);
    }

    private void loadPackageDetails(DefaultTableModel tableModel) {
        try (BufferedReader reader = new BufferedReader(new FileReader("packages.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) { // Ensure at least 6 parts exist
                    String studentName = parts[0].trim();
                    String roomNumber = parts[3].trim();
                    String tagNumber = parts[5].trim();
                    String status = parts[6].trim();
                    tableModel.addRow(new Object[]{studentName, roomNumber, tagNumber, status});
                } else {
                    System.err.println("Invalid line format in packages.csv: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private void searchPackagesByStudentName(String studentName, DefaultTableModel tableModel) {
       
        tableModel.setRowCount(0);
        
        
        try (BufferedReader reader = new BufferedReader(new FileReader("packages.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String currentStudentName = parts[0].trim();
                String status = parts[6].trim();
                if (currentStudentName.equalsIgnoreCase(studentName) && status.equalsIgnoreCase("notpicked")) { // Add this condition
                    String roomNumber = parts[3].trim();
                    String tagNumber = parts[4].trim();
                    tableModel.addRow(new Object[]{currentStudentName, roomNumber, tagNumber, status});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}
class staffViewPackageHistoryWindow extends JFrame {

    public staffViewPackageHistoryWindow() {
        setTitle("View Package History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(new JLabel("Search by Student Name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(backButton);

        String[] columns = {"Name", "Room Number", "Package Type", "Tag Number", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchName = searchField.getText().trim();
                searchPackages(searchName, tableModel);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StaffMenu("username", "password");
            }
        });

        loadPackageHistory(tableModel);
        loadPackages(tableModel);

        add(panel);
        setVisible(true);
    }

    private void loadPackageHistory(DefaultTableModel tableModel) {
        
        try (BufferedReader reader = new BufferedReader(new FileReader("packagehistory.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0].trim();
                String roomNumber = parts[3].trim();
                String packageType = parts[4].trim();
                String tagNumber = parts[5].trim();
                String status = parts[6].trim();
                tableModel.addRow(new Object[]{name, roomNumber, packageType, tagNumber, status});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPackages(DefaultTableModel tableModel) {
        
        try (BufferedReader reader = new BufferedReader(new FileReader("packages.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0].trim();
                String roomNumber = parts[3].trim();
                String packageType = parts[4].trim();
                String tagNumber = parts[5].trim();
                String status = parts[6].trim();
                tableModel.addRow(new Object[]{name, roomNumber, packageType, tagNumber, status});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void searchPackages(String searchName, DefaultTableModel tableModel) {
        
        tableModel.setRowCount(0);
        searchPackagesInFile(searchName, "packages.csv", tableModel);
        searchPackagesInFile(searchName, "packagehistory.csv", tableModel);
    }

    private void searchPackagesInFile(String searchName, String filename, DefaultTableModel tableModel) {
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0].trim();
                if (name.equalsIgnoreCase(searchName)) {
                    String roomNumber = parts[3].trim();
                    String packageType = parts[4].trim();
                    String tagNumber = parts[5].trim();
                    String status = parts[6].trim();
                    tableModel.addRow(new Object[]{name, roomNumber, packageType, tagNumber, status});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


