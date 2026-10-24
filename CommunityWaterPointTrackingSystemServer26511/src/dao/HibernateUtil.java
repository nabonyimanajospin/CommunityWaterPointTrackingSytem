/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import model.*;
/**
 *
 * @author Jospin Nabonyimana
 */

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();
    
    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            
            // Database configuration for PostgreSQL
            configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/water_point_system_db_26511");
            configuration.setProperty("hibernate.connection.username", "postgres");
            configuration.setProperty("hibernate.connection.password", "jospin123");
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            
            // Hibernate settings
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");
            configuration.setProperty("hibernate.use_sql_comments", "true");
            
            // Connection pooling with C3P0
            configuration.setProperty("hibernate.c3p0.min_size", "5");
            configuration.setProperty("hibernate.c3p0.max_size", "20");
            configuration.setProperty("hibernate.c3p0.timeout", "300");
            configuration.setProperty("hibernate.c3p0.max_statements", "50");
            configuration.setProperty("hibernate.c3p0.idle_test_period", "3000");
            
            // Add entity classes
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(UserProfile.class);
            configuration.addAnnotatedClass(UserSession.class);
            configuration.addAnnotatedClass(OTPRecord.class);
            configuration.addAnnotatedClass(WaterPoint.class);
            configuration.addAnnotatedClass(MaintenanceRequest.class);
            configuration.addAnnotatedClass(MaintenanceTeam.class);
            configuration.addAnnotatedClass(RepairRecord.class);
            configuration.addAnnotatedClass(Notification.class);
            
            return configuration.buildSessionFactory();
            
        } catch (Throwable ex) {
            System.err.println("❌ Failed to create SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void shutdown() {
        getSessionFactory().close();
    }
}
