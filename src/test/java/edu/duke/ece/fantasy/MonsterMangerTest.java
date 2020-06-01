package edu.duke.ece.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.*;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MonsterMangerTest {
    private MonsterManger myMonsterManger;
    private Session session;

    public MonsterMangerTest(){
        this.session = createSession();
        this.myMonsterManger = new MonsterManger(this.session);
    }

    private Session createSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return session;
    }

    @Test
    public void testAll(){
        initMonster();
        getMonsterTest();
        setMonsterHpTest();

        //session.getTransaction().commit();
        //session.close(); call session.close() will write data to database
        HibernateUtil.shutdown();
    }

    public void initMonster(){
        session.beginTransaction();
        TerrainHandler terrainHandler = new TerrainHandler(session);
        terrainHandler.initialTerrain();

        Territory t = new Territory(1111, 222, 3333, "explored");
        Monster m = new Monster("wolf", 100, 10);
        t.addMonster(m);
        t.setTerrain(terrainHandler.getRandomTerrain());
        session.save(t);

        Long count = (Long) session.createQuery("select count(*) from Monster ").uniqueResult();
        System.out.println(count.intValue());
    }

    public void getMonsterTest(){
        Monster m = myMonsterManger.getMonster(1);
        assertNotNull(m);
        assertEquals(m.getType(),"wolf");
        assertEquals(m.getAtk(),10);
        assertEquals(m.getHp(),100);
        assertEquals(m.getTerritory().getWid(),1111);
    }

    public void setMonsterHpTest(){
        myMonsterManger.setMonsterHp(1,90);
        Monster m = myMonsterManger.getMonster(1);
        assertEquals(m.getType(),"wolf");
        assertEquals(m.getAtk(),10);
        assertEquals(m.getHp(),90);
        assertEquals(m.getTerritory().getWid(),1111);

    }


}
