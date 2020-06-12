package edu.duke.ece.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.Item.IItem;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.ShopRequestMessage;
import edu.duke.ece.fantasy.json.ShopResultMessage;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShopHandlerTest {
    ShopHandler shopHandler;
    ShopDAO shopDAO;
    PlayerDAO playerDAO;
    ObjectMapper objectMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(ShopHandlerTest.class);

    Session createSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        shopHandler = new ShopHandler(session);
        shopDAO = new ShopDAO(session);
        playerDAO = new PlayerDAO(session);
        return session;
    }


    @Test
    void handle() {
        try (Session session = createSession()) {
            (new Initializer()).initialize();
            session.beginTransaction();
            PlayerDAO playerDAO = new PlayerDAO(session);
            Player player = playerDAO.getPlayer("test");
            if (player == null) { // if test player doesn't exist
                playerDAO.addPlayer("test", "test");
            }

            Shop shop = shopDAO.createShop();

            handle_list(shop);
            handle_buy(shop);
        }
    }

    void handle_list(Shop shop) {
        ShopRequestMessage shopRequestMessage = new ShopRequestMessage();
        shopRequestMessage.setShopID(shop.getId());
        shopRequestMessage.setAction("list");

        Player player = playerDAO.getPlayer("test");
        ShopResultMessage resultMessage = shopHandler.handle(shopRequestMessage, player.getId());
        try {
            logger.info(objectMapper.writeValueAsString(resultMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    void handle_buy(Shop shop) {
        List<shopInventory> itemPacks = shop.getItems();
        for (shopInventory select_item : itemPacks) {
            try {
                int itemPack_id = select_item.getId();
                int item_amount = select_item.getAmount();
                IItem item_obj = (IItem) Class.forName(select_item.getItem_name()).getDeclaredConstructor().newInstance();
                int required_money = item_obj.getCost() * item_amount;
                Player player = playerDAO.getPlayer("test");

                // Don't have enough money
                player.setMoney(required_money - 1);
                ShopResultMessage resultMessage = buy_item(player, shop, itemPack_id, item_amount);

                assertNotEquals("valid", resultMessage.getResult());

                // shop don't have enough item
                player.setMoney(required_money);
                buy_item(player, shop, itemPack_id, item_amount + 1);

                // success
                player.setMoney(required_money);
                resultMessage = buy_item(player, shop, itemPack_id, item_amount - 1);
                assertEquals("valid", resultMessage.getResult());
                try {
                    logger.info(objectMapper.writeValueAsString(resultMessage));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

//        ShopResultMessage resultMessage = shopHandler.handle(shopRequestMessage, player.getId());
//        try {
//            logger.info(objectMapper.writeValueAsString(resultMessage));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
    }

    ShopResultMessage buy_item(Player player, Shop shop, int itemPack_id, int item_amount) {
        ShopRequestMessage shopRequestMessage = new ShopRequestMessage();
        shopRequestMessage.setShopID(shop.getId());
        shopRequestMessage.setAction("buy");
        Map<Integer, Integer> itemMap = new HashMap<>();
        itemMap.put(itemPack_id, item_amount);
        shopRequestMessage.setItemMap(itemMap);
        return shopHandler.handle(shopRequestMessage, player.getId());
    }
}