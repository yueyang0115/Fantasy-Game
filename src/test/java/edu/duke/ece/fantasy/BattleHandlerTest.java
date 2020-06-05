package edu.duke.ece.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.*;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BattleHandlerTest {
    private Session session;
    private BattleHandler bh;

    public BattleHandlerTest(){
        this.session = createSession();
        bh = new BattleHandler(session);
    }

    private Session createSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return session;
    }

    @Test
    public void testAll(){
        session.beginTransaction();
        rollUnitQueueTest();
        //session.getTransaction().commit();
        //HibernateUtil.shutdown();
    }

    public void rollUnitQueueTest(){

    }

}
