package edu.duke.ece.fantasy.Building.Prototype;

import edu.duke.ece.fantasy.Item.NormalPotion;
import edu.duke.ece.fantasy.Item.SuperPotion;
import edu.duke.ece.fantasy.Item.Sword;
import edu.duke.ece.fantasy.database.*;

public class BaseShop extends Shop {

    public BaseShop() {
        super("shop", 200);
        shopInventory potion_Inventory = new shopInventory(new NormalPotion().toDBItem(), 20);
        possible_inventory.add(potion_Inventory);
        possible_inventory.add(new shopInventory(new SuperPotion().toDBItem(), 20));
        possible_inventory.add(new shopInventory(new Sword().toDBItem(), 20));
        Shop super_shop = new SuperShop();
        UpgradeTo.put(super_shop.getName(), super_shop);
    }


}
