package edu.duke.ece.fantasy;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Territory;
import edu.duke.ece.fantasy.database.TerritoryDAO;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PositionUpdateHandlerTest {
    PositionUpdateHandler positionUpdateHandler;


    Session createSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        positionUpdateHandler = new PositionUpdateHandler(session);
        return session;
    }

    @Test
    void handle() {
        try(Session session = createSession()){
            (new Initializer()).initialize();
            session.beginTransaction();
            TerritoryHandlerTest th = new TerritoryHandlerTest();
            List<Territory> res = positionUpdateHandler.handle(1,-15,15,3,3);
            th.printAsJson(res);
        }
    }
}
