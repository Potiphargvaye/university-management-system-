package view;

import controller.LoginController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private LoginController controller;

    // Brand Color Palette (Using Blue, Green, and Orange-Red)
    private final Color DEEP_BLUE = new Color(24, 43, 73);      // Main branding color
    private final Color ORANGE_RED = new Color(221, 74, 46);    // Action CTA button
    private final Color TEXT_DARK = new Color(51, 51, 51);      // Smooth text color
    private final Color MINT_GREEN = new Color(40, 167, 69);    // Background subtle hint or accent

    public LoginFrame() {
        controller = new LoginController();
        
        // Window Configuration
        setTitle("University Management System - Login");
        setSize(420, 360); // Increased height to beautifully fit headers and captions
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false);

        // Main Layout Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // --- 1. Top Header & Caption Panel ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(DEEP_BLUE);
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("University Control Center");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblCaption = new JLabel("Log in to manage your daily academic activities securely.");
        lblCaption.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblCaption.setForeground(new Color(200, 214, 229));
        lblCaption.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblCaption.setBorder(new EmptyBorder(5, 0, 0, 0));

        headerPanel.add(lblTitle);
        headerPanel.add(lblCaption);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- 2. Input Form Panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(25, 35, 25, 35));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);

        // Username Label
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(TEXT_DARK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
        formPanel.add(lblUser, gbc);

        // Username Field
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setPreferredSize(new Dimension(0, 32));
        gbc.gridy = 1;
        formPanel.add(txtUsername, gbc);

        // Password Label
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(TEXT_DARK);
        gbc.gridy = 2;
        formPanel.add(lblPass, gbc);

        // Password Field
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(0, 32));
        gbc.gridy = 3;
        formPanel.add(txtPassword, gbc);

        // --- 3. Styled Action Button ---
        btnLogin = new JButton("Sign In Securely");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(ORANGE_RED);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(0, 38));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        gbc.gridy = 4;
        gbc.insets = new java.awt.Insets(15, 5, 5, 5); // Add spacing above button
        formPanel.add(btnLogin, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Button Action Handling
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (controller.authenticate(username, password)) {
            JOptionPane.showMessageDialog(this, "Login Successful!", "Welcome back", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); 
            SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}