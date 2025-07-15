package admin;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

import ui.LandingPage;

class AdminMenu extends JFrame {

    public AdminMenu() {
        setTitle("Admin Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        JButton addStaffButton = new JButton("Add a Staff");
        JButton removeStaffButton = new JButton("Remove a Staff");
        JButton addStudentButton = new JButton("Add a Student");
        JButton removeStudentButton = new JButton("Remove a Student");
        JButton viewPackageHistoryButton = new JButton("View Package History");
        JButton logoutButton = new JButton("Logout");

        addStaffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddStaffWindow();
                dispose(); // Close the Admin Menu
            }
        });

        removeStaffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RemoveStaffWindow();
                dispose(); // Close the Admin Menu
            }
        });

        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddStudentWindow();
                dispose();
                
            }
        });

        removeStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RemoveStudentWindow();
                dispose();
                
            }
        });

        viewPackageHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               new adminViewPackageHistoryWindow();
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

        panel.add(addStaffButton);
        panel.add(removeStaffButton);
        panel.add(addStudentButton);
        panel.add(removeStudentButton);
        panel.add(viewPackageHistoryButton);
        panel.add(logoutButton);

        add(panel);
        setVisible(true);
    }
}

class LoginWindow extends JFrame {

    public LoginWindow(String userType) {
        setTitle(userType);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LandingPage();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                JOptionPane.showMessageDialog(null, "Login Successful as " + userType);
            }
        });

        add(panel);
        setVisible(true);
    }
}
class AddStaffWindow extends JFrame {
    
    public AddStaffWindow() {
        setTitle("Add Staff");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JTextField nameField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton addButton = new JButton("Add");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(addButton);
        panel.add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminMenu();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                if (isUsernameExists(username)) {
                    JOptionPane.showMessageDialog(AddStaffWindow.this, "User with this username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Add staff to CSV file
                    addStaffToCSV(name, username, password);
                    nameField.setText("");
                    usernameField.setText("");
                    passwordField.setText("");
                    JOptionPane.showMessageDialog(AddStaffWindow.this, "Staff added successfully");
                }
            }
        });

        add(panel);
        setVisible(true);
    }

    private boolean isUsernameExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("staff.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].trim().equals(username)) {
                    return true; // Username already exists
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while checking username", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false; // Username doesn't exist
    }

    private void addStaffToCSV(String name, String username, String password) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("staff.csv", true))) {
            writer.println(name + "," + username + "," + password);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while adding staff", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class AddStudentWindow extends JFrame {

    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField gmailField;
    private JTextField phoneField;
    private JTextField roomField;

    public AddStudentWindow() {
        setTitle("Add Student");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1));

        nameField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        gmailField = new JTextField();
        phoneField = new JTextField();
        roomField = new JTextField();
        JButton addButton = new JButton("Add");
        JButton backButton = new JButton("Back");

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Gmail ID:"));
        panel.add(gmailField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneField);
        panel.add(new JLabel("Room Number:"));
        panel.add(roomField);
        panel.add(addButton);
        panel.add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminMenu();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String gmail = gmailField.getText();
                String phone = phoneField.getText();
                String room = roomField.getText();
        
                if (!isUsernameUnique(username) || !isPhoneUnique(phone) || !isGmailUnique(gmail)) {
                    String errorMessage = "";
                    if (!isUsernameUnique(username)) {
                        errorMessage += "Username already exists.\n";
                    }
                    if (!isPhoneUnique(phone)) {
                        errorMessage += "Phone number already exists.\n";
                    }
                    if (!isGmailUnique(gmail)) {
                        errorMessage += "Gmail ID already exists.\n";
                    }
                    JOptionPane.showMessageDialog(AddStudentWindow.this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    addStudentToCSV(name, username, password, gmail, phone, room);
                    clearFields();
                    JOptionPane.showMessageDialog(AddStudentWindow.this, "Student added successfully");
                }
            }
        });
        
        add(panel);
        setVisible(true);
    }
        

    private void addStudentToCSV(String name, String username, String password, String gmail, String phone, String room) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("students.csv", true))) {
            writer.println(name + "," + username + "," + password + "," + gmail + "," + phone + "," + room);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while adding student", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        gmailField.setText("");
        phoneField.setText("");
        roomField.setText("");
    }
    private boolean isUsernameUnique(String username) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("students.csv"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].trim().equals(username)) {
                    reader.close();
                    return false;
                }
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    private boolean isPhoneUnique(String phone) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("students.csv"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[4].trim().equals(phone)) {
                    reader.close();
                    return false;
                }
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    private boolean isGmailUnique(String gmail) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("students.csv"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[3].trim().equals(gmail)) {
                    reader.close();
                    return false;
                }
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } 
        return true;
    }
}

