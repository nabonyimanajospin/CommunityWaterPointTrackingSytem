/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
/**
 *
 * @author Jospin Nabonyimana
 */

@Entity
@Table(name = "maintenance_teams")
public class MaintenanceTeam implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "team_name", unique = true, nullable = false, length = 100)
    private String teamName;
    
    @Column(name = "specialization", length = 100)
    private String specialization;
    
    @Column(name = "contact_person", length = 100)
    private String contactPerson;
    
    @Column(name = "phone", length = 15)
    private String phone;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "status", length = 50)
    private String status = "Available";
    
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RepairRecord> repairRecords = new HashSet<>();
    
    @ManyToMany(mappedBy = "maintenanceTeams", fetch = FetchType.LAZY)
    private Set<WaterPoint> waterPoints = new HashSet<>();
    
    public MaintenanceTeam() {}
    
    public MaintenanceTeam(String teamName, String specialization, String contactPerson, String phone, String email) {
        this.teamName = teamName;
        this.specialization = specialization;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.email = email;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Set<RepairRecord> getRepairRecords() { return repairRecords; }
    public void setRepairRecords(Set<RepairRecord> repairRecords) { this.repairRecords = repairRecords; }
    
    public Set<WaterPoint> getWaterPoints() { return waterPoints; }
    public void setWaterPoints(Set<WaterPoint> waterPoints) { this.waterPoints = waterPoints; }
}
