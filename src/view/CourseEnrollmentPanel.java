package view;

import controller.CourseController;
import database.DatabaseConnection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class CourseEnrollmentPanel extends JPanel {
    private CourseController controller;
    private JTabbedPane tabbedPane;

    // Dropdown References to allow dynamic updating
    private JComboBox<String> comboDept;
    private JComboBox<String> comboStudents;
    private JComboBox<String> comboCourses;
    
    private Map<Integer, String> deptsMap;
    private Map<Integer, String> studentsMap;
    private Map<Integer, String> coursesMap;

    // Palette Consistency
    private final Color DEEP_BLUE = new Color(24, 43, 73);
    private final Color MINT_GREEN = new Color(40, 167, 69);
    private final Color ORANGE_RED = new Color(221, 74, 46);
    private final Color BACKGROUND_LIGHT = new Color(245, 247, 250);

    public CourseEnrollmentPanel() {
        controller = new CourseController();
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_LIGHT);

        // Pre-initialize dropdown fields before building UI views to ensure no NullPointerExceptions
        comboDept = new JComboBox<>();
        comboStudents = new JComboBox<>();
        comboCourses = new JComboBox<>();

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Initialize tabs
        tabbedPane.addTab("🏢 Manage Departments", createDepartmentTab());
        tabbedPane.addTab("📚 Course Catalog", createCourseTab());
        tabbedPane.addTab("📝 Enrolment Manager", createEnrollmentTab());

        // Listen to tab changes and reload live database data into the dropdown pickers automatically!
        tabbedPane.addChangeListener(e -> refreshAllDropdowns());

        add(tabbedPane, BorderLayout.CENTER);
        
        // Initial load for setup
        refreshAllDropdowns();
    }

    /**
     * Method to re-query the database tables and refresh dropdown item listings live
     */
    private void refreshAllDropdowns() {
        // 1. Refresh Department ComboBox
        if (comboDept != null) {
            comboDept.removeAllItems();
            deptsMap = DatabaseConnection.getDropdownData("Departments", "department_id", "department_name");
            if (deptsMap != null) {
                for (String deptName : deptsMap.values()) {
                    comboDept.addItem(deptName);
                }
            }
        }

        // 2. Refresh Student Enrollment ComboBox
        if (comboStudents != null) {
            comboStudents.removeAllItems();
            studentsMap = DatabaseConnection.getDropdownData("Students", "student_id", "full_name");
            if (studentsMap != null) {
                for (String studentName : studentsMap.values()) {
                    comboStudents.addItem(studentName);
                }
            }
        }

        // 3. Refresh Course Enrollment ComboBox
        if (comboCourses != null) {
            comboCourses.removeAllItems();
            // FIXED: Changed "course_title" to "course_name" to match your exact DB script schema layout!
            coursesMap = DatabaseConnection.getDropdownData("Courses", "course_id", "course_name");
            if (coursesMap != null) {
                for (String courseName : coursesMap.values()) {
                    comboCourses.addItem(courseName);
                }
            }
        }
    }

    // ==========================================
    // TAB 1: DEPARTMENTS INTERFACE
    // ==========================================
    private JPanel createDepartmentTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(BACKGROUND_LIGHT);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(300, 0));
        formPanel.setBorder(createCustomTitleBorder("New Department"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 4, 8, 4);
        gbc.weightx = 1.0; gbc.gridx = 0;

        JLabel lblName = new JLabel("Department Name:");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JTextField txtDeptName = createStyledTextField();

        gbc.gridy = 0; formPanel.add(lblName, gbc);
        gbc.gridy = 1; formPanel.add(txtDeptName, gbc);

        JButton btnAddDept = new JButton("Save Department");
        stylePrimaryButton(btnAddDept, MINT_GREEN);
        gbc.gridy = 2; gbc.insets = new Insets(20, 4, 4, 4);
        formPanel.add(btnAddDept, gbc);
        panel.add(formPanel, BorderLayout.WEST);

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Department Name", "Lecturers Assigned", "Action"}, 0){
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = createStyledTable(model);
        table.getColumnModel().getColumn(3).setCellRenderer(new ActionButtonRenderer(ORANGE_RED));

        Runnable refreshDepts = () -> {
            model.setRowCount(0);
            var data = controller.getAllDepartments();
            if (data != null) {
                for (Object[] row : data) {
                    model.addRow(new Object[]{row[0], row[1], row[2], "Delete"});
                }
            }
        };

        btnAddDept.addActionListener(e -> {
            if (!txtDeptName.getText().trim().isEmpty() && controller.addDepartment(txtDeptName.getText().trim())) {
                JOptionPane.showMessageDialog(panel, "Department saved!");
                txtDeptName.setText("");
                refreshDepts.run();
                refreshAllDropdowns(); // Instantly map to other tabs
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1 && table.getSelectedColumn() == 3) {
                    int id = (int) table.getValueAt(row, 0);
                    if (JOptionPane.showConfirmDialog(panel, "Drop Department ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        controller.deleteDepartment(id);
                        refreshDepts.run();
                        refreshAllDropdowns();
                    }
                }
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        refreshDepts.run();
        return panel;
    }

    // ==========================================
    // TAB 2: COURSES CATALOG INTERFACE
    // ==========================================
    private JPanel createCourseTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(BACKGROUND_LIGHT);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(320, 0));
        formPanel.setBorder(createCustomTitleBorder("Add New Course Structure"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4); gbc.weightx = 1.0; gbc.gridx = 0;

        JTextField txtCode = createStyledTextField();
        JTextField txtTitle = createStyledTextField();

        gbc.gridy = 0; formPanel.add(new JLabel("Course Code:"), gbc);
        gbc.gridy = 1; formPanel.add(txtCode, gbc);
        gbc.gridy = 2; formPanel.add(new JLabel("Course Title:"), gbc);
        gbc.gridy = 3; formPanel.add(txtTitle, gbc);
        gbc.gridy = 4; formPanel.add(new JLabel("Host Department Allocation:"), gbc);
        gbc.gridy = 5; formPanel.add(comboDept, gbc); 

        JButton btnAddCourse = new JButton("Register Course Code");
        stylePrimaryButton(btnAddCourse, MINT_GREEN);
        gbc.gridy = 6; gbc.insets = new Insets(15, 4, 4, 4);
        formPanel.add(btnAddCourse, gbc);
        panel.add(formPanel, BorderLayout.WEST);

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Code", "Title", "Department", "Students Enrolled", "Action"}, 0){
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = createStyledTable(model);
        table.getColumnModel().getColumn(5).setCellRenderer(new ActionButtonRenderer(ORANGE_RED));

        Runnable refreshCourses = () -> {
            model.setRowCount(0);
            var data = controller.getCoursesWithStats("");
            if (data != null) {
                for (Object[] row : data) {
                    model.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], "Delete"});
                }
            }
        };

        btnAddCourse.addActionListener(e -> {
            String selectedDeptName = (String) comboDept.getSelectedItem();
            if (selectedDeptName == null || deptsMap == null) {
                JOptionPane.showMessageDialog(panel, "Please select/create a department first.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String code = txtCode.getText().trim();
            String title = txtTitle.getText().trim();

            if (code.isEmpty() || title.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Course Code and Title cannot be blank.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int deptId = deptsMap.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(selectedDeptName))
                    .map(Map.Entry::getKey).findFirst().orElse(-1);

            boolean success = controller.addCourse(code, title, deptId);
            
            if (success) {
                JOptionPane.showMessageDialog(panel, "Course code created securely!", "Success", JOptionPane.INFORMATION_MESSAGE);
                txtCode.setText(""); 
                txtTitle.setText("");
                refreshCourses.run();
                refreshAllDropdowns(); 
            }
        });

        // FIXED: Added MouseListener handler block to route action column clicks to deleteCourse execution securely
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1 && table.getSelectedColumn() == 5) {
                    int id = (int) table.getValueAt(row, 0);
                    String courseCode = table.getValueAt(row, 1).toString();
                    
                    int confirm = JOptionPane.showConfirmDialog(
                        panel, 
                        "Are you sure you want to delete course " + courseCode + " (ID: " + id + ")?", 
                        "Confirm Course Deletion", 
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (controller.deleteCourse(id)) {
                            JOptionPane.showMessageDialog(panel, "Course deleted successfully.");
                            refreshCourses.run();
                            refreshAllDropdowns();
                        }
                    }
                }
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        refreshCourses.run();
        return panel;
    }

    // ==========================================
    // TAB 3: ENROLLMENTS MANAGEMENT INTERFACE
    // ==========================================
    private JPanel createEnrollmentTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(BACKGROUND_LIGHT);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(320, 0));
        formPanel.setBorder(createCustomTitleBorder("Process Course Enrollment"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4); gbc.weightx = 1.0; gbc.gridx = 0;

        JTextField txtSemester = createStyledTextField();
        txtSemester.setText("Fall 2026");

        gbc.gridy = 0; formPanel.add(new JLabel("Select Student:"), gbc);
        gbc.gridy = 1; formPanel.add(comboStudents, gbc); 
        gbc.gridy = 2; formPanel.add(new JLabel("Select Course Allocation:"), gbc);
        gbc.gridy = 3; formPanel.add(comboCourses, gbc);  
        gbc.gridy = 4; formPanel.add(new JLabel("Academic Term/Semester:"), gbc);
        gbc.gridy = 5; formPanel.add(txtSemester, gbc);

        JButton btnEnroll = new JButton("Finalize Student Enrollment");
        stylePrimaryButton(btnEnroll, DEEP_BLUE);
        gbc.gridy = 6; gbc.insets = new Insets(20, 4, 4, 4);
        formPanel.add(btnEnroll, gbc);
        panel.add(formPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(BACKGROUND_LIGHT);

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterBar.setBackground(Color.WHITE);
        JTextField txtSearch = createStyledTextField(); txtSearch.setPreferredSize(new Dimension(200, 28));
        JButton btnSearch = new JButton("Filter View");
        styleSecondaryButton(btnSearch, DEEP_BLUE);

        filterBar.add(new JLabel("Search Enrollment Matrix:"));
        filterBar.add(txtSearch);
        filterBar.add(btnSearch);
        centerPanel.add(filterBar, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Enroll ID", "Reg Num", "Student Name", "Course Code", "Course Title", "Term"}, 0){
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = createStyledTable(model);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        Runnable refreshEnrollments = () -> {
            model.setRowCount(0);
            var data = controller.getAllEnrollments(txtSearch.getText().trim());
            if (data != null) {
                for (Object[] row : data) {
                    model.addRow(row);
                }
            }
        };

        btnEnroll.addActionListener(e -> {
            if (comboStudents.getSelectedItem() == null || comboCourses.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(panel, "Ensure fields contain data allocations first.");
                return;
            }

            int sId = studentsMap.entrySet().stream().filter(entry -> entry.getValue().equals(comboStudents.getSelectedItem())).map(Map.Entry::getKey).findFirst().orElse(-1);
            int cId = coursesMap.entrySet().stream().filter(entry -> entry.getValue().equals(comboCourses.getSelectedItem())).map(Map.Entry::getKey).findFirst().orElse(-1);

            if (controller.enrollStudent(sId, cId, txtSemester.getText().trim())) {
                JOptionPane.showMessageDialog(panel, "Student enrolled in course successfully!");
                refreshEnrollments.run();
            }
        });

        btnSearch.addActionListener(e -> refreshEnrollments.run());

        refreshEnrollments.run();
        return panel;
    }

    // ==========================================
    // UI CORE STRUCTURAL FACTORIES
    // ==========================================
    private JTextField createStyledTextField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setPreferredSize(new Dimension(0, 30));
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1, true), BorderFactory.createEmptyBorder(2, 6, 2, 6)));
        return f;
    }

    private javax.swing.border.Border createCustomTitleBorder(String title) {
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(220, 224, 230), 1, true), title);
        tb.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        tb.setTitleColor(DEEP_BLUE);
        return BorderFactory.createCompoundBorder(tb, new EmptyBorder(10, 10, 10, 10));
    }

    private void stylePrimaryButton(JButton b, Color bg) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 13)); b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setPreferredSize(new Dimension(0, 36)); b.setFocusPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleSecondaryButton(JButton b, Color bg) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 12)); b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setPreferredSize(new Dimension(100, 28)); b.setFocusPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JTable createStyledTable(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setRowHeight(38);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.getTableHeader().setBackground(DEEP_BLUE);
        t.getTableHeader().setForeground(Color.WHITE);
        t.setGridColor(new Color(240, 242, 245));
        return t;
    }

    private static class ActionButtonRenderer extends JPanel implements TableCellRenderer {
        private final JLabel label;
        public ActionButtonRenderer(Color themeColor) {
            setLayout(new BorderLayout()); setOpaque(true);
            label = new JLabel("", SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12)); label.setForeground(themeColor);
            label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(4, 15, 4, 15), BorderFactory.createLineBorder(themeColor, 1, true)));
            add(label, BorderLayout.CENTER);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object val, boolean isSel, boolean hasF, int r, int c) {
            label.setText(val != null ? val.toString() : "");
            setBackground(isSel ? table.getSelectionBackground() : Color.WHITE);
            return this;
        }
    }
}