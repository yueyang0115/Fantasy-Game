package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import org.hibernate.Session;

import static org.junit.jupiter.api.Assertions.*;

class UserHandlerTest {
    PlayerDAO playerDAO;

    Session createSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        playerDAO = new PlayerDAO(session);
        return session;
    }

//    @Test
    void testAll() {
        try {
            getUser();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            HibernateUtil.shutdown();
        }
    }


    void getUser() {
        try(Session session = createSession()){
            session.beginTransaction();
            Player user = playerDAO.getPlayer("1","1");
            if(user ==null){
                playerDAO.addPlayer("1","1");
            }
            user = playerDAO.getPlayer("1","1");
            session.getTransaction().commit();
            assertNotNull(user);
        }
    }
}