package utils;

import model.*;
import java.io.*;
import java.util.List;
import java.text.SimpleDateFormat;

public class ReportGenerator {
    
    public static void generatePDFReport(String reportType, List<?> data, String filePath) throws Exception {
        // For exam purposes, generate a well-formatted text file that can be converted to PDF
        StringBuilder content = new StringBuilder();
        
        content.append("COMMUNITY WATER POINT TRACKING SYSTEM\n");
        content.append("=====================================\n\n");
        content.append("Report Type: ").append(reportType).append("\n");
        content.append("Generated: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())).append("\n");
        content.append("=====================================\n\n");
        
        switch (reportType) {
            case "Water Points Summary":
                generateWaterPointsPDF(content, (List<WaterPoint>) data);
                break;
            case "Repair Records Report":
                generateRepairRecordsPDF(content, (List<RepairRecord>) data);
                break;
            case "Maintenance Requests Report":
                generateMaintenanceRequestsPDF(content, (List<MaintenanceRequest>) data);
                break;
        }
        
        // Write to file
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content.toString());
        }
    }
    
    public static void generateExcelReport(String reportType, List<?> data, String filePath) throws Exception {
        // Generate CSV format (Excel-compatible)
        StringBuilder csv = new StringBuilder();
        
        switch (reportType) {
            case "Water Points Summary":
                csv.append("ID,Location,Type,Status,Capacity,Installation Date\n");
                for (WaterPoint wp : (List<WaterPoint>) data) {
                    csv.append(wp.getId()).append(",")
                       .append(wp.getLocation()).append(",")
                       .append(wp.getWaterType()).append(",")
                       .append(wp.getStatus()).append(",")
                       .append(wp.getCapacity()).append(",")
                       .append(wp.getInstallationDate() != null ? 
                           new SimpleDateFormat("yyyy-MM-dd").format(wp.getInstallationDate()) : "N/A")
                       .append("\n");
                }
                break;
                
            case "Repair Records Report":
                csv.append("ID,Request ID,Team ID,Start Date,End Date,Status,Cost,Materials\n");
                for (RepairRecord rr : (List<RepairRecord>) data) {
                    csv.append(rr.getId()).append(",")
                       .append(rr.getRequestId()).append(",")
                       .append(rr.getTeamId()).append(",")
                       .append(rr.getStartDate() != null ? 
                           new SimpleDateFormat("yyyy-MM-dd").format(rr.getStartDate()) : "N/A").append(",")
                       .append(rr.getEndDate() != null ? 
                           new SimpleDateFormat("yyyy-MM-dd").format(rr.getEndDate()) : "Ongoing").append(",")
                       .append(rr.getRepairStatus()).append(",")
                       .append(rr.getCost()).append(",")
                       .append(rr.getMaterialsUsed() != null ? rr.getMaterialsUsed().replace(",", ";") : "N/A")
                       .append("\n");
                }
                break;
        }
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(csv.toString());
        }
    }
    
    private static void generateWaterPointsPDF(StringBuilder content, List<WaterPoint> waterPoints) {
        content.append("WATER POINTS SUMMARY REPORT\n");
        content.append("---------------------------\n\n");
        content.append(String.format("Total Water Points: %d\n\n", waterPoints.size()));
        
        int operational = 0, underRepair = 0, broken = 0;
        
        content.append(String.format("%-5s %-25s %-15s %-15s %-10s\n", 
            "ID", "Location", "Type", "Status", "Capacity"));
        content.append("---------------------------------------------------------------------\n");
        
        for (WaterPoint wp : waterPoints) {
            content.append(String.format("%-5d %-25s %-15s %-15s %-10d\n",
                wp.getId(), 
                wp.getLocation().length() > 25 ? wp.getLocation().substring(0, 22) + "..." : wp.getLocation(),
                wp.getWaterType(), wp.getStatus(), wp.getCapacity()));
                
            switch (wp.getStatus()) {
                case "Operational": operational++; break;
                case "Under Repair": underRepair++; break;
                case "Broken": broken++; break;
            }
        }
        
        content.append("\n\nSTATUS SUMMARY:\n");
        content.append("---------------\n");
        content.append(String.format("Operational: %d (%.1f%%)\n", operational, 
            waterPoints.size() > 0 ? (operational * 100.0 / waterPoints.size()) : 0));
        content.append(String.format("Under Repair: %d (%.1f%%)\n", underRepair,
            waterPoints.size() > 0 ? (underRepair * 100.0 / waterPoints.size()) : 0));
        content.append(String.format("Broken: %d (%.1f%%)\n", broken,
            waterPoints.size() > 0 ? (broken * 100.0 / waterPoints.size()) : 0));
    }
    
    private static void generateRepairRecordsPDF(StringBuilder content, List<RepairRecord> repairs) {
        content.append("REPAIR RECORDS REPORT\n");
        content.append("---------------------\n\n");
        content.append(String.format("Total Repair Records: %d\n\n", repairs.size()));
        
        double totalCost = 0;
        int completed = 0, inProgress = 0, pending = 0;
        
        content.append(String.format("%-5s %-12s %-12s %-15s %-12s\n", 
            "ID", "Start Date", "End Date", "Status", "Cost (RWF)"));
        content.append("----------------------------------------------------------------------\n");
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (RepairRecord repair : repairs) {
            String startDate = repair.getStartDate() != null ? sdf.format(repair.getStartDate()) : "N/A";
            String endDate = repair.getEndDate() != null ? sdf.format(repair.getEndDate()) : "Ongoing";
                
            content.append(String.format("%-5d %-12s %-12s %-15s %-12.0f\n",
                repair.getId(), startDate, endDate, repair.getRepairStatus(), repair.getCost()));
                
            totalCost += repair.getCost();
            switch (repair.getRepairStatus()) {
                case "Completed": completed++; break;
                case "In Progress": inProgress++; break;
                case "Pending": pending++; break;
            }
        }
        
        content.append("\n\nREPAIR SUMMARY:\n");
        content.append("---------------\n");
        content.append(String.format("Completed: %d (%.1f%%)\n", completed,
            repairs.size() > 0 ? (completed * 100.0 / repairs.size()) : 0));
        content.append(String.format("In Progress: %d\n", inProgress));
        content.append(String.format("Pending: %d\n", pending));
        content.append(String.format("Total Cost: %.0f RWF\n", totalCost));
        content.append(String.format("Average Cost per Repair: %.0f RWF\n", 
            completed > 0 ? (totalCost / completed) : 0));
    }
    
    private static void generateMaintenanceRequestsPDF(StringBuilder content, List<MaintenanceRequest> requests) {
        content.append("MAINTENANCE REQUESTS REPORT\n");
        content.append("---------------------------\n\n");
        content.append(String.format("Total Maintenance Requests: %d\n\n", requests.size()));
        
        content.append(String.format("%-5s %-30s %-12s %-15s %-15s\n", 
            "ID", "Description", "Priority", "Status", "Reporter"));
        content.append("--------------------------------------------------------------------------------\n");
        
        for (MaintenanceRequest req : requests) {
            content.append(String.format("%-5d %-30s %-12s %-15s %-15s\n",
                req.getId(), 
                req.getDescription().length() > 30 ? req.getDescription().substring(0, 27) + "..." : req.getDescription(),
                req.getPriority(), req.getStatus(), req.getReporterName()));
        }
    }
}