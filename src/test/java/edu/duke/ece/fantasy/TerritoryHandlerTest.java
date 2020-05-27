package edu.duke.ece.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Territory;
import edu.duke.ece.fantasy.json.MessagesS2C;
import edu.duke.ece.fantasy.json.PositionResultMessage;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryHandlerTest {
    TerritoryHandler th;
    Logger logger = LoggerFactory.getLogger(TerritoryHandler.class);
    ObjectMapper objectMapper = new ObjectMapper();
    double latitude = 0;
    double longitude = 0;
    int wid = 0;
    int[] coor;
    TerrainHandler terrainHandler;

    Session createSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        th = new TerritoryHandler(session);
        terrainHandler = new TerrainHandler(session);
        coor = th.MillierConvertion(latitude, longitude);
        return session;
    }

    TerritoryHandlerTest() {

    }

    @Test
    void testAll() {
        try {
            getTerritories();
            updateTerritory();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            HibernateUtil.shutdown();
        }
    }

    void getTerritories() {
        try (Session session = createSession()) {
            session.beginTransaction();
            terrainHandler.initialTerrain();
            th.addTerritories(wid, latitude, longitude);
            List<Territory> res = th.getTerritories(wid, latitude, longitude);
            assertEquals(9, res.size());
            session.getTransaction().commit();
            session.close();
            MessagesS2C msg = new MessagesS2C();
            PositionResultMessage positionResultMessage = new PositionResultMessage();
            positionResultMessage.setTerritoryArray(res);
            msg.setPositionResultMessage(positionResultMessage);
            try {
                logger.info(objectMapper.writeValueAsString(msg));
            } catch (JsonProcessingException e) {
                logger.debug(e.getMessage());
            }
        }
    }


    void updateTerritory() {
        try (Session session = createSession()) {
            session.beginTransaction();
            terrainHandler.initialTerrain();
            th.addTerritory(wid, coor[0], coor[1], "unexplored");
            th.updateTerritory(wid, coor[0], coor[1], "explored");
            assertEquals("explored", th.getTerritory(wid, coor[0], coor[1]).getStatus());
            session.getTransaction().commit();
        }
    }

}
