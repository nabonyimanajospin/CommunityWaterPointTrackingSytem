/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Notification;
/**
 *
 * @author Jospin Nabonyimana
 */

public interface NotificationService extends Remote {
    Notification createNotification(Notification notification) throws RemoteException;
    Notification updateNotification(Notification notification) throws RemoteException;
    boolean deleteNotification(int notificationId) throws RemoteException;
    Notification getNotificationById(int notificationId) throws RemoteException;
    
    List<Notification> getNotificationsByUser(int userId) throws RemoteException;
    List<Notification> getUnreadNotifications(int userId) throws RemoteException;
    boolean markAsRead(int notificationId) throws RemoteException;
    boolean markAllAsRead(int userId) throws RemoteException;
    int getUnreadCount(int userId) throws RemoteException;
    
    List<Notification> getNotificationsByType(int userId, String notificationType) throws RemoteException;
    List<Notification> getRecentNotifications(int userId, int limit) throws RemoteException;
    
    boolean createMaintenanceNotification(int userId, String title, String message, String actionUrl) throws RemoteException;
    boolean createRepairNotification(int userId, String title, String message, String actionUrl) throws RemoteException;
    boolean createSystemNotification(int userId, String title, String message) throws RemoteException;
    
    boolean cleanupOldNotifications(int days) throws RemoteException;
    
    // Additional methods for notifications panel
    List<Notification> getAllNotifications() throws RemoteException;
    boolean clearAllNotifications() throws RemoteException;
}