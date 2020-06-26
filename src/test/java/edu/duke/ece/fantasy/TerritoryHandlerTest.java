package edu.duke.ece.fantasy;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.DAO.TerritoryDAO;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    Session createSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        th = new TerritoryDAO(session);
        coor = th.MillierConvertion(latitude, longitude);
        return session;
    }

    TerritoryHandlerTest() {

    }

//    @Test
    void testAll() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            HibernateUtil.shutdown();
        }
    }

//    void getTerritories() {
//        try (Session session = createSession()) {
//            session.beginTransaction();
//            terrainDAO.initialTerrain();
//            th.addTerritories(wid, latitude, longitude);
//            List<Territory> res = th.getTerritories(wid, latitude, longitude);
////            session.close();
//            MessagesS2C msg = new MessagesS2C();
//            PositionResultMessage positionResultMessage = new PositionResultMessage();
//            positionResultMessage.setTerritoryArray(res);
//            msg.setPositionResultMessage(positionResultMessage);
//            try {
//                logger.info(objectMapper.writeValueAsString(msg));
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
//    }


}
