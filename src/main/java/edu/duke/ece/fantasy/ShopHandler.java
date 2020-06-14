package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.Item.IItem;
import edu.duke.ece.fantasy.Item.Item;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.InventoryRequestMessage;
import edu.duke.ece.fantasy.json.ShopRequestMessage;
import edu.duke.ece.fantasy.json.ShopResultMessage;
import net.bytebuddy.implementation.bytecode.Addition;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopHandler {
    private ShopDAO shopDAO;
    private PlayerDAO playerDAO;
    private playerInventoryDAO playerinventoryDAO;
    private shopInventoryDAO shopInventoryDAO;
    private InventoryDAO inventoryDAO;
    private Session session;

    public ShopHandler(Session session) {
        shopDAO = new ShopDAO(session);
        playerDAO = new PlayerDAO(session);
        playerinventoryDAO = new playerInventoryDAO(session);
        shopInventoryDAO = new shopInventoryDAO(session);
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
        } else if (action.equals("buy")) {
            try {
                validateAndExecute(shop, player, item_list);
                result.setResult("valid");
            } catch (Exception e) {
                result.setResult("invalid:" + e.getMessage());
            }
        } else if (action.equals("sell")) {
            try {
                validateAndExecute(shop, player, item_list);
                result.setResult("valid");
//                if (validate(player, shop, item_list, shopInventoryDAO)) {
//                    execute(player, shop, item_list, shopInventoryDAO);
//                }
            } catch (Exception e) {
                result.setResult("invalid:" + e.getMessage());
            }
        }
        // get latest data from db(previous transaction may roll back)
        shop = shopDAO.getShop(request.getShopID());
        result.setItems(new ArrayList<>(shop.getItems()));
        InventoryRequestMessage inventoryRequestMessage = new InventoryRequestMessage();
        inventoryRequestMessage.setAction("list");
        result.setInventoryResultMessage((new InventoryHandler(session)).handle(inventoryRequestMessage, playerID));
        return result;
    }

    public void validateAndExecute(Trader seller, Trader buyer, Map<Integer, Integer> item_list) throws Exception {
        int required_money = 0;
        List<Inventory> to_be_delete = new ArrayList<>();
        for (Map.Entry<Integer, Integer> inventory_pair : item_list.entrySet()) {
//            Inventory inventory = inventoryDAO.getInventory(seller.getId(), inventory_pair.getKey());
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
            if (inventory.getAmount() == 0) { // delete record if it amount is 0
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
