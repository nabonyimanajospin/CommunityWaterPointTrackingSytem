package TestConnection;

import dao.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import model.User;

public class SystemTest {
    public static void main(String[] args) {
        System.out.println("🧪 Testing Water Point System Components...\n");
        
        // Test 1: Hibernate Connection
        try {
            System.out.println("1. Testing Hibernate Connection...");
            Session session = HibernateUtil.getSessionFactory().openSession();
            System.out.println("   ✅ Hibernate connection successful!");
            session.close();
        } catch (Exception e) {
            System.out.println("   ❌ Hibernate connection failed: " + e.getMessage());
            return;
        }
        
        // Test 2: Database Operations
        try {
            System.out.println("\n2. Testing Database Operations...");
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            
            // Test user creation
            User testUser = new User("testuser", "password123", "test@example.com");
            testUser.setRole("USER");
            session.save(testUser);
            
            tx.commit();
            session.close();
            System.out.println("   ✅ Database operations successful!");
        } catch (Exception e) {
            System.out.println("   ❌ Database operations failed: " + e.getMessage());
        }
        
        // Test 3: Port Availability
        try {
            System.out.println("\n3. Testing RMI Port 3500...");
            java.net.ServerSocket socket = new java.net.ServerSocket(3500);
            socket.close();
            System.out.println("   ✅ Port 3500 is available!");
        } catch (Exception e) {
            System.out.println("   ⚠️ Port 3500 may be in use: " + e.getMessage());
        }
        
        System.out.println("\n🎯 System Test Complete!");
        System.out.println("💡 If all tests passed, you can start the server.");
    }
}