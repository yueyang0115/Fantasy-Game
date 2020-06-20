package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.Item.Item;
import edu.duke.ece.fantasy.building.Building;
import edu.duke.ece.fantasy.building.Shop;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.InventoryRequestMessage;
import edu.duke.ece.fantasy.json.ShopRequestMessage;
import edu.duke.ece.fantasy.json.ShopResultMessage;
import org.hibernate.Session;

import java.util.List;
import java.util.Map;

public class ShopHandler {
    private PlayerDAO playerDAO;
    private playerInventoryDAO playerinventoryDAO;
    private shopInventoryDAO shopInventoryDAO;
    private DBBuildingDAO dbBuildingDAO;
    private InventoryDAO inventoryDAO;
    private Session session;

    public ShopHandler(Session session) {
        dbBuildingDAO = new DBBuildingDAO(session);
        playerDAO = new PlayerDAO(session);
        playerinventoryDAO = new playerInventoryDAO(session);
        shopInventoryDAO = new shopInventoryDAO(session);
        inventoryDAO = new InventoryDAO(session);
        this.session = session;

    }

    public void createShop() {

    }

    public ShopResultMessage handle(ShopRequestMessage request, int playerID) {
        String action = request.getAction();
//        DBShop DBShop = DBShopDAO.getShop(request.getShopID());
        Shop shop = (Shop) dbBuildingDAO.getBuilding(request.getCoord()).toGameBuilding();
        shop.loadInventory(session,request.getCoord());
        Map<Integer, Integer> item_list = request.getItemMap();
        // may need to check relationship of shop and territory
        Player player = playerDAO.getPlayer(playerID);
        ShopResultMessage result = new ShopResultMessage();

        if (action.equals("list")) {
            result.setResult("valid");
        } else if (action.equals("buy")) {
            try {
                validateAndExecute(shop, player, item_list);
                result.setResult("valid");
            } catch (Exception e) {
                result.setResult("invalid:" + e.getMessage());
            }
        } else if (action.equals("sell")) {
//            try {
//                validateAndExecute(player, DBShop, item_list);
//                result.setResult("valid");
//            } catch (Exception e) {
//                result.setResult("invalid:" + e.getMessage());
//            }
        }
        // get latest data from db(previous transaction may roll back)
//        DBShop = DBShopDAO.getShop(request.getShopID());
//        List<shopInventory> db_items = shop.getCurrent_inventory();
        shop.loadInventory(session,request.getCoord());

        for (shopInventory db_item : shop.getCurrent_inventory()) {
            // add more information of item
            Inventory toClientInventory = new Inventory(db_item.getId(), db_item.getDBItem().toGameItem().toClient(), db_item.getAmount());
            result.addItem(toClientInventory);
        }

        InventoryRequestMessage inventoryRequestMessage = new InventoryRequestMessage();
        inventoryRequestMessage.setAction("list");
        result.setInventoryResultMessage((new InventoryHandler(session)).handle(inventoryRequestMessage, playerID));
        return result;
    }

    public void validateAndExecute(Trader seller, Trader buyer, Map<Integer, Integer> item_list) throws Exception {
        int required_money = 0;
        for (Map.Entry<Integer, Integer> inventory_pair : item_list.entrySet()) {
            Inventory inventory = inventoryDAO.getInventory(inventory_pair.getKey());
            int amount = inventory_pair.getValue();
            Item item_obj = inventory.getDBItem().toGameItem();
            // check if seller have enough inventory
            if (!seller.checkItem(inventory, amount)) {
                session.getTransaction().rollback();
                throw new Exception("Seller don't have enough item" + "-" + item_obj.getName());
            }
            // deduce the amount of item from seller
            seller.sellItem(inventory, amount);
            // add the amount of item to buyer
            buyer.buyItem(inventory, amount);
            if (inventory.getAmount() == 0) { // delete record if it's amount is 0
                session.delete(inventory);
            }
            required_money += item_obj.getCost() * amount;
        }
        // check if buyer have enough money
        if (!buyer.checkMoney(required_money)) {
            session.getTransaction().rollback();
            throw new Exception("Don't have enough money");
        }
    }

}
