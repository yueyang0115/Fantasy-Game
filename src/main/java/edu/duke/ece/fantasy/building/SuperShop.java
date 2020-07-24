package edu.duke.ece.fantasy.building;

import edu.duke.ece.fantasy.Item.SuperPotion;
import edu.duke.ece.fantasy.database.*;

public class SuperShop extends Shop {

    public SuperShop() {
        super("super_shop", 200);
        shopInventory potion_Inventory = new shopInventory(new SuperPotion().toDBItem(), 20);
        possible_inventory.add(potion_Inventory);
    }

}
