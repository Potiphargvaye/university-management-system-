package view;

import controller.StudentController;
import model.Student;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class StudentPanel extends JPanel {
    private JTextField txtReg, txtName, txtAge, txtGpa, txtSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnSearch, btnRefresh;
    private StudentController controller;

    // Brand Color Palette Harmony
    private final Color DEEP_BLUE = new Color(24, 43, 73);
    private final Color MINT_GREEN = new Color(40, 167, 69);    // Green for Edit
    private final Color ORANGE_RED = new Color(221, 74, 46);    // Red for Delete
    private final Color BACKGROUND_LIGHT = new Color(245, 247, 250);
    private final Color TEXT_DARK = new Color(51, 51, 51);

    public StudentPanel() {
        controller = new StudentController();
        setLayout(new BorderLayout(15, 15));
        setBackground(BACKGROUND_LIGHT);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // ==========================================
        // 1. LEFT PANEL: Registration Form
        // ==========================================
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(320, 0));
        
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 224, 230), 1, true), 
            "Student Registration"
        );
        titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        titledBorder.setTitleColor(DEEP_BLUE);
        
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            titledBorder, 
            new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.weightx = 1.0;

        txtReg = createStyledTextField();
        txtName = createStyledTextField();
        txtAge = createStyledTextField();
        txtGpa = createStyledTextField();

        addFormRow(formPanel, "Registration Number", txtReg, gbc, 0);
        addFormRow(formPanel, "Full Name", txtName, gbc, 2);
        addFormRow(formPanel, "Age", txtAge, gbc, 4);
        addFormRow(formPanel, "GPA Score", txtGpa, gbc, 6);

        btnAdd = new JButton("Add New Student");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAdd.setBackground(MINT_GREEN);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setPreferredSize(new Dimension(0, 36));
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 4, 4, 4);
        formPanel.add(btnAdd, gbc);

        add(formPanel, BorderLayout.WEST);

        // ==========================================
        // 2. RIGHT PANEL: Search & Modern Data Table
        // ==========================================
        JPanel displayPanel = new JPanel(new BorderLayout(10, 10));
        displayPanel.setBackground(BACKGROUND_LIGHT);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 235, 240), 1, true),
            new EmptyBorder(8, 10, 8, 10)
        ));
        
        JLabel lblSearch = new JLabel("Search Target:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSearch.setForeground(TEXT_DARK);
        
        txtSearch = createStyledTextField();
        txtSearch.setPreferredSize(new Dimension(180, 28));
        
        btnSearch = new JButton("Search");
        styleSecondaryButton(btnSearch, DEEP_BLUE);
        
        btnRefresh = new JButton("Refresh");
        styleSecondaryButton(btnRefresh, new Color(110, 120, 135));

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        displayPanel.add(searchPanel, BorderLayout.NORTH);

        String[] columns = {"Reg Number", "Full Name", "Age", "GPA", "Edit Action", "Delete Action"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(38); // Increased height slightly to allow breathing room for the button outlines
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(DEEP_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(240, 242, 245));
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(TEXT_DARK);

        // --- CUSTOM BUTTON RENDERERS FOR THE TABLE ACTIONS ---
        table.getColumnModel().getColumn(4).setCellRenderer(new ActionButtonRenderer(MINT_GREEN));
        table.getColumnModel().getColumn(5).setCellRenderer(new ActionButtonRenderer(ORANGE_RED));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 235, 240)));
        displayPanel.add(scrollPane, BorderLayout.CENTER);

        add(displayPanel, BorderLayout.CENTER);

        // ==========================================
        // 3. LISTENERS & INTERACTIVE TRIGGERS
        // ==========================================
        btnAdd.addActionListener(e -> saveStudent());
        btnSearch.addActionListener(e -> refreshTableData(txtSearch.getText().trim()));
        btnRefresh.addActionListener(e -> { txtSearch.setText(""); refreshTableData(""); });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();
                if (row == -1) return;

                String regNum = table.getValueAt(row, 0).toString();
                String fullName = table.getValueAt(row, 1).toString();
                String age = table.getValueAt(row, 2).toString();
                String gpa = table.getValueAt(row, 3).toString();

                if (col == 4) { 
                    openEditModal(regNum, fullName, age, gpa);
                } else if (col == 5) { 
                    int confirm = JOptionPane.showConfirmDialog(StudentPanel.this, 
                            "Are you sure you want to delete student " + regNum + "?", 
                            "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (controller.deleteStudent(regNum)) {
                            JOptionPane.showMessageDialog(StudentPanel.this, "Student entry cleanly removed.");
                            refreshTableData("");
                        }
                    }
                }
            }
        });

        refreshTableData("");
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(0, 30));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(204, 204, 204), 1, true),
            BorderFactory.createEmptyBorder(2, 6, 2, 6)
        ));
        return field;
    }

    private void styleSecondaryButton(JButton btn, Color bg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(90, 28));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void addFormRow(JPanel panel, String labelText, JTextField field, GridBagConstraints gbc, int rowY) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_DARK);
        
        gbc.gridx = 0; gbc.gridy = rowY; gbc.gridwidth = 2;
        panel.add(label, gbc);
        
        gbc.gridy = rowY + 1;
        panel.add(field, gbc);
    }

    private void refreshTableData(String keyword) {
        tableModel.setRowCount(0);
        List<Student> students = controller.getAllStudents(keyword);
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                s.getRegistrationNumber(), 
                s.getFullName(), 
                s.getAge(), 
                s.getGpa(), 
                "Edit",     // String matching the renderer custom design
                "Delete"    // String matching the renderer custom design
            });
        }
    }

    private void saveStudent() {
        try {
            Student s = new Student(0, txtReg.getText().trim(), txtName.getText().trim(), Integer.parseInt(txtAge.getText().trim()), 0, Double.parseDouble(txtGpa.getText().trim()));
            if(controller.addStudent(s)) {
                JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                txtReg.setText(""); txtName.setText(""); txtAge.setText(""); txtGpa.setText("");
                refreshTableData("");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Validation Error: Verify numeric score formats.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openEditModal(String reg, String name, String age, String gpa) {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JDialog editDialog = new JDialog(topFrame, "Modify Entry Record", true);
        editDialog.setSize(360, 280);
        editDialog.setLocationRelativeTo(this);
        editDialog.setLayout(new GridLayout(5, 2, 12, 12));
        ((JPanel)editDialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        editDialog.getContentPane().setBackground(Color.WHITE);

        JTextField mName = createStyledTextField(); mName.setText(name);
        JTextField mAge = createStyledTextField(); mAge.setText(age);
        JTextField mGpa = createStyledTextField(); mGpa.setText(gpa);
        
        JButton btnUpdate = new JButton("Update Profile");
        btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnUpdate.setBackground(DEEP_BLUE);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);

        JLabel lblRegTitle = new JLabel("Reg Number:"); lblRegTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JLabel lblRegVal = new JLabel(reg); lblRegVal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRegVal.setForeground(ORANGE_RED);

        editDialog.add(lblRegTitle); editDialog.add(lblRegVal);
        editDialog.add(new JLabel("Full Name:")); editDialog.add(mName);
        editDialog.add(new JLabel("Age:")); editDialog.add(mAge);
        editDialog.add(new JLabel("GPA:")); editDialog.add(mGpa);
        editDialog.add(new JLabel("")); editDialog.add(btnUpdate);

        btnUpdate.addActionListener(e -> {
            try {
                Student updatedStudent = new Student(0, reg, mName.getText().trim(), 
                        Integer.parseInt(mAge.getText().trim()), 0, Double.parseDouble(mGpa.getText().trim()));
                
                if (controller.updateStudent(updatedStudent)) {
                    JOptionPane.showMessageDialog(editDialog, "Profile changes committed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    editDialog.dispose();
                    refreshTableData(""); 
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(editDialog, "Invalid entries. Please fix parameters.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        editDialog.setVisible(true);
    }

    // =========================================================================
    // INNER HELPER CLASS: CUSTOM COMPONENT BORDER BADGE CELL RENDERER
    // =========================================================================
    private static class ActionButtonRenderer extends JPanel implements TableCellRenderer {
        private final JLabel label;

        public ActionButtonRenderer(Color themeColor) {
            setLayout(new BorderLayout());
            setOpaque(true);
            
            label = new JLabel("", SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(themeColor);
            
            // Creates the exact empty external padding + internal bordered outline look from the screenshot
            label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 15, 4, 15), // Outer margin inside cell
                BorderFactory.createLineBorder(themeColor, 1, true) // Pill line container outline
            ));
            
            add(label, BorderLayout.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                                                       boolean hasFocus, int row, int column) {
            label.setText(value != null ? value.toString() : "");
            
            // Maintain matching row background selections cleanly
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(Color.WHITE);
            }
            return this;
        }
    }
}