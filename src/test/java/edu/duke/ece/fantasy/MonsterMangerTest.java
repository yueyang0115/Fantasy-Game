package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

public class MonsterMangerTest {
    private MonsterManger myMonsterManger;
    private Session session;
    private TerritoryDAO territoryDAO;
    private int wid = -3;
    private int x = 5;
    private int y = 5;


    public MonsterMangerTest(){
        this.session = createSession();
        this.myMonsterManger = new MonsterManger(this.session);
        this.territoryDAO = new TerritoryDAO(session);
    }

    private Session createSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return session;
    }

    @Test
    public void testAll(){
        initMonster();
        //getMonsterTest();
        getMonstersTest();
        setMonsterHpTest();
        session.close();
        //session.getTransaction().commit();
        //session.close(); call session.close() will write data to database
        //HibernateUtil.shutdown();
    }

    public void initMonster(){
        session.beginTransaction();
        
        Territory t = new Territory(new WorldCoord(wid, x, y), 100);
        Monster m = new Monster("wolf", 97, 10,10);
        Monster m2 = new Monster("wolf", 98, 10,10);
        //t.addMonster(m);
        //t.addMonster(m2);

        t.setTerrainType("grass");
        session.save(t);
//        session.getTransaction().commit();
        Long count = (Long) session.createQuery("select count(*) from Monster ").uniqueResult();
        System.out.println("Monster num in database is "+count.intValue());
    }

    public void getMonsterTest(){
    }

    public void getMonstersTest(){
      Territory territory = territoryDAO.getTerritory(new WorldCoord(wid,x,y));
      //List<Monster> monsterList = myMonsterManger.getMonsters(territory.getId());
      //System.out.println("Monster num in territoryID = 1 is "+monsterList.size());

        //test getMonater
      //Monster monster = myMonsterManger.getMonster(monsterList.get(0).getId());
      //assertNotNull(monster);
    }

    public void setMonsterHpTest(){
      //myMonsterManger.setMonsterHp(1,90);
      //Monster m = myMonsterManger.getMonster(1);
      //assertEquals(m.getHp(),90);
    }

}
