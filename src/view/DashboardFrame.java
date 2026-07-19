package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private JPanel contentPanel; // Central panel where different modules will load

    // Consistent Brand Color Palette
    private final Color DEEP_BLUE = new Color(24, 43, 73);      // Main headers and menu background
    private final Color ORANGE_RED = new Color(221, 74, 46);    // Accent metric card / highlights
    private final Color MINT_GREEN = new Color(40, 167, 69);    // Success metrics / secondary focus
    private final Color BACKGROUND_LIGHT = new Color(245, 247, 250); // Soft grey application background

    public DashboardFrame() {
        setTitle("University Management System - Dashboard");
        setSize(1000, 650); // Upgraded size for breathing room around analytics cards
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- 1. Visually Friendly & High-Contrast Menu Bar ---
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(DEEP_BLUE);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, ORANGE_RED)); // Vibrant bottom line

        JMenu menuModules = new JMenu("💼 Management Modules");
        menuModules.setForeground(Color.WHITE);
        menuModules.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JMenuItem itemStudents = new JMenuItem("🎓 Students Module");
        JMenuItem itemLecturers = new JMenuItem("👨‍🏫 Lecturers Module");
        JMenuItem itemCourses = new JMenuItem("📚 Courses & Enrollments");
        
        menuModules.add(itemStudents);
        menuModules.add(itemLecturers);
        menuModules.addSeparator();
        menuModules.add(itemCourses);
        
        JMenu menuSystem = new JMenu("⚙️ System");
        menuSystem.setForeground(Color.WHITE);
        menuSystem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JMenuItem itemLogout = new JMenuItem("🚪 Logout");
        menuSystem.add(itemLogout);
        
        menuBar.add(menuModules);
        menuBar.add(menuSystem);
        setJMenuBar(menuBar);

        // --- 2. Central Structural Panel ---
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_LIGHT);
        
        // Load the customized interactive Landing Screen by default
        showLandingPage();
        
        add(contentPanel);

        // --- Action Listeners for Navigation ---
        itemStudents.addActionListener(e -> switchModule(new StudentPanel()));
        itemLecturers.addActionListener(e -> switchModule(new LecturerPanel()));
        itemCourses.addActionListener(e -> switchModule(new CourseEnrollmentPanel()));
        
        itemLogout.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });
    }

    /**
     * Builds and displays the modernized metrics landing dashboard view
     */
    private void showLandingPage() {
        contentPanel.removeAll();

        JPanel landingPanel = new JPanel(new BorderLayout(20, 20));
        landingPanel.setBackground(BACKGROUND_LIGHT);
        landingPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Top Header & Professional Caption Area
        JPanel headerArea = new JPanel();
        headerArea.setLayout(new BoxLayout(headerArea, BoxLayout.Y_AXIS));
        headerArea.setBackground(BACKGROUND_LIGHT);

        JLabel lblGreeting = new JLabel("Welcome Back, Administrator");
        lblGreeting.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblGreeting.setForeground(DEEP_BLUE);
        
        JLabel lblSubCaption = new JLabel("Overview of system performance metrics and centralized operational workflows.");
        lblSubCaption.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubCaption.setForeground(new Color(100, 110, 120));
        lblSubCaption.setBorder(new EmptyBorder(5, 0, 20, 0));

        headerArea.add(lblGreeting);
        headerArea.add(lblSubCaption);
        landingPanel.add(headerArea, BorderLayout.NORTH);

        // Statistics Cards Grid Panel (3 Columns)
        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        statsGrid.setBackground(BACKGROUND_LIGHT);

        // Add metric summary cards using the target palette
        statsGrid.add(createStatCard("Total Registered Students", "5", DEEP_BLUE));
        statsGrid.add(createStatCard("Active Faculty Lecturers", "4", MINT_GREEN));
        statsGrid.add(createStatCard("System Accounts Online", "12", ORANGE_RED));

        landingPanel.add(statsGrid, BorderLayout.CENTER);

        // Bottom Landing Page CTA Caption
        JLabel lblCTA = new JLabel("Use the Management Modules navigation menu above to access, edit, or search current records.", SwingConstants.CENTER);
        lblCTA.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 13));
        lblCTA.setForeground(DEEP_BLUE);
        lblCTA.setBorder(new EmptyBorder(20, 0, 10, 0));
        landingPanel.add(lblCTA, BorderLayout.SOUTH);

        contentPanel.add(landingPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Reusable factory helper method to build beautiful unified stats layout cards
     */
    private JPanel createStatCard(String title, String count, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 235, 240), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Add a colorful top banner line reflecting the identity
        JPanel indicatorLine = new JPanel();
        indicatorLine.setBackground(accentColor);
        indicatorLine.setPreferredSize(new Dimension(0, 4));
        card.add(indicatorLine, BorderLayout.NORTH);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(new Color(120, 130, 140));

        JLabel lblCount = new JLabel(count);
        lblCount.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblCount.setForeground(DEEP_BLUE);
        lblCount.setBorder(new EmptyBorder(10, 0, 0, 0));

        card.add(lblTitle, BorderLayout.CENTER);
        card.add(lblCount, BorderLayout.SOUTH);

        return card;
    }

    private void switchModule(JPanel newModulePanel) {
        contentPanel.removeAll();
        contentPanel.add(newModulePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}