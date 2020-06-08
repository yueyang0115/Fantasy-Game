package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.ShopRequestMessage;
import edu.duke.ece.fantasy.json.ShopResultMessage;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryHandlerTest {
    ItemDAO itemDAO;
    PlayerDAO playerDAO;
    Session session;

    InventoryHandlerTest() {
        session = HibernateUtil.getSessionFactory().openSession();
        itemDAO = new ItemDAO(session);
        playerDAO = new PlayerDAO(session);
    }

    void initial_player() {
        List<Item> items = itemDAO.getAllItem();
        List<ItemPack> itemPacks = new ArrayList<>();
        for (Item item : items) {
            itemPacks.add(new ItemPack(item, 20));
        }
        playerDAO.addPlayer("test", "test");
        Player player = playerDAO.getPlayer("test");
        for (ItemPack itemPack : itemPacks) {
            player.addItem(itemPack);
        }
        session.save(player);
    }

    @Test
    void handle() {
        initial_player();
        handle_list();
        handle_use();
    }


    void handle_list() {
//        ShopRequestMessage shopRequestMessage = new ShopRequestMessage();
//        shopRequestMessage.setShopID(shop.getId());
//        shopRequestMessage.setAction("list");
//
//        ShopResultMessage resultMessage = shopHandler.handle(shopRequestMessage, player.getId());
//
//        assertEquals("valid", resultMessage.getResult());

    }

    void handle_use(){
//        ShopRequestMessage shopRequestMessage = new ShopRequestMessage();
//        shopRequestMessage.setShopID(shop.getId());
//        shopRequestMessage.setAction("use");
//
//        ShopResultMessage resultMessage = shopHandler.handle(shopRequestMessage, player.getId());
//
//        assertEquals(19,amount);
    }
}