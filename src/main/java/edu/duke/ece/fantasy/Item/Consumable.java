package edu.duke.ece.fantasy.Item;

import edu.duke.ece.fantasy.database.Unit;

import javax.persistence.Entity;
import javax.persistence.Table;


public class Consumable extends Item {
    int hp;

    public Consumable() {
    }

    public Consumable(String name, int cost, int hp) {
        super(name, cost);
        this.hp = hp;
    }

    @Override
    public void OnUse(Unit unit) {
        unit.addHp(hp);
    }
}
