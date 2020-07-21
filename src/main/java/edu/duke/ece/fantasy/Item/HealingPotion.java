package edu.duke.ece.fantasy.Item;

import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.Unit;


public class HealingPotion extends Item {
    int healingHp;

    public HealingPotion() {
    }

    public int getHealingHp() {
        return healingHp;
    }

    public void setHealingHp(int healingHp) {
        this.healingHp = healingHp;
    }


    public HealingPotion(String name, int cost, int healingHp) {
        super(name, cost);
        this.healingHp = healingHp;
    }

    @Override
    public void OnUse(Unit unit, Player player) {
        unit.addHp(healingHp);
    }
}