class RemoveStaffWindow extends JFrame {

public RemoveStaffWindow() {
    setTitle("Remove Staff");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(400, 400); 

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    JTextArea staffListArea = new JTextArea();
    staffListArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(staffListArea);

    
    staffListArea.append("Name\t\t\tUsername\n");

    try {
        BufferedReader reader = new BufferedReader(new FileReader("staff.csv"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 2) {
                staffListArea.append(parts[0] + "\t\t\t" + parts[1] + "\n");
            }
        }
        reader.close();
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error occurred while reading staff data", "Error", JOptionPane.ERROR_MESSAGE);
    }

    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BorderLayout());

    JTextArea usernameField = new JTextArea(); 
    JButton removeButton = new JButton("Remove");
    JButton backButton = new JButton("Back");

    inputPanel.add(new JLabel("Enter Username to Remove:"), BorderLayout.NORTH);
    inputPanel.add(new JScrollPane(usernameField), BorderLayout.CENTER);
    inputPanel.add(removeButton, BorderLayout.EAST);
    inputPanel.add(backButton, BorderLayout.WEST);

    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, inputPanel);
    splitPane.setResizeWeight(0.5); 

    panel.add(splitPane, BorderLayout.CENTER);

    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            new AdminMenu();
        }
    });

    removeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String usernameToRemove = usernameField.getText().trim(); 
            if (removeStaffFromCSV(usernameToRemove)) {
                JOptionPane.showMessageDialog(RemoveStaffWindow.this, "Staff removed successfully");
                dispose();
                new RemoveStaffWindow(); 
            } else {
                JOptionPane.showMessageDialog(RemoveStaffWindow.this, "Staff with the given username not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    add(panel);
    setVisible(true);
}

private boolean removeStaffFromCSV(String usernameToRemove) {
    try {
        ArrayList<String> lines = new ArrayList<>();
        boolean removed = false;
        BufferedReader reader = new BufferedReader(new FileReader("staff.csv"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 2 && parts[1].trim().equals(usernameToRemove)) {
                removed = true;
            } else {
                lines.add(line);
            }
        }
        reader.close();

        if (removed) {
            PrintWriter writer = new PrintWriter(new FileWriter("staff.csv"));
            for (String newLine : lines) {
                writer.println(newLine);
            }
            writer.close();
        }
        return removed;
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error occurred while removing staff", "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
}

class RemoveStudentWindow extends JFrame {

public RemoveStudentWindow() {
    setTitle("Remove Student");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(400, 400); 

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    JTextArea studentListArea = new JTextArea();
    studentListArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(studentListArea);

    
    studentListArea.append("Name\t\t\tUsername\n");

    try {
        BufferedReader reader = new BufferedReader(new FileReader("students.csv"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 2) {
                studentListArea.append(parts[0] + "\t\t\t" + parts[1] + "\n");
            }
        }
        reader.close();
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error occurred while reading student data", "Error", JOptionPane.ERROR_MESSAGE);
    }

    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BorderLayout());

    JTextArea usernameField = new JTextArea(); 
    JButton removeButton = new JButton("Remove");
    JButton backButton = new JButton("Back");

    inputPanel.add(new JLabel("Enter Username to Remove:"), BorderLayout.NORTH);
    inputPanel.add(new JScrollPane(usernameField), BorderLayout.CENTER); 
    inputPanel.add(removeButton, BorderLayout.EAST);
    inputPanel.add(backButton, BorderLayout.WEST);

    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, inputPanel);
    splitPane.setResizeWeight(0.5); 

    panel.add(splitPane, BorderLayout.CENTER);

    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            new AdminMenu();
        }
    });

    removeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String usernameToRemove = usernameField.getText().trim(); 
            if (removeStudentFromCSV(usernameToRemove)) {
                JOptionPane.showMessageDialog(RemoveStudentWindow.this, "Student removed successfully");
                dispose();
                new RemoveStudentWindow(); 
            } else {
                JOptionPane.showMessageDialog(RemoveStudentWindow.this, "Student with the given username not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    add(panel);
    setVisible(true);
}

private boolean removeStudentFromCSV(String usernameToRemove) {
    try {
        ArrayList<String> lines = new ArrayList<>();
        boolean removed = false;
        BufferedReader reader = new BufferedReader(new FileReader("students.csv"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 2 && parts[1].trim().equals(usernameToRemove)) {
                removed = true;
            } else {
                lines.add(line);
            }
        }
        reader.close();

        if (removed) {
            PrintWriter writer = new PrintWriter(new FileWriter("students.csv"));
            for (String newLine : lines) {
                writer.println(newLine);
            }
            writer.close();
        }
        return removed;
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error occurred while removing student", "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
}

class adminViewPackageHistoryWindow extends JFrame {

public adminViewPackageHistoryWindow() {
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
            new AdminMenu();
        }
    });

    loadPackageHistory(tableModel);
    loadPackages(tableModel);

    add(panel);
    setVisible(true);
}

private void loadPackageHistory(DefaultTableModel tableModel) {
    // Load package history from packagehistory.csv
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
 
