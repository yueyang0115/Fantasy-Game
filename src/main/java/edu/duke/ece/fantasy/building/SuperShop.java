package edu.duke.ece.fantasy.building;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.duke.ece.fantasy.Item.Potion;
import edu.duke.ece.fantasy.Item.SuperPotion;
import edu.duke.ece.fantasy.database.*;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class SuperShop extends Shop {

    public SuperShop() {
        super("super_shop", 200);
        shopInventory potion_Inventory = new shopInventory(new SuperPotion().toDBItem(), 20);
        possible_inventory.add(potion_Inventory);
    }

    public List<shopInventory> getCurrent_inventory() {
        return current_inventory;
    }

    public void setCurrent_inventory(List<shopInventory> current_inventory) {
        this.current_inventory = current_inventory;
    }

    @Override
    public void onCreate(Session session, WorldCoord coord) {
//        DBBuilding dbBuilding = SaveToBuildingTable(session, coord);
//        shopInventoryDAO shopInventoryDAO = new shopInventoryDAO(session);
        super.onCreate(session, coord);
        for (shopInventory inventory : possible_inventory) {
            inventory.setDBBuilding(dbBuilding);
            session.save(inventory);
        }
    }

    public void loadInventory(Session session, WorldCoord coord) {
        shopInventoryDAO shopInventoryDAO = new shopInventoryDAO(session);
        current_inventory = shopInventoryDAO.getInventories(coord);
//        shopInventoryDAO.getInventory();
    }


    @Override
    public boolean checkMoney(int required_money) {
        return true;
    }

    @Override
    public boolean checkItem(Inventory inventory, int amount) {
        for (Inventory item : current_inventory) {
            if (item.equals(inventory)) { // if have this type of item
                return item.getAmount() >= amount;
            }
        }
        return false;
    }

    @Override
    public void sellItem(Inventory inventory, int amount) {
        int left_amount = inventory.getAmount() - amount;
        inventory.setAmount(left_amount);
    }

    @Override
    public Inventory buyItem(Inventory select_item, int amount) {
        Inventory inventory = null;
        for (Inventory item : current_inventory) {
            if (item.equals(select_item)) { // if have this type of item
                int init_amount = item.getAmount();
                inventory = item;
                item.setAmount(init_amount + amount);
            }
        }
        if (inventory == null) {
            inventory = new shopInventory(select_item.getDBItem(), amount, dbBuilding);
        }
        return inventory;
    }

}
