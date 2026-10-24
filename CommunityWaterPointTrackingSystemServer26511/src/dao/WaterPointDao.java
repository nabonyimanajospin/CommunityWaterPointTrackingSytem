/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import model.*;
import org.hibernate.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
/**
 *
 * @author Jospin Nabonyimana
 */

public class WaterPointDao {
    private SessionFactory sessionFactory;
    
    public WaterPointDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }
    
    public WaterPoint createWaterPoint(WaterPoint waterPoint) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            
            // Validate water point data
            if (!validateWaterPointData(waterPoint)) {
                throw new RuntimeException("Validation failed:\n" + getValidationErrors(waterPoint));
            }
            
            // Check location uniqueness
            if (!isLocationUnique(waterPoint.getLocation())) {
                throw new RuntimeException("Location already exists. Please choose a different location.");
            }
            
            session.save(waterPoint);
            transaction.commit();
            return waterPoint;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error creating water point: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
    
    public WaterPoint updateWaterPoint(WaterPoint waterPoint) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(waterPoint);
            transaction.commit();
            return waterPoint;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error updating water point: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
    
    public boolean deleteWaterPoint(int waterPointId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            
            WaterPoint waterPoint = session.get(WaterPoint.class, waterPointId);
            if (waterPoint != null) {
                Query<Long> requestQuery = session.createQuery(
                    "SELECT COUNT(*) FROM MaintenanceRequest mr WHERE mr.waterPointId = :wpId " +
                    "AND mr.status != 'Completed'", Long.class);
                requestQuery.setParameter("wpId", waterPointId);
                Long activeRequests = requestQuery.uniqueResult();
                
                if (activeRequests > 0) {
                    throw new RuntimeException("Cannot delete water point with active maintenance requests");
                }
                
                session.delete(waterPoint);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error deleting water point: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
    
    public WaterPoint getWaterPointById(int waterPointId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(WaterPoint.class, waterPointId);
        } catch (Exception e) {
            throw new RuntimeException("Error getting water point: " + e.getMessage(), e);
        }
    }
    
    public List<WaterPoint> getAllWaterPoints() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "FROM WaterPoint ORDER BY " +
                "CASE status " +
                "WHEN 'Operational' THEN 1 " +
                "WHEN 'Under Repair' THEN 2 " +
                "WHEN 'Broken' THEN 3 " +
                "ELSE 4 END, location", 
                WaterPoint.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error getting water points: " + e.getMessage(), e);
        }
    }
    
    public List<WaterPoint> searchWaterPoints(String location) {
        try (Session session = sessionFactory.openSession()) {
            Query<WaterPoint> query = session.createQuery(
                "FROM WaterPoint WHERE LOWER(location) LIKE LOWER(:location) " +
                "ORDER BY location", WaterPoint.class);
            query.setParameter("location", "%" + location + "%");
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error searching water points: " + e.getMessage(), e);
        }
    }
    
    public boolean updateWaterPointStatus(int waterPointId, String status) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            
            WaterPoint waterPoint = session.get(WaterPoint.class, waterPointId);
            if (waterPoint != null) {
                waterPoint.setStatus(status);
                session.update(waterPoint);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error updating water point status: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
    
    public boolean assignTeamToWaterPoint(int waterPointId, int teamId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            
            WaterPoint waterPoint = session.get(WaterPoint.class, waterPointId);
            MaintenanceTeam team = session.get(MaintenanceTeam.class, teamId);
            
            if (waterPoint != null && team != null) {
                waterPoint.getMaintenanceTeams().add(team);
                team.getWaterPoints().add(waterPoint);
                session.update(waterPoint);
                session.update(team);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error assigning team to water point: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
    
    public boolean removeTeamFromWaterPoint(int waterPointId, int teamId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            
            WaterPoint waterPoint = session.get(WaterPoint.class, waterPointId);
            MaintenanceTeam team = session.get(MaintenanceTeam.class, teamId);
            
            if (waterPoint != null && team != null) {
                waterPoint.getMaintenanceTeams().remove(team);
                team.getWaterPoints().remove(waterPoint);
                session.update(waterPoint);
                session.update(team);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error removing team from water point: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
    
    public List<MaintenanceTeam> getTeamsForWaterPoint(int waterPointId) {
        try (Session session = sessionFactory.openSession()) {
            Query<MaintenanceTeam> query = session.createQuery(
                "SELECT t FROM WaterPoint w JOIN w.maintenanceTeams t WHERE w.id = :wpId", 
                MaintenanceTeam.class);
            query.setParameter("wpId", waterPointId);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error getting teams for water point: " + e.getMessage(), e);
        }
    }
    
    public List<WaterPoint> getWaterPointsForTeam(int teamId) {
        try (Session session = sessionFactory.openSession()) {
            Query<WaterPoint> query = session.createQuery(
                "SELECT w FROM MaintenanceTeam t JOIN t.waterPoints w WHERE t.id = :teamId", 
                WaterPoint.class);
            query.setParameter("teamId", teamId);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error getting water points for team: " + e.getMessage(), e);
        }
    }
    
    public boolean validateCapacity(int capacity) {
        return capacity >= 100 && capacity <= 100000;
    }
    
    public boolean validateInstallationDate(Date date) {
        Date today = new Date();
        // Allow installation dates from past to future (more flexible)
        return date != null;
    }
    
    // Enhanced validation methods
    public boolean validateWaterPointData(WaterPoint waterPoint) {
        if (waterPoint == null) return false;
        if (waterPoint.getLocation() == null || waterPoint.getLocation().trim().isEmpty()) return false;
        if (waterPoint.getWaterType() == null || waterPoint.getWaterType().trim().isEmpty()) return false;
        if (waterPoint.getStatus() == null || waterPoint.getStatus().trim().isEmpty()) return false;
        if (!validateCapacity(waterPoint.getCapacity())) return false;
        if (!validateInstallationDate(waterPoint.getInstallationDate())) return false;
        return true;
    }
    
    public String getValidationErrors(WaterPoint waterPoint) {
        StringBuilder errors = new StringBuilder();
        if (waterPoint == null) {
            errors.append("Water point data is null.\n");
            return errors.toString();
        }
        
        if (waterPoint.getLocation() == null || waterPoint.getLocation().trim().isEmpty()) {
            errors.append("Location is required.\n");
        }
        if (waterPoint.getWaterType() == null || waterPoint.getWaterType().trim().isEmpty()) {
            errors.append("Water type is required.\n");
        }
        if (waterPoint.getStatus() == null || waterPoint.getStatus().trim().isEmpty()) {
            errors.append("Status is required.\n");
        }
        if (!validateCapacity(waterPoint.getCapacity())) {
            errors.append("Capacity must be between 100 and 100,000 liters.\n");
        }
        if (!validateInstallationDate(waterPoint.getInstallationDate())) {
            errors.append("Installation date is invalid.\n");
        }
        
        return errors.toString();
    }
    
    public boolean isLocationUnique(String location) {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM WaterPoint WHERE LOWER(location) = LOWER(:location)", 
                Long.class);
            query.setParameter("location", location);
            Long count = query.uniqueResult();
            return count == 0;
        } catch (Exception e) {
            throw new RuntimeException("Error checking location uniqueness: " + e.getMessage(), e);
        }
    }
    
    public boolean canDeleteWaterPoint(int waterPointId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM MaintenanceRequest mr WHERE mr.waterPointId = :wpId " +
                "AND mr.status != 'Completed'", Long.class);
            query.setParameter("wpId", waterPointId);
            Long count = query.uniqueResult();
            return count == 0;
        } catch (Exception e) {
            throw new RuntimeException("Error checking if water point can be deleted: " + e.getMessage(), e);
        }
    }
    
    public int getWaterPointCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM WaterPoint", Long.class);
            return query.uniqueResult().intValue();
        } catch (Exception e) {
            throw new RuntimeException("Error getting water point count: " + e.getMessage(), e);
        }
    }
    
    public int getWaterPointCountByStatus(String status) {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(*) FROM WaterPoint WHERE status = :status", Long.class);
            query.setParameter("status", status);
            Long count = query.uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            throw new RuntimeException("Error getting water point count by status: " + e.getMessage(), e);
        }
    }
    
    public Object[] getWaterPointAnalytics() {
        try (Session session = sessionFactory.openSession()) {
            Object[] analytics = new Object[5];
            
            Query<Long> totalQuery = session.createQuery("SELECT COUNT(*) FROM WaterPoint", Long.class);
            analytics[0] = totalQuery.uniqueResult();
            
            Query<Long> operationalQuery = session.createQuery(
                "SELECT COUNT(*) FROM WaterPoint WHERE status = 'Operational'", Long.class);
            analytics[1] = operationalQuery.uniqueResult();
            
            Query<Long> repairQuery = session.createQuery(
                "SELECT COUNT(*) FROM WaterPoint WHERE status = 'Under Repair'", Long.class);
            analytics[2] = repairQuery.uniqueResult();
            
            Query<Long> brokenQuery = session.createQuery(
                "SELECT COUNT(*) FROM WaterPoint WHERE status = 'Broken'", Long.class);
            analytics[3] = brokenQuery.uniqueResult();
            
            Query<Double> avgQuery = session.createQuery(
                "SELECT AVG(capacity) FROM WaterPoint WHERE capacity IS NOT NULL", Double.class);
            Double avgCapacity = avgQuery.uniqueResult();
            analytics[4] = avgCapacity != null ? String.format("%.1f liters", avgCapacity) : "0 liters";
            
            return analytics;
        } catch (Exception e) {
            throw new RuntimeException("Error getting water point analytics: " + e.getMessage(), e);
        }
    }
    
    public List<WaterPoint> getWaterPointsByStatus(String status) {
        try (Session session = sessionFactory.openSession()) {
            Query<WaterPoint> query = session.createQuery(
                "FROM WaterPoint WHERE status = :status ORDER BY location", WaterPoint.class);
            query.setParameter("status", status);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error getting water points by status: " + e.getMessage(), e);
        }
    }
    
    public List<WaterPoint> getWaterPointsByType(String waterType) {
        try (Session session = sessionFactory.openSession()) {
            Query<WaterPoint> query = session.createQuery(
                "FROM WaterPoint WHERE waterType = :waterType ORDER BY location", WaterPoint.class);
            query.setParameter("waterType", waterType);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error getting water points by type: " + e.getMessage(), e);
        }
    }
    
    public List<WaterPoint> getWaterPointsInstalledAfter(Date date) {
        try (Session session = sessionFactory.openSession()) {
            Query<WaterPoint> query = session.createQuery(
                "FROM WaterPoint WHERE installationDate >= :date ORDER BY installationDate DESC", 
                WaterPoint.class);
            query.setParameter("date", date);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error getting water points installed after date: " + e.getMessage(), e);
        }
    }
    
    public List<WaterPoint> getWaterPointsWithHighUsage() {
        try (Session session = sessionFactory.openSession()) {
            Query<WaterPoint> query = session.createQuery(
                "FROM WaterPoint wp WHERE (SELECT COUNT(*) FROM MaintenanceRequest mr " +
                "WHERE mr.waterPoint.id = wp.id) > 3 ORDER BY " +
                "(SELECT COUNT(*) FROM MaintenanceRequest mr WHERE mr.waterPoint.id = wp.id) DESC", 
                WaterPoint.class);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error getting high usage water points: " + e.getMessage(), e);
        }
    }
    
    public List<Object[]> getWaterPointStatusDistribution() {
        try (Session session = sessionFactory.openSession()) {
            Query<Object[]> query = session.createQuery(
                "SELECT status, COUNT(*) as count FROM WaterPoint " +
                "GROUP BY status ORDER BY status", Object[].class);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error getting water point status distribution: " + e.getMessage(), e);
        }
    }
    
    public List<WaterPoint> getWaterPointsNeedingMaintenance() {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        transaction = session.beginTransaction();
        
        // Find water points with status "Needs Maintenance" or "Critical"
        String hql = "FROM WaterPoint w WHERE w.status IN ('Needs Maintenance', 'Critical', 'Requires Repair')";
        Query<WaterPoint> query = session.createQuery(hql, WaterPoint.class);
        
        List<WaterPoint> waterPoints = query.getResultList();
        transaction.commit();
        return waterPoints;
        
    } catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();
        }
        e.printStackTrace();
        return new ArrayList<>();
    }
    }
    
    public boolean updateWaterPointCoordinates(int waterPointId, Double latitude, Double longitude) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        transaction = session.beginTransaction();
        
        WaterPoint waterPoint = session.get(WaterPoint.class, waterPointId);
        if (waterPoint != null) {
            waterPoint.setLatitude(latitude);
            waterPoint.setLongitude(longitude);
            session.update(waterPoint);
            transaction.commit();
            return true;
        }
        return false;
        
    } catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();
        }
        e.printStackTrace();
        return false;
    }
    }
    
    public double getAverageWaterPointAge() {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        transaction = session.beginTransaction();
        
        // Calculate average age in days: (current_date - installation_date)
        String hql = "SELECT AVG(DATEDIFF(CURRENT_DATE, w.installationDate)) FROM WaterPoint w WHERE w.installationDate IS NOT NULL";
        Query<Double> query = session.createQuery(hql, Double.class);
        
        Double averageAge = query.uniqueResult();
        transaction.commit();
        
        return averageAge != null ? averageAge : 0.0;
        
    } catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();
        }
        e.printStackTrace();
        return 0.0;
    }
    }
    
    
    public boolean updateLastMaintenanceDate(int waterPointId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            
            WaterPoint waterPoint = session.get(WaterPoint.class, waterPointId);
            if (waterPoint != null) {
                waterPoint.setLastMaintenanceDate(new Date());
                session.update(waterPoint);
                transaction.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error updating last maintenance date: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
}