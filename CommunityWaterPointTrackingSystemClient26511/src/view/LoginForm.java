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
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class LoginForm extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginBtn;

    public LoginForm() {
        initComponents();
        setupUI();
    }

    private void initComponents() {
        setTitle("Water Maintenance System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        
        // This is for the main panel with picture background I have downloaded
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load your background image here
                try {
                    ImageIcon imageIcon = new ImageIcon("C:\\Users\\Jospin Nabonyimana\\OneDrive\\Desktop\\1\\waterLoginBg.jpg");
                    Image image = imageIcon.getImage();
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    g.setColor(new Color(70, 130, 180));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        
        // FLEXIBLE BORDER with scales with window size
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Center panel to hold login form - FLEXIBLE
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        
        // Login form panel with a flexible size
        JPanel loginPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
                
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                // My scale has to be FLEXIBLE: Scale with parent container but keep minimum size
                Container parent = getParent();
                if (parent != null) {
                    int parentWidth = parent.getWidth();
                    int parentHeight = parent.getHeight();
                    
                    // Let me Calculate proportional size (60% of parent, but with min/max limits)
                    int width = Math.max(350, Math.min(600, (int)(parentWidth * 0.6)));
                    int height = Math.max(400, Math.min(500, (int)(parentHeight * 0.7)));
                    
                    return new Dimension(width, height);
                }
                return new Dimension(400, 450); // Default size
            }
        };
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0; // IMPORTANT: Allow horizontal expansion
        
        // Title - FLEXIBLE FONT SIZE
        JLabel titleLabel = new JLabel("WATER MAINTENANCE SYSTEM", JLabel.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(70, 130, 180, 200));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2d.setColor(Color.WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                return new Dimension(dim.width + 40, dim.height + 15);
            }
        };        

        
        // FLEXIBLE FONT - scales with window size
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        loginPanel.add(titleLabel, gbc);
        
        // Email field - FLEXIBLE WIDTH
        emailField = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    super.paintComponent(g);
                    g2.dispose();
                } else {
                    super.paintComponent(g);
                }
            }
            
            @Override
            public Dimension getPreferredSize() {
                // FLEXIBLE: Scale with parent width
                Container parent = getParent();
                if (parent != null) {
                    int fieldWidth = Math.max(250, parent.getWidth() - 100); // Dynamic width
                    return new Dimension(fieldWidth, 40);
                }
                return new Dimension(300, 40);
            }
            
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize(); // Allow expansion
            }
        };
        emailField.setOpaque(false);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        //emailField.setText("admin@water.com");
        emailField.setText("");
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        emailLabel.setForeground(Color.BLACK);
        loginPanel.add(emailLabel, gbc);
        loginPanel.add(emailField, gbc);
        
        // Password field - FLEXIBLE WIDTH
        passwordField = new JPasswordField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    super.paintComponent(g);
                    g2.dispose();
                } else {
                    super.paintComponent(g);
                }
            }
            
            @Override
            public Dimension getPreferredSize() {
                // FLEXIBLE: Scale with parent width
                Container parent = getParent();
                if (parent != null) {
                    int fieldWidth = Math.max(250, parent.getWidth() - 100); // Dynamic width
                    return new Dimension(fieldWidth, 40);
                }
                return new Dimension(300, 40);
            }
            
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize(); // Allow expansion
            }
        };
        passwordField.setOpaque(false);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        //passwordField.setText("admin123");
        passwordField.setText("");
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passwordLabel.setForeground(Color.BLACK);
        loginPanel.add(passwordLabel, gbc);
        loginPanel.add(passwordField, gbc);
        
        // Login button - FLEXIBLE WIDTH
        loginBtn = new JButton("LOGIN") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(50, 100, 150));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(90, 150, 210));
                } else {
                    g2d.setColor(new Color(70, 130, 180));
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2d.setColor(getForeground());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                // FLEXIBLE: Scale with parent width
                Container parent = getParent();
                if (parent != null) {
                    int buttonWidth = Math.max(250, parent.getWidth() - 100); // Dynamic width
                    return new Dimension(buttonWidth, 45);
                }
                return new Dimension(300, 45);
            }
            
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize(); // Allow expansion
            }
        };
        loginBtn.setContentAreaFilled(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setForeground(Color.WHITE);
        
        gbc.insets = new Insets(20, 10, 10, 10);
        loginPanel.add(loginBtn, gbc);
        
        // Footer
        JLabel footerLabel = new JLabel("Community Water Point Maintenance Tracker", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        footerLabel.setForeground(Color.BLACK);
        gbc.insets = new Insets(20, 10, 0, 10);
        loginPanel.add(footerLabel, gbc);
        
        centerPanel.add(loginPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        
        // Set initial size
        setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(null);
        
        // PROPER RESIZE HANDLING
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
    }
    
    private void formComponentResized(java.awt.event.ComponentEvent evt) {
        // Force re-layout and repaint when resized
        revalidate();
        repaint();
    }

    private void setupUI() {
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                loginActionPerformed(evt);
            }
        });
        
        passwordField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                loginActionPerformed(evt);
            }
        });
    }

    private void loginActionPerformed(ActionEvent evt) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Connect to RMI server
            rmi.RMIClient.connect();
            
            // Check if admin login (full access)
            if ("admin@water.com".equals(email) && "admin123".equals(password)) {
                JOptionPane.showMessageDialog(this, "Admin login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
                this.dispose();
            }
            // Check if regular user login (limited access)
            else if ("user@water.com".equals(email) && "user123".equals(password)) {
                JOptionPane.showMessageDialog(this, "User login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                UserDashboard userDashboard = new UserDashboard();
                userDashboard.setVisible(true);
                this.dispose();
            }
            else {
                // All other emails need OTP verification (admin access)
                showOTPForm();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Server connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void showOTPForm() {
        final JDialog otpDialog = new JDialog(this, "OTP Verification", true);
        final String[] storedOTP = new String[1];
        final String[] storedEmail = new String[1];
        otpDialog.setSize(500, 400);
        otpDialog.setLocationRelativeTo(this);
        otpDialog.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("📧 OTP Verification Required");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel emailLabel = new JLabel("Email for OTP:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 13));
        mainPanel.add(emailLabel, gbc);
        
        JTextField otpEmailField = new JTextField(25);
        otpEmailField.setPreferredSize(new Dimension(280, 35));
        otpEmailField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(otpEmailField, gbc);
        
        JButton sendOTPBtn = new JButton("Send OTP");
        sendOTPBtn.setBackground(Color.WHITE);
        sendOTPBtn.setForeground(new Color(70, 130, 180));
        sendOTPBtn.setFont(new Font("Arial", Font.BOLD, 14));
        sendOTPBtn.setPreferredSize(new Dimension(300, 40));
        sendOTPBtn.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        mainPanel.add(sendOTPBtn, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel otpLabel = new JLabel("Enter OTP Code:");
        otpLabel.setFont(new Font("Arial", Font.BOLD, 13));
        mainPanel.add(otpLabel, gbc);
        
        JTextField otpCodeField = new JTextField(25);
        otpCodeField.setPreferredSize(new Dimension(280, 35));
        otpCodeField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        mainPanel.add(otpCodeField, gbc);
        
        JButton verifyBtn = new JButton("Verify and Login");
        verifyBtn.setBackground(Color.WHITE);
        verifyBtn.setForeground(new Color(34, 139, 34));
        verifyBtn.setFont(new Font("Arial", Font.BOLD, 14));
        verifyBtn.setPreferredSize(new Dimension(300, 40));
        verifyBtn.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        mainPanel.add(verifyBtn, gbc);
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setForeground(new Color(220, 20, 60));
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 14));
        cancelBtn.setPreferredSize(new Dimension(300, 40));
        cancelBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 20, 60), 2));
        gbc.gridy = 5;
        mainPanel.add(cancelBtn, gbc);
        
        // Send OTP button action - use RMI OTP service
        sendOTPBtn.addActionListener(e -> {
            String otpEmail = otpEmailField.getText().trim();
            if (otpEmail.isEmpty() || !otpEmail.contains("@")) {
                JOptionPane.showMessageDialog(otpDialog, "Please enter a valid email address!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // Use RMI OTP service
                service.OTPService otpService = rmi.RMIClient.getOTPService();
                String otp = otpService.generateOTP();
                boolean sent = otpService.sendOTP(otpEmail, otp);
                
                if (sent) {
                    // Create notification for OTP sent
                    try {
                        model.Notification notification = new model.Notification();
                        notification.setUserId(1);
                        notification.setType("OTP");
                        notification.setTitle("OTP Sent");
                        notification.setMessage("OTP code has been sent to " + otpEmail + " for login verification.");
                        notification.setRecipient(otpEmail);
                        rmi.RMIClient.getNotificationService().createNotification(notification);
                    } catch (Exception ex) {
                        // Notification failed but don't stop the main operation
                    }
                    
                    // Store OTP for verification
                    storedOTP[0] = otp;
                    storedEmail[0] = otpEmail;
                    
                    JOptionPane.showMessageDialog(otpDialog, "OTP sent to: " + otpEmail + "\nPlease check your email inbox!", "OTP Sent", JOptionPane.INFORMATION_MESSAGE);
                    otpCodeField.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(otpDialog, "Failed to send OTP to " + otpEmail + ". Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(otpDialog, "Error sending OTP: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Verify button action
        verifyBtn.addActionListener(e -> {
            String otpEmail = otpEmailField.getText().trim();
            String otpCode = otpCodeField.getText().trim();
            
            if (otpEmail.isEmpty() || otpCode.isEmpty()) {
                JOptionPane.showMessageDialog(otpDialog, "Please fill in both email and OTP fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Verify real OTP code from email
            String correctOTP = storedOTP[0];
            String correctEmail = storedEmail[0];
            
            if (correctOTP == null) {
                JOptionPane.showMessageDialog(otpDialog, "Please click 'Send OTP' first!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!otpEmail.equals(correctEmail)) {
                JOptionPane.showMessageDialog(otpDialog, "Email doesn't match! Please use: " + correctEmail, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (otpCode.equals(correctOTP)) {
                JOptionPane.showMessageDialog(otpDialog, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                otpDialog.dispose();
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(otpDialog, "Invalid OTP! Please check your email and enter the correct code.", "Verification Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Cancel button action
        cancelBtn.addActionListener(e -> otpDialog.dispose());
        
        otpDialog.add(mainPanel, BorderLayout.CENTER);
        otpDialog.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }
}