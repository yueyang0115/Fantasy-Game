package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.TerrainDAO;
import edu.duke.ece.fantasy.database.TerritoryDAO;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionUpdateHandlerTest {
    PositionUpdateHandler positionUpdateHandler;
    TerrainDAO terrainDAO;

    Session createSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        positionUpdateHandler = new PositionUpdateHandler(session);
        terrainDAO = new TerrainDAO(session);
        return session;
    }

    @Test
    void handle() {
        try(Session session = createSession()){
            session.beginTransaction();
            terrainDAO.initialTerrain();
            positionUpdateHandler.handle(1,-15,15,3,3);
        }
    }
}