package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.Item.IItem;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.InventoryRequestMessage;
import edu.duke.ece.fantasy.json.ShopRequestMessage;
import edu.duke.ece.fantasy.json.ShopResultMessage;
import org.hibernate.Session;

import java.util.Map;

public class ShopHandler {
    private ShopDAO shopDAO;
    private PlayerDAO playerDAO;
    private InventoryDAO inventoryDAO;
    private Session session;

    public ShopHandler(Session session) {
        shopDAO = new ShopDAO(session);
        playerDAO = new PlayerDAO(session);
        inventoryDAO = new InventoryDAO(session);
        this.session = session;
    }

    public ShopResultMessage handle(ShopRequestMessage request, int playerID) {
        String action = request.getAction();
        Shop shop = shopDAO.getShop(request.getShopID());
        Map<Integer, Integer> item_list = request.getItemMap();
        // may need to check relationship of shop and territory
        Player player = playerDAO.getPlayer(playerID);
        ShopResultMessage result = new ShopResultMessage();
        if (action.equals("list")) {
            result.setResult("valid");
        } else if (action.equals("buy")) {  //
            try {
                if (validate(shop, player, item_list)) {
                    execute(shop, player, item_list);
                    result.setResult("valid");
                }
            } catch (Exception e) {
                result.setResult("invalid:" + e.getMessage());
            }
        } else if (action.equals("sell")) {
            try {
                if (validate(player, shop, item_list)) {
                    execute(player, shop, item_list);
                    result.setResult("valid");
                }
            } catch (Exception e) {
                result.setResult("invalid:" + e.getMessage());
            }
        }
        session.update(shop);
        session.update(player);
        result.setItems(shop.getItems());
        InventoryRequestMessage inventoryRequestMessage = new InventoryRequestMessage();
        inventoryRequestMessage.setAction("list");
        result.setInventoryResultMessage((new InventoryHandler(session)).handle(inventoryRequestMessage, playerID));
        return result;
    }

    private boolean validate(Trader seller, Trader buyer, Map<Integer, Integer> item_list) throws Exception {
        int required_money = 0;
        for (Map.Entry inventory_pair : item_list.entrySet()) {
            Inventory inventory = inventoryDAO.getInventory((Integer) inventory_pair.getKey());
            int amount = (Integer) inventory_pair.getValue();
            IItem item_obj = (IItem) Class.forName(inventory.getItem_name()).getDeclaredConstructor().newInstance();
            // check if seller have enough itemPack
            if (!seller.checkItem(inventory, amount)) {
                throw new Exception("Seller don't have enough item" + "-" + inventory.getItem_name());
            }
            required_money += item_obj.getCost() * amount;
        }
        // check if buyer have enough money
        if (!buyer.checkMoney(required_money)) {
            throw new Exception("Don't have enough money");
        }
        return true;
    }

    private void execute(Trader seller, Trader buyer, Map<Integer, Integer> item_list) {
        for (Map.Entry inventory_pair : item_list.entrySet()) {
            try {
                Inventory inventory = inventoryDAO.getInventory((Integer) inventory_pair.getKey());
                int amount = (Integer) inventory_pair.getValue();
                // deduce the amount of item from seller
                seller.sellItem(inventory, amount);
                // add the amount of item to buyer
                buyer.buyItem(inventory, amount);
                if (inventory.getOwnerID() == -1) { // delete record if it doesn't have owner
                    session.delete(inventory);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
