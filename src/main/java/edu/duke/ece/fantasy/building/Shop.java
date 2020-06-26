package edu.duke.ece.fantasy.building;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.ShopInventoryDAO;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public abstract class Shop extends Building implements Trader {
    List<shopInventory> possible_inventory = new ArrayList<>();

    @JsonIgnore
    List<shopInventory> current_inventory = new ArrayList<>();


    public Shop(String name, int cost) {
        super(name, cost);
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
        // delete all old inventory
        ShopInventoryDAO shopinventoryDAO = new ShopInventoryDAO(session);
        shopinventoryDAO.deleteInventory(coord);
        for (shopInventory inventory : possible_inventory) {
            inventory.setCoord(coord);
            session.save(inventory);
        }
    }

    public void loadInventory(Session session, WorldCoord coord) {
        ShopInventoryDAO shopInventoryDAO = new ShopInventoryDAO(session);
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
            inventory = new shopInventory(select_item.getDBItem(), amount, coord);
        }
        return inventory;
    }

}
