package edu.duke.ece.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.PositionRequestMessage;
import edu.duke.ece.fantasy.json.PositionResultMessage;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PositionUpdateHandlerTest {
    PositionUpdateHandler positionUpdateHandler;
    ObjectMapper objectMapper = new ObjectMapper();
    PlayerDAO playerDAO;
    Logger logger = LoggerFactory.getLogger(PositionUpdateHandlerTest.class);

    Session createSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        positionUpdateHandler = new PositionUpdateHandler(session);
        playerDAO = new PlayerDAO(session);
        return session;
    }


    @Test
    void handle() {
        try (Session session = createSession()) {
//            (new Initializer()).test_initialize(session);
            session.beginTransaction();
            (new Initializer(session)).initialize_test_player();
            Player player = playerDAO.getPlayer("test");
            player = playerDAO.getPlayerByWid(player.getWid()); // TODO: wid return give null pointer
            List<WorldCoord> coords = new ArrayList<>();
            for (int i=0;i<100;i+=10){
                for (int j=0;j<100;j+=10){
                    coords.add(new WorldCoord(player.getWid(), i, j));
                }
            }
            PositionRequestMessage positionRequestMessage = new PositionRequestMessage();
            positionRequestMessage.setCoords(coords);
            positionRequestMessage.setCurrentCoord(coords.get(0));
            PositionResultMessage res = positionUpdateHandler.handle(player.getWid(), positionRequestMessage);
            try {
                logger.info(objectMapper.writeValueAsString(res));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}