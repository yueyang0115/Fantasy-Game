package edu.duke.ece.fantasy.building;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.duke.ece.fantasy.Item.Potion;
import edu.duke.ece.fantasy.Item.SuperPotion;
import edu.duke.ece.fantasy.database.*;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class BaseShop extends Shop {

    public BaseShop() {
        super("shop", 200);
        shopInventory potion_Inventory = new shopInventory(new Potion().toDBItem(), 20);
        possible_inventory.add(potion_Inventory);
        possible_inventory.add(new shopInventory(new SuperPotion().toDBItem(), 20));
        Shop super_shop = new SuperShop();
        UpgradeTo.put(super_shop.getName(), super_shop);
    }


}
