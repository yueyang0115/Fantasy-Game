package edu.duke.ece.fantasy.database;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Consumable")
public class Consumable extends Item {
    int hp;

    public Consumable() {
    }

    public Consumable(String name, int cost, int hp) {
        super(name, cost);
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
