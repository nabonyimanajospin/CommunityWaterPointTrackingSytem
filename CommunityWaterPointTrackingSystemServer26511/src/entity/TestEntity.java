/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author Jospin Nabonyimana
 */

import javax.persistence.*;

@Entity
@Table(name = "test_entity")
public class TestEntity {
    @Id
    private int id;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}