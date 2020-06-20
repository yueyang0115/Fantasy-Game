package edu.duke.ece.fantasy.building;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.duke.ece.fantasy.Item.Item;
import edu.duke.ece.fantasy.Item.Potion;
import edu.duke.ece.fantasy.database.*;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Shop extends Building implements Trader {
    List<shopInventory> possible_inventory = new ArrayList<>();

    @JsonIgnore
    List<shopInventory> current_inventory = new ArrayList<>();

    public Shop() {
        super("shop", 200);
        shopInventory shopInventory = new shopInventory(new Potion().toDBItem(), 20);
        possible_inventory.add(shopInventory);
    }

    public List<shopInventory> getCurrent_inventory() {
        return current_inventory;
    }

    public void setCurrent_inventory(List<shopInventory> current_inventory) {
        this.current_inventory = current_inventory;
    }

    @Override
    public DBBuilding onCreate(Session session, WorldCoord coord) {
//        DBBuilding dbBuilding = SaveToBuildingTable(session, coord);
//        shopInventoryDAO shopInventoryDAO = new shopInventoryDAO(session);
        DBBuilding dbBuilding = super.onCreate(session,coord);
        for (shopInventory inventory : possible_inventory) {
            inventory.setDBBuilding(dbBuilding);
            session.save(inventory);
        }
        return dbBuilding;
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

//    @Override
//    public boolean checkItem(Inventory inventory, int amount) {
//        for (Inventory item : items) {
//            if (item == inventory) { // if have this type of item
//                return item.getAmount() >= amount;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public void sellItem(Inventory inventory, int amount) {
//        int left_amount = inventory.getAmount() - amount;
//        inventory.setAmount(left_amount);
//        if (left_amount == 0) {
//            this.getItems().remove(inventory);
//        }
//    }
//
//    @Override
//    public void buyItem(Inventory select_item, int amount) {
//        boolean find = false;
//        for (Inventory item : items) {
//            if (item.equals(select_item)) { // if have this type of item
//                int init_amount = item.getAmount();
//                item.setAmount(init_amount + amount);
//                find = true;
//            }
//        }
//        if (!find) {
//            shopInventory new_item = new shopInventory(select_item.getDBItem(), amount, this);
//            addInventory(new_item);
//        }
//    }

//
    @Override
    public boolean checkItem(Inventory inventory, int amount) {
        return false;
    }

    @Override
    public void sellItem(Inventory inventory, int amount) {

    }

    @Override
    public void buyItem(Inventory inventory, int amount) {

    }
}
