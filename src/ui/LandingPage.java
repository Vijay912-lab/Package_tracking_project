package ui;
import javax.swing.*;

import admin.AdminLoginWindow;
import staff.StaffLoginWindow;
import student.StudentLoginWindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LandingPage extends JFrame {

    public LandingPage() {
        setTitle("Package Tracking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1)); 

        JButton adminButton = new JButton("Admin");
        JButton staffButton = new JButton("Staff");
        JButton studentButton = new JButton("Student");
        JButton exitButton = new JButton("Exit"); 

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminLoginWindow();
                dispose();
            }
        });

        staffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StaffLoginWindow();
                dispose();
            }
        });

        studentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StudentLoginWindow();
                dispose();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); 
            }
        });

        panel.add(adminButton);
        panel.add(staffButton);
        panel.add(studentButton);
        panel.add(exitButton); 

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LandingPage();
            }
        });
    }
}
