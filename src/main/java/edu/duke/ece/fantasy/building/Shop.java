package edu.duke.ece.fantasy.building;

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
    public void onCreate(MetaDAO metaDAO, WorldCoord coord) {
//        DBBuilding dbBuilding = SaveToBuildingTable(session, coord);
        super.onCreate(metaDAO, coord);
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
    public Inventory addInventory(MetaDAO metaDAO, Inventory inventory) {
        shopInventory shopInventory = new shopInventory(inventory.getDBItem(), inventory.getAmount(), coord);
        metaDAO.getSession().save(shopInventory);
        return shopInventory;
//        ShopInventoryDAO shopInventoryDAO = metaDAO.getShopInventoryDAO();
//        return shopInventoryDAO.addInventory(inventory,coord);
    }

//    @Override
    //    public Inventory buyItem(Inventory select_item, int amount) {
//        Inventory inventory = null;
//        for (Inventory item : current_inventory) { // get
//            if (item.equals(select_item)) { // if have this type of item
//                int init_amount = item.getAmount();
//                inventory = item;
//                item.setAmount(init_amount + amount);
//            }
//        }
//        if (inventory == null) {
//            inventory = new shopInventory(select_item.getDBItem(), amount, coord);
//        }
//        return inventory;
//    }

}
