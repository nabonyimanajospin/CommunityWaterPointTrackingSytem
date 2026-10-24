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
import model.RepairRecord;
import model.MaintenanceRequest;
import model.MaintenanceTeam;
import rmi.RMIClient;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RepairTracking extends JPanel {
    private JTable repairTable;
    private DefaultTableModel tableModel;
    private JButton refreshBtn, markInProgressBtn, markCompleteBtn, deleteBtn, createRepairBtn;
    private JLabel completedLabel, progressLabel, totalCostLabel, successRateLabel;

    public RepairTracking() {
        initComponents();
        setupUI();
        refreshData();
        refreshAnalytics();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 248, 255));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("🛠️ Repair Tracking & Analytics", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(0, 102, 204));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.3);
        
        JPanel analyticsPanel = createAnalyticsPanel();
        splitPane.setTopComponent(analyticsPanel);
        
        JPanel tablePanel = createTablePanel();
        splitPane.setBottomComponent(tablePanel);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createAnalyticsPanel() {
        JPanel analyticsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        analyticsPanel.setBackground(new Color(240, 248, 255));
        analyticsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        analyticsPanel.add(createAnalyticsCard("✅ Completed Repairs", "0", new Color(34, 139, 34)));
        analyticsPanel.add(createAnalyticsCard("🔄 In Progress", "0", new Color(255, 140, 0)));
        analyticsPanel.add(createAnalyticsCard("💰 Total Cost", "0 RWF", new Color(75, 0, 130)));
        analyticsPanel.add(createAnalyticsCard("📊 Success Rate", "0%", new Color(220, 20, 60)));
        
        return analyticsPanel;
    }
    
    private JPanel createAnalyticsCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 3),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(color);
        
        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setForeground(new Color(50, 50, 50));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        // Store references for dynamic updates
        switch (title) {
            case "✅ Completed Repairs": completedLabel = valueLabel; break;
            case "🔄 In Progress": progressLabel = valueLabel; break;
            case "💰 Total Cost": totalCostLabel = valueLabel; break;
            case "📊 Success Rate": successRateLabel = valueLabel; break;
        }
        
        return card;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel tableTitle = new JLabel("🔧 Repair Records", JLabel.CENTER);
        tableTitle.setFont(new Font("Arial", Font.BOLD, 18));
        tableTitle.setForeground(new Color(0, 102, 204));
        headerPanel.add(tableTitle, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        refreshBtn = createStyledButton("🔄 Refresh", new Color(70, 130, 180));
        createRepairBtn = createStyledButton("➕ Create Repair", new Color(0, 128, 0));
        markInProgressBtn = createStyledButton("⚡ Mark In Progress", new Color(255, 165, 0));
        markCompleteBtn = createStyledButton("✅ Mark Complete", new Color(34, 139, 34));
        deleteBtn = createStyledButton("🗑️ Delete", new Color(178, 34, 34));
        
        buttonPanel.add(refreshBtn);
        buttonPanel.add(createRepairBtn);
        buttonPanel.add(markInProgressBtn);
        buttonPanel.add(markCompleteBtn);
        buttonPanel.add(deleteBtn);
        
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        tablePanel.add(headerPanel, BorderLayout.NORTH);
        
        String[] columns = {"ID", "Request ID", "Team ID", "Start Date", "End Date", "Cost", "Materials", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        repairTable = new JTable(tableModel);
        repairTable.setRowHeight(30);
        repairTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        repairTable.getColumnModel().getColumn(7).setCellRenderer(new StatusCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(repairTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    class StatusCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Arial", Font.BOLD, 11));
            
            if (column == 7 && value != null) {
                String status = value.toString();
                switch (status) {
                    case "Completed":
                        c.setBackground(new Color(144, 238, 144));
                        c.setForeground(Color.BLACK);
                        break;
                    case "In Progress":
                        c.setBackground(new Color(255, 255, 153));
                        c.setForeground(Color.BLACK);
                        break;
                    case "Pending":
                        c.setBackground(new Color(255, 193, 153));
                        c.setForeground(Color.BLACK);
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                }
            } else {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }
            
            if (isSelected) {
                c.setBackground(new Color(173, 216, 230));
                c.setForeground(Color.BLACK);
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
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    private void setupUI() {
        refreshBtn.addActionListener(e -> {
            refreshData();
            refreshAnalytics();
        });
        
        createRepairBtn.addActionListener(e -> createNewRepair());
        markInProgressBtn.addActionListener(e -> markRepairInProgress());
        markCompleteBtn.addActionListener(e -> markRepairComplete());
        deleteBtn.addActionListener(e -> deleteRepair());
    }
    
    private void createNewRepair() {
        try {
            List<MaintenanceRequest> allRequests = RMIClient.getMaintenanceRequestService().getAllMaintenanceRequests();
            java.util.List<MaintenanceRequest> pendingRequests = new java.util.ArrayList<>();
            
            for (MaintenanceRequest req : allRequests) {
                if (!"Completed".equals(req.getStatus())) {
                    pendingRequests.add(req);
                }
            }
            
            if (pendingRequests.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No pending maintenance requests available!\nPlease create maintenance requests first.",
                    "No Pending Requests", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            List<MaintenanceTeam> allTeams = RMIClient.getMaintenanceTeamService().getAllMaintenanceTeams();
            java.util.List<MaintenanceTeam> availableTeams = new java.util.ArrayList<>();
            
            // Only show Available and Busy teams, not On Leave
            for (MaintenanceTeam team : allTeams) {
                String status = team.getStatus();
                if ("Available".equals(status) || "Busy".equals(status)) {
                    availableTeams.add(team);
                }
            }
            
            if (availableTeams.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No available teams!\nPlease check team availability or create new teams.",
                    "No Available Teams", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JDialog createDialog = new JDialog();
            createDialog.setTitle("➕ Create New Repair Record");
            createDialog.setModal(true);
            createDialog.setLayout(new BorderLayout(10, 10));
            createDialog.setSize(500, 300);
            createDialog.setLocationRelativeTo(this);
            
            JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel requestLabel = new JLabel("Maintenance Request:");
            JComboBox<String> requestCombo = new JComboBox<>();
            for (MaintenanceRequest req : pendingRequests) {
                requestCombo.addItem("Request #" + req.getId() + " - " + req.getDescription());
            }
            
            JLabel teamLabel = new JLabel("Assign Team:");
            JComboBox<String> teamCombo = new JComboBox<>();
            for (MaintenanceTeam team : availableTeams) {
                teamCombo.addItem("Team #" + team.getId() + " - " + team.getTeamName() + " (" + team.getSpecialization() + ")");
            }
            
            JLabel materialsLabel = new JLabel("Initial Materials:");
            JTextField materialsField = new JTextField();
            
            formPanel.add(requestLabel);
            formPanel.add(requestCombo);
            formPanel.add(teamLabel);
            formPanel.add(teamCombo);
            formPanel.add(materialsLabel);
            formPanel.add(materialsField);
            
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton createButton = new JButton("Create Repair");
            JButton cancelButton = new JButton("Cancel");
            
            createButton.setBackground(new Color(0, 128, 0));
            createButton.setForeground(Color.BLACK);
            cancelButton.setBackground(new Color(178, 34, 34));
            cancelButton.setForeground(Color.BLACK);
            
            buttonPanel.add(createButton);
            buttonPanel.add(cancelButton);
            
            createDialog.add(formPanel, BorderLayout.CENTER);
            createDialog.add(buttonPanel, BorderLayout.SOUTH);
            
            createButton.addActionListener(e -> {
                if (requestCombo.getSelectedIndex() == -1 || teamCombo.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(createDialog, "Please select both request and team!", "Selection Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int selectedRequestIndex = requestCombo.getSelectedIndex();
                int selectedTeamIndex = teamCombo.getSelectedIndex();
                
                MaintenanceRequest selectedRequest = pendingRequests.get(selectedRequestIndex);
                MaintenanceTeam selectedTeam = availableTeams.get(selectedTeamIndex);
                
                String materials = materialsField.getText().trim();
                if (materials.isEmpty()) {
                    materials = "Standard repair materials";
                }
                
                try {
                    RepairRecord record = new RepairRecord();
                    record.setRequestId(selectedRequest.getId());
                    record.setTeamId(selectedTeam.getId());
                    record.setStartDate(new Date());
                    record.setRepairStatus("Pending");
                    record.setMaterialsUsed(materials);
                    record.setCost(0.0);
                    
                    RepairRecord result = RMIClient.getRepairRecordService().createRepairRecord(record);
                    
                    if (result != null) {
                        JOptionPane.showMessageDialog(createDialog, 
                            "Repair record created successfully!\n" +
                            "Assigned Team: " + selectedTeam.getTeamName() + "\n" +
                            "For Request: " + selectedRequest.getDescription(),
                            "Success", 
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        createDialog.dispose();
                        refreshData();
                        refreshAnalytics();
                    } else {
                        JOptionPane.showMessageDialog(createDialog, 
                            "Failed to create repair record!", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    String errorMessage = ex.getMessage();
                    if (errorMessage != null && errorMessage.contains(":")) {
                        // Extract the actual business logic error message
                        String[] parts = errorMessage.split(":");
                        errorMessage = parts[parts.length - 1].trim();
                    }
                    JOptionPane.showMessageDialog(createDialog, 
                        errorMessage != null ? errorMessage : "Failed to create repair record", 
                        "Business Logic Error", 
                        JOptionPane.WARNING_MESSAGE);
                }
            });
            
            cancelButton.addActionListener(e -> createDialog.dispose());
            
            createDialog.setVisible(true);
            
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            if (errorMessage != null && errorMessage.contains(":")) {
                String[] parts = errorMessage.split(":");
                errorMessage = parts[parts.length - 1].trim();
            }
            JOptionPane.showMessageDialog(this, 
                errorMessage != null ? errorMessage : "Failed to create repair", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void markRepairInProgress() {
        int selectedRow = repairTable.getSelectedRow();
        if (selectedRow >= 0) {
            int recordId = (int) tableModel.getValueAt(selectedRow, 0);
            String currentStatus = tableModel.getValueAt(selectedRow, 7).toString();
            
            if ("In Progress".equals(currentStatus)) {
                JOptionPane.showMessageDialog(this, "This repair is already in progress!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            if ("Completed".equals(currentStatus)) {
                JOptionPane.showMessageDialog(this, "Cannot change completed repairs to in progress!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Mark this repair as in progress?",
                "Confirm Status Change", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    RepairRecord record = RMIClient.getRepairRecordService().getRepairRecordById(recordId);
                    if (record != null) {
                        record.setRepairStatus("In Progress");
                        RepairRecord result = RMIClient.getRepairRecordService().updateRepairRecord(record);
                        
                        if (result != null) {
                            JOptionPane.showMessageDialog(this, "Repair marked as in progress!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            refreshData();
                            refreshAnalytics();
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to update repair status!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception ex) {
                    String errorMessage = ex.getMessage();
                    if (errorMessage != null && errorMessage.contains(":")) {
                        String[] parts = errorMessage.split(":");
                        errorMessage = parts[parts.length - 1].trim();
                    }
                    JOptionPane.showMessageDialog(this, 
                        errorMessage != null ? errorMessage : "Failed to update repair status", 
                        "Update Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a repair to mark as in progress!", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void markRepairComplete() {
        int selectedRow = repairTable.getSelectedRow();
        if (selectedRow >= 0) {
            int recordId = (int) tableModel.getValueAt(selectedRow, 0);
            String currentStatus = tableModel.getValueAt(selectedRow, 7).toString();
            
            if ("Completed".equals(currentStatus)) {
                JOptionPane.showMessageDialog(this, "This repair is already completed!", "Already Completed", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            if ("Pending".equals(currentStatus)) {
                JOptionPane.showMessageDialog(this, 
                    "Cannot mark repair as completed directly from Pending status!\n\n" +
                    "Please mark it as 'In Progress' first, then complete it.",
                    "Invalid Status Change", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JTextField costField = new JTextField();
            JTextField materialsField = new JTextField();
            
            Object[] message = {
                "Completion Cost (RWF):", costField,
                "Materials Used:", materialsField
            };
            
            int option = JOptionPane.showConfirmDialog(this, message, 
                "Complete Repair Details", JOptionPane.OK_CANCEL_OPTION);
            
            if (option == JOptionPane.OK_OPTION) {
                try {
                    double cost = Double.parseDouble(costField.getText());
                    String materials = materialsField.getText();
                    
                    if (cost <= 0) {
                        JOptionPane.showMessageDialog(this, "Cost must be greater than 0!", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    if (materials.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please specify materials used!", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    int confirm = JOptionPane.showConfirmDialog(this,
                        "Mark this repair as completed?\nCost: " + String.format("%,.0f", cost) + " RWF\nMaterials: " + materials,
                        "Confirm Completion", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            RepairRecord record = RMIClient.getRepairRecordService().getRepairRecordById(recordId);
                            if (record != null) {
                                record.setRepairStatus("Completed");
                                record.setEndDate(new Date());
                                record.setCost(cost);
                                record.setMaterialsUsed(materials);
                                
                                RepairRecord result = RMIClient.getRepairRecordService().updateRepairRecord(record);
                                
                                if (result != null) {
                                    JOptionPane.showMessageDialog(this, "Repair marked as completed!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                    refreshData();
                                    refreshAnalytics();
                                } else {
                                    JOptionPane.showMessageDialog(this, "Failed to complete repair!", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } catch (Exception ex) {
                            String errorMessage = ex.getMessage();
                            if (errorMessage != null && errorMessage.contains(":")) {
                                String[] parts = errorMessage.split(":");
                                errorMessage = parts[parts.length - 1].trim();
                            }
                            JOptionPane.showMessageDialog(this, 
                                errorMessage != null ? errorMessage : "Failed to complete repair", 
                                "Completion Error", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid cost number!", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a repair to mark as complete!", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteRepair() {
        int selectedRow = repairTable.getSelectedRow();
        if (selectedRow >= 0) {
            int recordId = (int) tableModel.getValueAt(selectedRow, 0);
            String status = tableModel.getValueAt(selectedRow, 7).toString();
            
            if ("Completed".equals(status)) {
                JOptionPane.showMessageDialog(this, "Cannot delete completed repairs!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this repair record?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean result = RMIClient.getRepairRecordService().deleteRepairRecord(recordId);
                    
                    if (result) {
                        JOptionPane.showMessageDialog(this, "Repair record deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshData();
                        refreshAnalytics();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete repair record!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    String errorMessage = ex.getMessage();
                    if (errorMessage != null && errorMessage.contains(":")) {
                        String[] parts = errorMessage.split(":");
                        errorMessage = parts[parts.length - 1].trim();
                    }
                    JOptionPane.showMessageDialog(this, 
                        errorMessage != null ? errorMessage : "Failed to delete repair", 
                        "Deletion Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a repair to delete!", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void refreshData() {
        try {
            tableModel.setRowCount(0);
            
            List<RepairRecord> records = RMIClient.getRepairRecordService().getAllRepairRecords();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            for (RepairRecord record : records) {
                tableModel.addRow(new Object[]{
                    record.getId(),
                    record.getRequestId(),
                    record.getTeamId(),
                    sdf.format(record.getStartDate()),
                    record.getEndDate() != null ? sdf.format(record.getEndDate()) : "Ongoing",
                    record.getCost() > 0 ? String.format("%,.0f RWF", record.getCost()) : "Not set",
                    record.getMaterialsUsed() != null ? record.getMaterialsUsed() : "Not specified",
                    record.getRepairStatus()
                });
            }
        } catch (Exception ex) {
            String errorMessage = ex.getMessage();
            if (errorMessage != null && errorMessage.contains(":")) {
                String[] parts = errorMessage.split(":");
                errorMessage = parts[parts.length - 1].trim();
            }
            JOptionPane.showMessageDialog(this, 
                "Error loading data: " + (errorMessage != null ? errorMessage : "Connection failed"), 
                "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void refreshAnalytics() {
        try {
            List<RepairRecord> records = RMIClient.getRepairRecordService().getAllRepairRecords();
            
            int completed = 0;
            int inProgress = 0;
            double totalCost = 0;
            
            for (RepairRecord record : records) {
                if ("Completed".equals(record.getRepairStatus())) {
                    completed++;
                    totalCost += record.getCost();
                } else if ("In Progress".equals(record.getRepairStatus())) {
                    inProgress++;
                }
            }
            
            double successRate = records.size() > 0 ? (completed * 100.0 / records.size()) : 0;
            
            if (completedLabel != null) completedLabel.setText(String.valueOf(completed));
            if (progressLabel != null) progressLabel.setText(String.valueOf(inProgress));
            if (totalCostLabel != null) totalCostLabel.setText(String.format("%,.0f RWF", totalCost));
            if (successRateLabel != null) successRateLabel.setText(String.format("%.1f%%", successRate));
            
        } catch (Exception ex) {
            System.err.println("Error refreshing analytics: " + ex.getMessage());
        }
    }
}