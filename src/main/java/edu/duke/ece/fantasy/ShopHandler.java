package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.ShopRequestMessage;
import edu.duke.ece.fantasy.json.ShopResultMessage;
import org.hibernate.Session;

import java.util.Map;

public class ShopHandler {
    private ShopDAO shopDAO;
    private PlayerDAO playerDAO;
    private ItemPackDAO itemPackDAO;
    private Session session;

    public ShopHandler(Session session) {
        shopDAO = new ShopDAO(session);
        playerDAO = new PlayerDAO(session);
        itemPackDAO = new ItemPackDAO(session);
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
        result.setItems(shop.getInventory());
        return result;
    }

    private boolean validate(Trader seller, Trader buyer, Map<Integer, Integer> item_list) throws Exception {
        int required_money = 0;
        for (Map.Entry item : item_list.entrySet()) {
            ItemPack itemPack = itemPackDAO.getItemPack((Integer) item.getKey());
            int amount = (Integer) item.getValue();
            // check if seller have enough itemPack
            if (!seller.checkItem(itemPack, amount)) {
                throw new Exception("Seller don't have enough item" + "-" + itemPack.getItem().getName());
            }
            required_money += itemPack.getItem().getCost() * amount;
        }
        // check if buyer have enough money
        if (!buyer.checkMoney(required_money)) {
            throw new Exception("Don't have enough money");
        }
        return true;
    }

    private void execute(Trader seller, Trader buyer, Map<Integer, Integer> item_list) {
        for (Map.Entry item : item_list.entrySet()) {
            ItemPack itemPack = itemPackDAO.getItemPack((Integer) item.getKey());
            int amount = (Integer) item.getValue();
            // deduce the amount of item from seller
            seller.sellItem(itemPack, amount);
            // add the amount of item to buyer
            buyer.buyItem(itemPack, amount);
        }
    }

}
