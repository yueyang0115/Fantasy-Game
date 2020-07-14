package edu.duke.ece.fantasy.Item;

import edu.duke.ece.fantasy.database.Unit;

public class Sword extends Equipment {
    public Sword() {
        super("sword", 20, 20, 20, 10);
    }

    @Override
    public void OnDeEquip() {
        super.OnDeEquip();
    }


    protected boolean meetRequirement(Unit unit) throws InvalidItemUsageException {
        if(unit.getWeapon() == null){
            return true;
        } else {
            throw new InvalidItemUsageException("Unit already has weapon, please unload weapon first");
        }
    }
}
