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
        super("Shop", 200);
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
    public void onCreate(Session session, WorldCoord coord) {
        DBBuilding dbBuilding = SaveToBuildingTable(session, coord);
        shopInventoryDAO shopInventoryDAO = new shopInventoryDAO(session);
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
        return false;
    }

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
