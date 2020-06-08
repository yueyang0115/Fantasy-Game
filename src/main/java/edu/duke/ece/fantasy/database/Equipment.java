package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "Equipment")
public class Equipment extends Item {

    @Column(name = "hp")
    int hp;

    @Column(name = "atk")
    int atk;

    @Column(name = "speed")
    int speed;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;

    public Equipment() {
    }

    public Equipment(String name, int cost, int hp, int atk, int speed) {
        super(name, cost);
        this.hp = hp;
        this.atk = atk;
        this.speed = speed;
    }

    @Override
    public void useItem(Unit unit) {
        unit.addEquipment(this);
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
