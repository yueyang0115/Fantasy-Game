package edu.duke.ece.fantasy.Item;

import edu.duke.ece.fantasy.database.Unit;

public class Sword extends Equipment {
    public Sword() {
        super("sword", 20, 20, 20, 10);
    }



//    protected String meetRequirement(Unit unit) {
//        if(unit.getWeapon() == null){
//            return true;
//        } else {
//            throw new InvalidItemUsageException("Unit already has weapon, please unload weapon first");
//        }
//    }
}
