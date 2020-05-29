package edu.duke.ece.fantasy.database;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;
import org.jasypt.hibernate5.type.EncryptedStringType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Table( name = "Player" )
public class Player {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "ID", unique = true, nullable = false)
    private int id;


    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;


    @Column(name = "WID",columnDefinition="serial", unique = true,insertable = false, updatable = false,nullable = false)
    private int wid;

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Player() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }
}
