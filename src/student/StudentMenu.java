package student;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;

import ui.LandingPage;

class StudentMenu extends JFrame {

    private String currentUsername; 

    public StudentMenu(String username) 
    {
        this.currentUsername = username; 
        setTitle("Student Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JButton viewNotificationsButton = new JButton("View Notifications");
        JButton viewPackagesButton = new JButton("View Your Packages");
        JButton logoutButton = new JButton("Logout");
        
    
        viewNotificationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewNotificationWindow(currentUsername);
                dispose();

            }
        });

        viewPackagesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewPackagesWindow(currentUsername);
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

        panel.add(viewNotificationsButton);
        panel.add(viewPackagesButton);
        panel.add(logoutButton);

        add(panel);
        setVisible(true);
    }
}

class ViewNotificationWindow extends JFrame {

    private String currentUsername;

    public ViewNotificationWindow(String username) {
        this.currentUsername = username;
        setTitle("View Notifications");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        String[] columns = {"Name", "One-Time Password", "Tag Number", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                new StudentMenu(currentUsername); 
            }
        });
        panel.add(backButton, BorderLayout.SOUTH);

        loadNotifications(tableModel);

        add(panel);
        setVisible(true);
    }

    private void loadNotifications(DefaultTableModel tableModel) {
        
        String name = getNameForUsername(currentUsername);
        if (name != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader("notifications.csv"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String username = parts[0].trim();
                    if (username.equalsIgnoreCase(name)) {
                        String oneTimePassword = parts[1].trim();
                        String tagNumber = parts[2].trim();
                        String status = getStatusForTagNumber(tagNumber); 
                        tableModel.addRow(new Object[]{name, oneTimePassword, tagNumber, status});
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getNameForUsername(String username) {
        
        try (BufferedReader reader = new BufferedReader(new FileReader("students.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String studentUsername = parts[1].trim(); 
                if (studentUsername.equalsIgnoreCase(username)) {
                    //System.out.println("Name found: " + parts[0].trim()); 
                    return parts[0].trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getStatusForTagNumber(String tagNumber) {
        
        try (BufferedReader reader = new BufferedReader(new FileReader("packages.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String packageTagNumber = parts[5].trim();
                if (packageTagNumber.equalsIgnoreCase(tagNumber)) {
                    return "Not Picked"; 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Picked Up"; 
    }
}
class ViewPackagesWindow extends JFrame {

    private String currentUsername;

    public ViewPackagesWindow(String username) {
        this.currentUsername = username;
        setTitle("View Your Packages");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        DefaultTableModel unpickedTableModel = new DefaultTableModel(new String[]{"Tag Number", "Package Type", "Status"}, 0);
        JTable unpickedTable = new JTable(unpickedTableModel);
        JScrollPane unpickedScrollPane = new JScrollPane(unpickedTable);

        DefaultTableModel pickedTableModel = new DefaultTableModel(new String[]{"Tag Number", "Package Type", "Status"}, 0);
        JTable pickedTable = new JTable(pickedTableModel);
        JScrollPane pickedScrollPane = new JScrollPane(pickedTable);

        JPanel unpickedPanel = new JPanel();
        unpickedPanel.setLayout(new BorderLayout());
        unpickedPanel.add(new JLabel("Unpicked Packages"), BorderLayout.NORTH);
        unpickedPanel.add(unpickedScrollPane, BorderLayout.CENTER);

        JPanel pickedPanel = new JPanel();
        pickedPanel.setLayout(new BorderLayout());
        pickedPanel.add(new JLabel("Picked Packages"), BorderLayout.NORTH);
        pickedPanel.add(pickedScrollPane, BorderLayout.CENTER);

        JPanel packagesPanel = new JPanel(new GridLayout(2, 1));
        packagesPanel.add(unpickedPanel);
        packagesPanel.add(pickedPanel);

        JButton backButton = new JButton("Back to Student Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StudentMenu(currentUsername);
            }
        });

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(backButton);

        panel.add(packagesPanel, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        loadPackages(unpickedTableModel, pickedTableModel);

        add(panel);
        setVisible(true);
    }

    private void loadPackages(DefaultTableModel unpickedTableModel, DefaultTableModel pickedTableModel) {
        String name = getNameForUsername(currentUsername);
        System.out.println("Current username: " + currentUsername);
        System.out.println("Corresponding name: " + name);
        if (name != null) {
            loadUnpickedPackages(unpickedTableModel, name);
            loadPickedPackages(pickedTableModel, name);
        }
    }

    private void loadUnpickedPackages(DefaultTableModel tableModel, String name) {
        try (BufferedReader reader = new BufferedReader(new FileReader("packages.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                System.out.println("Package line: " + Arrays.toString(parts));
                if (parts.length >= 7) {
                    String csvName = parts[0].trim();
                    String status = parts[6].trim();
                    if (csvName.equalsIgnoreCase(name) && status.equalsIgnoreCase("notpicked")) {
                        String tagNumber = parts[5].trim();
                        String packageType = parts[4].trim();
                        tableModel.addRow(new Object[]{tagNumber, packageType, "Unpicked"});
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPickedPackages(DefaultTableModel tableModel, String name) {
        try (BufferedReader reader = new BufferedReader(new FileReader("packagehistory.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                System.out.println("Package history line: " + Arrays.toString(parts));
                if (parts.length >= 7) {
                    String csvName = parts[0].trim();
                    String status = parts[6].trim();
                    if (csvName.equalsIgnoreCase(name) && status.equalsIgnoreCase("picked up")) {
                        String tagNumber = parts[5].trim();
                        String packageType = parts[4].trim();
                        tableModel.addRow(new Object[]{tagNumber, packageType, "Picked"});
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNameForUsername(String username) {
        // Get the name corresponding to the given username from students.csv
        try (BufferedReader reader = new BufferedReader(new FileReader("students.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].trim().equalsIgnoreCase(username)) {
                    return parts[0].trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        
    }
}



