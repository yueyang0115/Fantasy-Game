package edu.duke.ece.fantasy.Item;

import edu.duke.ece.fantasy.database.DBItem;
import edu.duke.ece.fantasy.database.Unit;

import javax.persistence.*;

public class Equipment extends Item {
    int hp;
    int atk;
    int speed;

    public Equipment() {
    }

    public Equipment(String name, int cost, int hp, int atk, int speed) {
        super(name, cost);
        this.hp = hp;
        this.atk = atk;
        this.speed = speed;
    }

    public void OnEquip(Unit unit){

    }

    public void OnDeEquip(){

    }

//    @Override
//    public DBItem toDBItem() {
//        return null;
//    }

    @Override
    public void OnUse(Unit unit) {
        OnEquip(unit);
    }

    //    @Override
//    public void useItem(Unit unit) {
//        ItemPack new_equiment = new ItemPack(this, 1);
//        unit.addEquipment(new_equiment);
//    }
}
