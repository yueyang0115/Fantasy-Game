package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.Item.Item;
import edu.duke.ece.fantasy.building.Shop;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.*;
import edu.duke.ece.fantasy.json.InventoryRequestMessage;
import edu.duke.ece.fantasy.json.ShopRequestMessage;
import edu.duke.ece.fantasy.json.ShopResultMessage;
import org.hibernate.Session;

import java.util.Map;

public class ShopHandler {
    private PlayerDAO playerDAO;
    private PlayerInventoryDAO playerinventoryDAO;
    private ShopInventoryDAO shopInventoryDAO;
    private DBBuildingDAO dbBuildingDAO;
    private InventoryDAO inventoryDAO;
    private Session session;
    private MetaDAO metaDAO;

    public ShopHandler(MetaDAO metaDAO) {
        this.metaDAO = metaDAO;
        dbBuildingDAO = metaDAO.getDbBuildingDAO();
        playerDAO = metaDAO.getPlayerDAO();
        playerinventoryDAO = metaDAO.getPlayerInventoryDAO();
        shopInventoryDAO = metaDAO.getShopInventoryDAO();
        inventoryDAO = metaDAO.getInventoryDAO();
        this.session = metaDAO.getSession();
    }

    public void createShop() {

    }

    public ShopResultMessage handle(ShopRequestMessage request, int playerID) {

        String action = request.getAction();
//        DBShop DBShop = DBShopDAO.getShop(request.getShopID());

        Shop shop = (Shop) dbBuildingDAO.getBuilding(request.getCoord()).toGameBuilding();
        shop.loadInventory(session, request.getCoord());

        Map<Integer, Integer> item_list = request.getItemMap();
        // may need to check relationship of shop and territory

        Player player = playerDAO.getPlayer(playerID);

        ShopResultMessage result = new ShopResultMessage();

        try {
            if (action.equals("list")) {
                result.setResult("valid");
            } else if (action.equals("buy")) {
                validateAndExecute(shop, player, item_list);
                result.setResult("valid");
            } else if (action.equals("sell")) {
                validateAndExecute(player, shop, item_list);
                result.setResult("valid");
            }
        } catch (InvalidShopRequest e) {
//            session.getTransaction().rollback();
            result.setResult("invalid:" + e.getMessage());
        }
        // get latest data from db(previous transaction may roll back)
//        DBShop = DBShopDAO.getShop(request.getShopID());
//        List<shopInventory> db_items = shop.getCurrent_inventory();
        shop.loadInventory(session, request.getCoord());

        for (shopInventory inventory : shop.getCurrent_inventory()) {
            // add more information of item
            if (inventory.getAmount() == 0) continue;
            Inventory toClientInventory = new Inventory(inventory.getId(), inventory.getDBItem().toGameItem().toClient(), inventory.getAmount());
            result.addItem(toClientInventory);
        }

        InventoryRequestMessage inventoryRequestMessage = new InventoryRequestMessage();
        inventoryRequestMessage.setAction("list");
        result.setInventoryResultMessage((new InventoryHandler(metaDAO)).handle(inventoryRequestMessage, playerID));
        return result;
    }

    public void validateAndExecute(Trader seller, Trader buyer, Map<Integer, Integer> item_list) throws InvalidShopRequest {
        int required_money = 0;

        for (Map.Entry<Integer, Integer> inventory_pair : item_list.entrySet()) {
            Inventory inventory = inventoryDAO.getInventory(inventory_pair.getKey());
            int amount = inventory_pair.getValue();
            Item item_obj = inventory.getDBItem().toGameItem();
            // check if seller have enough inventory
            if (!seller.checkItem(inventory, amount)) {
                throw new InvalidShopRequest("Seller don't have enough item" + "-" + item_obj.getName());
            }
            required_money += item_obj.getCost() * amount;
            // check if buyer have enough money
            if (!buyer.checkMoney(required_money)) {
                throw new InvalidShopRequest("Don't have enough money");
            }
        }

        for (Map.Entry<Integer, Integer> inventory_pair : item_list.entrySet()) {
            Inventory inventory = inventoryDAO.getInventory(inventory_pair.getKey());
            int amount = inventory_pair.getValue();
            // deduce the amount of item from seller
            seller.sellItem(inventory, amount);
            // sell inventory update
            if (inventory.getAmount() == 0) { // delete record if it's amount is 0
                session.delete(inventory);
            }
            // add the amount of item to buyer
            Inventory buy_inventory = buyer.buyItem(inventory, amount);
            // buy inventory update
            session.saveOrUpdate(buy_inventory);
        }

    }

}
