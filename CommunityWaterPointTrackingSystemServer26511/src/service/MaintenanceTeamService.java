/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.MaintenanceTeam;
/**
 *
 * @author Jospin Nabonyimana
 */

public interface MaintenanceTeamService extends Remote {
    MaintenanceTeam createMaintenanceTeam(MaintenanceTeam team) throws RemoteException;
    MaintenanceTeam updateMaintenanceTeam(MaintenanceTeam team) throws RemoteException;
    boolean deleteMaintenanceTeam(int teamId) throws RemoteException;
    MaintenanceTeam getMaintenanceTeamById(int teamId) throws RemoteException;
    List<MaintenanceTeam> getAllMaintenanceTeams() throws RemoteException;
    List<MaintenanceTeam> getAvailableTeams() throws RemoteException;
    List<MaintenanceTeam> searchTeams(String teamName) throws RemoteException;
    
    boolean updateTeamStatus(int teamId, String status) throws RemoteException;
    int[] getTeamStats() throws RemoteException;
    boolean isTeamNameUnique(String teamName) throws RemoteException;
    
    List<MaintenanceTeam> getTeamsBySpecialization(String specialization) throws RemoteException;
    int getTeamCount() throws RemoteException;
    List<MaintenanceTeam> getTeamsWithActiveRepairs() throws RemoteException;
}