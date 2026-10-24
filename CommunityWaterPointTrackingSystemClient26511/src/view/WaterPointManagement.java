/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 *
 * @author Jospin Nabonyimana
 */
import model.WaterPoint;
import rmi.RMIClient;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WaterPointManagement extends JPanel {
    private JTextField locationField, capacityField;
    private JComboBox<String> waterTypeCombo, statusCombo;
    private JTextField installationDateField;
    private JButton saveBtn, updateBtn, deleteBtn, clearBtn, searchBtn, backBtn;
    private JTable waterPointTable;
    private DefaultTableModel tableModel;
    private JPanel tableHeaderPanel;
    
    private Color operationalColor = new Color(144, 238, 144);
    private Color brokenColor = new Color(255, 182, 193);
    private Color repairColor = new Color(255, 255, 153);

    public WaterPointManagement() {
        initComponents();
        setupUI();
        refreshData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.4);
        
        JPanel leftPanel = createLeftPanel();
        splitPane.setLeftComponent(leftPanel);
        
        JPanel rightPanel = createRightPanel();
        splitPane.setRightComponent(rightPanel);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBackground(new Color(220, 230, 241));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("Water Point Management", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        leftPanel.add(title, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBackground(new Color(220, 230, 241));
        
        formPanel.add(new JLabel("Location:"));
        locationField = new JTextField();
        formPanel.add(locationField);
        
        formPanel.add(new JLabel("Water Type:"));
        waterTypeCombo = new JComboBox<>(new String[]{"Borehole", "Tap", "Well", "Spring"});
        formPanel.add(waterTypeCombo);
        
        formPanel.add(new JLabel("Status:"));
        statusCombo = new JComboBox<>(new String[]{"Operational", "Broken", "Under Repair"});
        formPanel.add(statusCombo);
        
        formPanel.add(new JLabel("Installation Date:"));
        installationDateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        formPanel.add(installationDateField);
        
        formPanel.add(new JLabel("Capacity (liters):"));
        capacityField = new JTextField();
        formPanel.add(capacityField);
        
        leftPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        buttonPanel.setBackground(new Color(220, 230, 241));
        
        saveBtn = createStyledButton("Save", new Color(76, 175, 80));
        updateBtn = createStyledButton("Update", new Color(33, 150, 243));
        deleteBtn = createStyledButton("Delete", new Color(244, 67, 54));
        clearBtn = createStyledButton("Clear", new Color(255, 152, 0));
        searchBtn = createStyledButton("Search", new Color(156, 39, 176));
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(searchBtn);
        
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return leftPanel;
    }
    
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create header panel with title and back button
        tableHeaderPanel = new JPanel(new BorderLayout());
        tableHeaderPanel.setBackground(Color.WHITE);
        
        JLabel tableTitle = new JLabel("Water Points List", JLabel.CENTER);
        tableTitle.setFont(new Font("Arial", Font.BOLD, 16));
        tableHeaderPanel.add(tableTitle, BorderLayout.CENTER);
        
        // Create back button (initially hidden)
        backBtn = createStyledButton("Back to All", new Color(100, 100, 100));
        backBtn.setVisible(false); // Hidden by default
        tableHeaderPanel.add(backBtn, BorderLayout.EAST);
        
        rightPanel.add(tableHeaderPanel, BorderLayout.NORTH);
        
        String[] columns = {"No.", "Location", "Type", "Status", "Install Date", "Capacity"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        waterPointTable = new JTable(tableModel);
        waterPointTable.setRowHeight(30);
        waterPointTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        waterPointTable.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(waterPointTable);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        
        return rightPanel;
    }
    
    class StatusCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (column == 3) {
                String status = value.toString();
                switch (status) {
                    case "Operational":
                        c.setBackground(operationalColor);
                        break;
                    case "Broken":
                        c.setBackground(brokenColor);
                        break;
                    case "Under Repair":
                        c.setBackground(repairColor);
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                }
                setHorizontalAlignment(JLabel.CENTER);
            } else {
                c.setBackground(Color.WHITE);
            }
            
            if (isSelected) {
                c.setBackground(new Color(173, 216, 230));
            }
            
            return c;
        }
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        return button;
    }

    private void setupUI() {
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                saveWaterPoint();
            }
        });
        
        updateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                updateWaterPoint();
            }
        });
        
        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                deleteWaterPoint();
            }
        });
        
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                clearForm();
            }
        });
        
        searchBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String searchLocation = JOptionPane.showInputDialog(WaterPointManagement.this, 
                    "Enter location to search:", "Search Water Points", JOptionPane.QUESTION_MESSAGE);
                
                if (searchLocation != null && !searchLocation.trim().isEmpty()) {
                    searchWaterPoints(searchLocation.trim());
                } else if (searchLocation != null) {
                    JOptionPane.showMessageDialog(WaterPointManagement.this, 
                        "Please enter a location to search!", "Search Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        // Back button action - returns to main list
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                refreshData(); // Show all water points
                backBtn.setVisible(false); // Hide back button
                
                // Update table title
                JLabel tableTitle = (JLabel) ((BorderLayout) tableHeaderPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
                tableTitle.setText("Water Points List");
            }
        });
        
        waterPointTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedWaterPoint();
            }
        });
    }
    
    private void saveWaterPoint() {
        try {
            if (locationField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Location is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                locationField.requestFocus();
                return;
            }
            
            int capacity;
            try {
                capacity = Integer.parseInt(capacityField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Capacity must be a valid number!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                capacityField.requestFocus();
                return;
            }
            
            if (capacity <= 0 || capacity > 100000) {
                JOptionPane.showMessageDialog(this, "Capacity must be between 1 and 100,000 liters!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                capacityField.requestFocus();
                return;
            }
            
            if (!isValidDate(installationDateField.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Please enter date in YYYY-MM-DD format!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                installationDateField.requestFocus();
                return;
            }
            
            Date installDate = new SimpleDateFormat("yyyy-MM-dd").parse(installationDateField.getText().trim());
            Date today = new Date();
            
            // Remove time component for accurate date comparison
            SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
            Date todayDateOnly = dateOnly.parse(dateOnly.format(today));
            Date installDateOnly = dateOnly.parse(dateOnly.format(installDate));
            
            if (installDateOnly.before(todayDateOnly)) {
                JOptionPane.showMessageDialog(this, 
                    "Installation date cannot be in the past!\nPlease use today's date or a future date.", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                installationDateField.requestFocus();
                return;
            }
            
            WaterPoint waterPoint = new WaterPoint();
            waterPoint.setLocation(locationField.getText().trim());
            waterPoint.setWaterType(waterTypeCombo.getSelectedItem().toString());
            waterPoint.setStatus(statusCombo.getSelectedItem().toString());
            waterPoint.setInstallationDate(installDate);
            waterPoint.setCapacity(capacity);
            
            WaterPoint result = RMIClient.getWaterPointService().createWaterPoint(waterPoint);
            
            if (result != null) {
                // Create real notification for water point creation
                try {
                    model.Notification notification = new model.Notification();
                    notification.setUserId(1);
                    notification.setType("WATER_POINT");
                    notification.setTitle("New Water Point Added");
                    notification.setMessage("Water point at '" + waterPoint.getLocation() + "' has been successfully created. Type: " + waterPoint.getWaterType() + ", Capacity: " + waterPoint.getCapacity() + " liters");
                    notification.setRecipient("admin@waterpoint.com");
                    RMIClient.getNotificationService().createNotification(notification);
                } catch (Exception ex) {
                    // Notification failed but don't stop the main operation
                }
                
                JOptionPane.showMessageDialog(this, "Water point saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save water point!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            // Extract clean message from RMI exception
            if (errorMessage != null && errorMessage.contains("Capacity must be")) {
                int startIndex = errorMessage.indexOf("Capacity must be");
                if (startIndex != -1) {
                    errorMessage = errorMessage.substring(startIndex);
                    // Remove any trailing text after "liters"
                    int endIndex = errorMessage.indexOf(" liters");
                    if (endIndex != -1) {
                        errorMessage = errorMessage.substring(0, endIndex + 7); // +7 for " liters"
                    }
                }
            } else if (errorMessage != null && errorMessage.contains("Error creating water point:")) {
                int startIndex = errorMessage.indexOf("Error creating water point:") + "Error creating water point:".length();
                errorMessage = errorMessage.substring(startIndex).trim();
            }
            JOptionPane.showMessageDialog(this, errorMessage, "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateWaterPoint() {
        int selectedRow = waterPointTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                List<WaterPoint> allWaterPoints = RMIClient.getWaterPointService().getAllWaterPoints();
                int displayNumber = (int) tableModel.getValueAt(selectedRow, 0);
                
                WaterPoint selectedWaterPoint = null;
                int currentIndex = 0;
                for (WaterPoint wp : allWaterPoints) {
                    if (currentIndex == displayNumber - 1) {
                        selectedWaterPoint = wp;
                        break;
                    }
                    currentIndex++;
                }
                
                if (selectedWaterPoint == null) {
                    JOptionPane.showMessageDialog(this, "Error: Could not find selected water point!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String currentLocation = selectedWaterPoint.getLocation();
                
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to update water point:\n" + currentLocation + "?",
                    "Confirm Update", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
                
                if (locationField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Location is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int capacity;
                try {
                    capacity = Integer.parseInt(capacityField.getText().trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Capacity must be a valid number!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!isValidDate(installationDateField.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "Please enter date in YYYY-MM-DD format!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                WaterPoint waterPoint = new WaterPoint();
                waterPoint.setId(selectedWaterPoint.getId());
                waterPoint.setLocation(locationField.getText().trim());
                waterPoint.setWaterType(waterTypeCombo.getSelectedItem().toString());
                waterPoint.setStatus(statusCombo.getSelectedItem().toString());
                waterPoint.setInstallationDate(new SimpleDateFormat("yyyy-MM-dd").parse(installationDateField.getText().trim()));
                waterPoint.setCapacity(capacity);
                
                WaterPoint result = RMIClient.getWaterPointService().updateWaterPoint(waterPoint);
                
                if (result != null) {
                    JOptionPane.showMessageDialog(this, "Water point updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update water point!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a water point to update!", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteWaterPoint() {
    int selectedRow = waterPointTable.getSelectedRow();
    if (selectedRow >= 0) {
        try {
            List<WaterPoint> allWaterPoints = RMIClient.getWaterPointService().getAllWaterPoints();
            int displayNumber = (int) tableModel.getValueAt(selectedRow, 0);
            
            WaterPoint selectedWaterPoint = null;
            int currentIndex = 0;
            for (WaterPoint wp : allWaterPoints) {
                if (currentIndex == displayNumber - 1) {
                    selectedWaterPoint = wp;
                    break;
                }
                currentIndex++;
            }
            
            if (selectedWaterPoint == null) {
                JOptionPane.showMessageDialog(this, "Error: Could not find selected water point!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String location = selectedWaterPoint.getLocation();
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to DELETE water point:\n" + location + "?\n\nThis action cannot be undone!", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            boolean result = RMIClient.getWaterPointService().deleteWaterPoint(selectedWaterPoint.getId());
            
            if (result) {
                JOptionPane.showMessageDialog(this, "Water point deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            
            refreshData();
            clearForm();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Please select a water point to delete!", "Selection Error", JOptionPane.WARNING_MESSAGE);
    }
}
    
    private void searchWaterPoints(String location) {
        try {
            List<WaterPoint> allWaterPoints = RMIClient.getWaterPointService().getAllWaterPoints();
            List<WaterPoint> searchResults = new java.util.ArrayList<>();
            
            for (WaterPoint wp : allWaterPoints) {
                if (wp.getLocation().toLowerCase().contains(location.toLowerCase())) {
                    searchResults.add(wp);
                }
            }
            
            if (searchResults.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No water points found for location: " + location, 
                    "Search Results", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                tableModel.setRowCount(0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                
                int displayNumber = 1;
                for (WaterPoint wp : searchResults) {
                    tableModel.addRow(new Object[]{
                        displayNumber++,
                        wp.getLocation(),
                        wp.getWaterType(),
                        wp.getStatus(),
                        sdf.format(wp.getInstallationDate()),
                        wp.getCapacity()
                    });
                }
                
                backBtn.setVisible(true);
                JLabel tableTitle = (JLabel) ((BorderLayout) tableHeaderPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
                tableTitle.setText("Search Results for: " + location);
                
                JOptionPane.showMessageDialog(this, 
                    "Found " + searchResults.size() + " water point(s) for: " + location, 
                    "Search Results", JOptionPane.INFORMATION_MESSAGE);
                
                if (searchResults.size() > 0) {
                    waterPointTable.setRowSelectionInterval(0, 0);
                    waterPointTable.scrollRectToVisible(waterPointTable.getCellRect(0, 0, true));
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error searching: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        locationField.setText("");
        capacityField.setText("");
        waterTypeCombo.setSelectedIndex(0);
        statusCombo.setSelectedIndex(0);
        installationDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        waterPointTable.clearSelection();
    }
    
    private void loadSelectedWaterPoint() {
        int selectedRow = waterPointTable.getSelectedRow();
        if (selectedRow >= 0) {
            locationField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            waterTypeCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
            statusCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());
            installationDateField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            capacityField.setText(tableModel.getValueAt(selectedRow, 5).toString());
        }
    }
    
    private boolean isValidDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public void refreshData() {
        try {
            tableModel.setRowCount(0);
            
            List<WaterPoint> waterPoints = RMIClient.getWaterPointService().getAllWaterPoints();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            int displayNumber = 1;
            for (WaterPoint wp : waterPoints) {
                tableModel.addRow(new Object[]{
                    displayNumber++,
                    wp.getLocation(),
                    wp.getWaterType(),
                    wp.getStatus(),
                    sdf.format(wp.getInstallationDate()),
                    wp.getCapacity()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}