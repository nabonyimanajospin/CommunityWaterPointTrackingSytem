/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
/**
 *
 * @author Jospin Nabonyimana
 */
@Entity
@Table(name = "otp_records")
public class OTPRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "otp_code", nullable = false, length = 6)
    private String otpCode;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "phone", length = 15)
    private String phone;
    
    @Column(name = "purpose", length = 50)
    private String purpose;
    
    @Column(name = "is_used")
    private boolean isUsed = false;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt = new Date();
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires_at", nullable = false)
    private Date expiresAt;
    
    @Column(name = "user_id", nullable = false)
    private int userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    public OTPRecord() {}
    
    public OTPRecord(User user, String otpCode, String email, String purpose) {
        this.user = user;
        this.userId = user.getId();
        this.otpCode = otpCode;
        this.email = email;
        this.purpose = purpose;
        this.expiresAt = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    
    public boolean isUsed() { return isUsed; }
    public void setUsed(boolean used) { isUsed = used; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Date expiresAt) { this.expiresAt = expiresAt; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public User getUser() { return user; }
    public void setUser(User user) { 
        this.user = user;
        if (user != null) {
            this.userId = user.getId();
        }
    }
    
    public boolean isValid() {
        return !isUsed && new Date().before(expiresAt);
    }
}

