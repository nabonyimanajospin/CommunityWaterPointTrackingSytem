/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import model.MaintenanceRequest;
/**
 *
 * @author Jospin Nabonyimana
 */

public interface MaintenanceRequestService extends Remote {
    MaintenanceRequest createMaintenanceRequest(MaintenanceRequest request) throws RemoteException;
    MaintenanceRequest updateMaintenanceRequest(MaintenanceRequest request) throws RemoteException;
    boolean deleteMaintenanceRequest(int requestId) throws RemoteException;
    MaintenanceRequest getMaintenanceRequestById(int requestId) throws RemoteException;
    List<MaintenanceRequest> getAllMaintenanceRequests() throws RemoteException;
    
    boolean updateRequestStatus(int requestId, String status) throws RemoteException;
    boolean hasDuplicateRequest(int waterPointId, String issueType) throws RemoteException;
    List<MaintenanceRequest> getRequestsByPriority(String priority) throws RemoteException;
    List<MaintenanceRequest> getPendingRequests() throws RemoteException;
    int[] getRequestStats() throws RemoteException;
    
    List<MaintenanceRequest> getRequestsByWaterPoint(int waterPointId) throws RemoteException;
    List<MaintenanceRequest> getRequestsByReporter(int reporterId) throws RemoteException;
    List<MaintenanceRequest> getRequestsByDateRange(Date startDate, Date endDate) throws RemoteException;
    
    int assignTeamToRequest(int requestId, int teamId) throws RemoteException;
    Object[] getRequestAnalytics() throws RemoteException;
}
