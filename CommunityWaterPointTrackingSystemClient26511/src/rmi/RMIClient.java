package rmi;

import service.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 3500;
    
    private static Registry registry;
    
    // Service instances
    private static UserService userService;
    private static WaterPointService waterPointService;
    private static MaintenanceRequestService maintenanceRequestService;
    private static MaintenanceTeamService maintenanceTeamService;
    private static RepairRecordService repairRecordService;
    private static NotificationService notificationService;
    private static ReportService reportService;
    
    public static void connect() throws Exception {
        registry = LocateRegistry.getRegistry(SERVER_HOST, SERVER_PORT);
        
        userService = (UserService) registry.lookup("UserService");
        waterPointService = (WaterPointService) registry.lookup("WaterPointService");
        maintenanceRequestService = (MaintenanceRequestService) registry.lookup("MaintenanceRequestService");
        maintenanceTeamService = (MaintenanceTeamService) registry.lookup("MaintenanceTeamService");
        repairRecordService = (RepairRecordService) registry.lookup("RepairRecordService");
        notificationService = (NotificationService) registry.lookup("NotificationService");
        reportService = (ReportService) registry.lookup("ReportService");
    }
    
    public static UserService getUserService() { return userService; }
    public static WaterPointService getWaterPointService() { return waterPointService; }
    public static MaintenanceRequestService getMaintenanceRequestService() { return maintenanceRequestService; }
    public static MaintenanceTeamService getMaintenanceTeamService() { return maintenanceTeamService; }
    public static RepairRecordService getRepairRecordService() { return repairRecordService; }
    public static NotificationService getNotificationService() { return notificationService; }
    public static ReportService getReportService() { return reportService; }
    
    public static service.OTPService getOTPService() throws Exception {
        return (service.OTPService) registry.lookup("OTPService");
    }
}