package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Unit")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Unit{
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "ID", unique = true, nullable = false)
    private int id;

    @Column(name = "unit_type", unique = false, nullable = false, length = 100)
    private String u_type;

    @Column(name = "HP", unique = false, nullable = false)
    private int hp;

    @Column(name = "ATK", unique = false, nullable = false)
    private int atk;

    @Column(name = "speed", unique = false, nullable = false)
    private int speed;

    @JsonManagedReference
    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL)
    private List<Equipment> equipment = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() { return u_type; }

    public void setType(String type) { this.u_type = type; }

    public int getSpeed() { return speed; }

    public void setSpeed(int speed) { this.speed = speed; }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    }
