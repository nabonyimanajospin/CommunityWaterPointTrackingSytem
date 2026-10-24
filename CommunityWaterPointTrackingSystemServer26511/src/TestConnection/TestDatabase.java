/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestConnection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
/**
 *
 * @author Jospin Nabonyimana
 */

// Create TestDatabase.java

public class TestDatabase {
    public static void main(String[] args) {
        try {
            System.out.println("Testing Hibernate Database Connection...");
            
            // Load configuration
            Configuration config = new Configuration().configure("hibernate.cfg.xml");
            SessionFactory sessionFactory = config.buildSessionFactory();
            Session session = sessionFactory.openSession();
            
            System.out.println("✅ Database connection SUCCESSFUL!");
            System.out.println("✅ PostgreSQL connected to: water_point_system_db_26511");
            System.out.println("✅ Hibernate session created");
            
            session.close();
            sessionFactory.close();
            
        } catch (Exception e) {
            System.err.println("❌ Database connection FAILED!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}