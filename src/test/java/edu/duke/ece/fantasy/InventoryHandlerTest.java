package edu.duke.ece.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.InventoryRequestMessage;
import edu.duke.ece.fantasy.json.InventoryResultMessage;
import edu.duke.ece.fantasy.json.ShopRequestMessage;
import edu.duke.ece.fantasy.json.ShopResultMessage;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryHandlerTest {
    ItemDAO itemDAO;
    ItemPackDAO itemPackDAO;
    PlayerDAO playerDAO;
    Session session;
    InventoryHandler inventoryHandler;
    List<Item> items;
    List<ItemPack> itemPacks = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(InventoryHandlerTest.class);

    InventoryHandlerTest() {
        (new Initializer()).initialize();
        session = HibernateUtil.getSessionFactory().openSession();
        itemDAO = new ItemDAO(session);
        playerDAO = new PlayerDAO(session);
        itemPackDAO = new ItemPackDAO(session);
        inventoryHandler = new InventoryHandler(session);
        items = itemDAO.getAllItem();

    }

    void initial_player() {
        session.beginTransaction();
        for (Item item : items) { // add itempack
            itemPacks.add(new ItemPack(item, 20));
        }
        Player player = playerDAO.getPlayer("test");
        if (player == null) { // if test player doesn't exist
            playerDAO.addPlayer("test", "test");
            player = playerDAO.getPlayer("test");
        }
        for (ItemPack itemPack : itemPacks) {
            player.addItem(itemPack);
        }
        session.update(player);
//        session.getTransaction().commit();
    }

    @Test
    void handle() {
        Player player = playerDAO.getPlayer("test");
        initial_player();

        handle_list();
        handle_use();
    }


    void handle_list() {
        InventoryRequestMessage inventoryRequestMessage = new InventoryRequestMessage();
        inventoryRequestMessage.setAction("list");
        Player player = playerDAO.getPlayer("test");
        InventoryResultMessage resultMessage = new InventoryResultMessage();

        for (ItemPack itemPack : itemPacks) {
            int itemPack_id = itemPack.getId();
            inventoryRequestMessage.setItemPackID(itemPack_id);
            resultMessage = inventoryHandler.handle(inventoryRequestMessage, player.getId());
            assertEquals("valid", resultMessage.getResult());
        }
        try {
            logger.info(objectMapper.writeValueAsString(resultMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    void handle_use() {
        InventoryRequestMessage inventoryRequestMessage = new InventoryRequestMessage();
        inventoryRequestMessage.setAction("use");
        Player player = playerDAO.getPlayer("test");
        InventoryResultMessage resultMessage = new InventoryResultMessage();

        for (ItemPack itemPack : itemPacks) {
            int i = itemPack.getAmount()-1;
            int itemPack_id = itemPack.getId();
            inventoryRequestMessage.setItemPackID(itemPack_id);
            resultMessage = inventoryHandler.handle(inventoryRequestMessage, player.getId());
            assertEquals(i, itemPack.getAmount());
        }
        try {
            logger.info(objectMapper.writeValueAsString(resultMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}