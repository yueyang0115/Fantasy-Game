package edu.duke.ece.fantasy.Building.Prototype;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.ShopInventoryDAO;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public abstract class Shop extends Building implements Trader {
    @JsonIgnore
    List<shopInventory> possible_inventory = new ArrayList<>();

    @JsonIgnore
    List<shopInventory> current_inventory = new ArrayList<>();


    public Shop(String name, int cost) {
        super(name, cost);
    }

    public List<shopInventory> getCurrent_inventory() {
        return current_inventory;
    }

    public List<shopInventory> getPossible_inventory() {
        return possible_inventory;
    }

    public void setCurrent_inventory(List<shopInventory> current_inventory) {
        this.current_inventory = current_inventory;
    }

    @Override
    public void onCreate(WorldCoord coord, MetaDAO metaDAO) {
//        DBBuilding dbBuilding = SaveToBuildingTable(session, coord);
        super.onCreate(coord, metaDAO);
        // delete all old inventory
        ShopInventoryDAO shopinventoryDAO = metaDAO.getShopInventoryDAO();
        shopinventoryDAO.deleteInventory(coord);
        for (shopInventory inventory : possible_inventory) {
            inventory.setCoord(coord);
            shopinventoryDAO.addInventory(inventory);
        }
    }

    public List<Inventory> getInventory(MetaDAO metaDAO) {
        ShopInventoryDAO shopInventoryDAO = metaDAO.getShopInventoryDAO();
        return shopInventoryDAO.getInventories(coord);
    }


    @Override
    public boolean checkMoney(int required_money) {
        return true;
    }

    @Override
    public boolean checkItem(Inventory inventory, int amount) {
//        for (Inventory item : getInventory()) {
//            if (item.equals(inventory)) { // if have this type of item
//                return item.getAmount() >= amount;
//            }
//        }
        return false;
    }

//    @Override
//    public void sellItem(Inventory inventory, int amount) {
//        int left_amount = inventory.getAmount() - amount;
//        inventory.setAmount(left_amount);
//    }

    @Override
    public void addMoney(int money) { //TODO: may need an adapter
    }

    @Override
    public void subtractMoney(int money) {
    }

    @Override
    public Inventory addInventory(Session session, Inventory inventory) {
        shopInventory shopInventory = new shopInventory(inventory.getDBItem().toGameItem().toDBItem(), inventory.getAmount(), coord);
        session.save(shopInventory);
        return shopInventory;
    }

}
