package edu.duke.ece.fantasy.Item;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.DBItem;
import edu.duke.ece.fantasy.database.Unit;

import javax.persistence.Entity;
import javax.persistence.Table;


public class Consumable extends Item {
    int hp;

    public Consumable() {
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }


    public Consumable(String name, int cost, int hp) {
        super(name, cost);
        this.hp = hp;
    }

    @Override
    public void OnUse(Unit unit) {
        unit.addHp(hp);
        System.out.println("I'm consumable");
    }
}
