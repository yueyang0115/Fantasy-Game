package edu.duke.ece.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.MessagesS2C;
import edu.duke.ece.fantasy.json.ShopRequestMessage;
import edu.duke.ece.fantasy.json.ShopResultMessage;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class ShopHandlerTest {
    ShopHandler shopHandler;
    ShopDAO shopDAO;
    ObjectMapper objectMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(ShopHandlerTest.class);

    Session createSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        shopHandler = new ShopHandler(session);
        shopDAO = new ShopDAO(session);
        return session;
    }

    @Test
    void handle() {
        try(Session session = createSession()){
            (new Initializer()).initialize();
            session.beginTransaction();
            Shop shop = shopDAO.getRandomShop();
            ShopRequestMessage shopRequestMessage = new ShopRequestMessage();
            shopRequestMessage.setAction("list");
            shopRequestMessage.setShopID(shop.getId());


            ShopResultMessage resultMessage = shopHandler.handle(shopRequestMessage);
            resultMessage.getItems();
            try {
                logger.info(objectMapper.writeValueAsString(resultMessage));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}