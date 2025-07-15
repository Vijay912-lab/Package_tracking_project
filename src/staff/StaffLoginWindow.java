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

public class StaffLoginWindow extends JFrame {

    public StaffLoginWindow() {
        setTitle("Staff Login");
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
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (isValidStaff(username, password)) {
                    dispose();
                    new StaffMenu(username,password); 
                } else {
                    JOptionPane.showMessageDialog(StaffLoginWindow.this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        

        add(panel);
        setVisible(true);
    }

    private boolean isValidStaff(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("staff.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[1].trim().equals(username) && parts[2].trim().equals(password)) {
                    return true; 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while checking staff credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false; 
    }
}
