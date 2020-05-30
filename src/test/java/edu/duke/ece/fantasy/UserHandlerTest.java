package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Player;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserHandlerTest {
    UserHandler userHandler;

    Session createSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        userHandler = new UserHandler(session);
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
            Player user = userHandler.getUser("1","1");
            if(user ==null){
                userHandler.addUser("1","1");
            }
            user = userHandler.getUser("1","1");
            session.getTransaction().commit();
            assertNotNull(user);
        }
    }
}