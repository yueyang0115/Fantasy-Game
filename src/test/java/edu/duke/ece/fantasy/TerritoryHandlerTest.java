package edu.duke.ece.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.TerrainDAO;
import edu.duke.ece.fantasy.database.Territory;
import edu.duke.ece.fantasy.database.TerritoryDAO;
import edu.duke.ece.fantasy.json.MessagesS2C;
import edu.duke.ece.fantasy.json.PositionResultMessage;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryHandlerTest {
    TerritoryDAO th;
    Logger logger = LoggerFactory.getLogger(TerritoryDAO.class);
    ObjectMapper objectMapper = new ObjectMapper();
    double latitude = 35;
    double longitude = 178;
    int x = 5;
    int y = 5;
    int x_block_num = 3;
    int y_block_num = 3;
    int wid = 0;
    int[] coor;
    TerrainDAO terrainDAO;

    Session createSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        th = new TerritoryDAO(session);
        terrainDAO = new TerrainDAO(session);
        coor = th.MillierConvertion(latitude, longitude);
        return session;
    }

    TerritoryHandlerTest() {

    }

//    @Test
    void testAll() {
        try {
            getTerritories();
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
            terrainDAO.initialTerrain();
            th.addTerritories(wid, latitude, longitude);
            List<Territory> res = th.getTerritories(wid, latitude, longitude);
//            session.close();
            MessagesS2C msg = new MessagesS2C();
            PositionResultMessage positionResultMessage = new PositionResultMessage();
            positionResultMessage.setTerritoryArray(res);
            msg.setPositionResultMessage(positionResultMessage);
            try {
                logger.info(objectMapper.writeValueAsString(msg));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }


    void getTerritories_virtual() {
        try (Session session = createSession()) {
            session.beginTransaction();
            terrainDAO.initialTerrain();
//            th.addTerritories(wid, x, y,x_block_num,y_block_num);
//            List<Territory> res = th.getTerritories(wid, x, y,x_block_num,y_block_num);

            List<Territory> res = th.handle(wid, x, y, x_block_num, y_block_num);
            printAsJson(res);
            res = th.handle(wid, x+10, y+10, x_block_num, y_block_num);
            printAsJson(res);
        }
    }

    void printAsJson(List<Territory> res) {
        MessagesS2C msg = new MessagesS2C();
        PositionResultMessage positionResultMessage = new PositionResultMessage();
        positionResultMessage.setTerritoryArray(res);
        msg.setPositionResultMessage(positionResultMessage);
        try {
            logger.info(objectMapper.writeValueAsString(msg));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    void updateTerritory() {
        try (Session session = createSession()) {
            session.beginTransaction();
            terrainDAO.initialTerrain();
            th.addTerritory(wid, coor[0], coor[1], "unexplored");
            th.updateTerritory(wid, coor[0], coor[1], "explored");
            assertEquals("explored", th.getTerritory(wid, coor[0], coor[1]).getStatus());
        }
    }

}
