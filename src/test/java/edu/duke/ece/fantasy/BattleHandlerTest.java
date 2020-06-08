package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BattleHandlerTest {
    private Session session;
    private BattleHandler myBattleHandler;

//    public BattleHandlerTest(){
//        this.session = createSession();
//        myBattleHandler = new BattleHandler(session);
//    }
//
//    private Session createSession() {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        return session;
//    }
//
//    @Test
//    public void testAll(){
//        session.beginTransaction();
//
//        Queue q1 = generateUnitQueueTest();
//        rollUnitQueueTest(q1);
//        Queue q2 = generateUnitQueueTest();
//        generateIDListTest(q2);
//
//        session.getTransaction().commit();
//        //HibernateUtil.shutdown();
//    }
//
//    public Queue<Unit> generateUnitQueueTest(){
//        TerrainDAO terrainHandler = new TerrainDAO(session);
//        terrainHandler.initialTerrain();
//        Territory t = new Territory(0, 0, 0, "testexplored");
//        t.setTerrain(terrainHandler.getRandomTerrain());
//
//        Monster m1 = new Monster("testwolf",3,3,5);
//        Monster m2 = new Monster("testwolf",3,3,7);
//        Monster m3 = new Monster("testwolf",3,3,6);
//        t.addMonster(m1);
//        t.addMonster(m2);
//        t.addMonster(m3);
//        session.save(t);
//
//        List<Monster> monsterList = new ArrayList<>();
//        monsterList.add(m1);
//        monsterList.add(m2);
//        monsterList.add(m3);
//        List<Soldier> soldierList = new ArrayList<>();
//
//        Queue<Unit> q = myBattleHandler.generateUnitQueue(monsterList,soldierList);
//        assertEquals(q.poll().getSpeed(),7);
//        assertEquals(q.poll().getSpeed(),6);
//        assertEquals(q.poll().getSpeed(),5);
//
//        return myBattleHandler.generateUnitQueue(monsterList,soldierList);
//    }
//
//    public void rollUnitQueueTest(Queue<Unit> q){
//        q = myBattleHandler.rollUnitQueue(q,-1);
//        assertEquals(q.poll().getSpeed(),6);
//        assertEquals(q.poll().getSpeed(),5);
//        assertEquals(q.poll().getSpeed(),7);
//
//    }
//
//    public void generateIDListTest(Queue<Unit> q){
//        int firstID = q.peek().getId();
//        int queueSize = q.size();
//        List<Integer> unitIDList= myBattleHandler.generateIDList(q);
//        assertEquals(unitIDList.get(0),firstID);
//        assertEquals(queueSize,unitIDList.size());
//
//    }

}
