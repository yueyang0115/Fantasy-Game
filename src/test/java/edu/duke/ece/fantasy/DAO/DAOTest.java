package edu.duke.ece.fantasy.DAO;

import edu.duke.ece.fantasy.database.DAO.MonsterDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.DAO.UnitDAO;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.WorldCoord;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DAOTest {
    public static Session session;

    @BeforeAll
    public static void setUpSession(){
        System.out.println("executing beforeAll in DAOTest");
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
    }

    @AfterAll
    public static void closeSession(){
        System.out.println("executing afterAll in DAOTest");
        session.getTransaction().rollback();
        session.close();
    }

    @Test
    public void TestAll(){
        testPlayerDAO();
        testMonsterDAO();
    }

    public void testPlayerDAO(){
        System.out.println("test playerDAO");
        PlayerDAO playerDAO = new PlayerDAO(this.session);

        playerDAO.addPlayer("testname","testpassword");
        Player p = playerDAO.getPlayer("testname");
        int id = p.getId();
        int wid = p.getWid();
        assertEquals(playerDAO.getPlayer(id).getUsername(),playerDAO.getPlayerByWid(wid).getUsername());
    }

    public void testMonsterDAO(){
        System.out.println("test monsterDAO");
        MonsterDAO monsterDAO = new MonsterDAO(this.session);

        Monster m1 = new Monster("wolf",60,6,10);
        Monster m2 = new Monster("wolf",70,7,20);
        WorldCoord coord1 = new WorldCoord(1,1,1);
        WorldCoord coord2 = new WorldCoord(1,1,2);
        monsterDAO.addMonster(m1,coord1);
        monsterDAO.addMonster(m2,coord2);

        Monster DBmonster1 = monsterDAO.getMonsters(coord1).get(0);
        int id = DBmonster1.getId();
        int id2 = monsterDAO.getMonsters(coord2).get(0).getId();
        assertEquals(monsterDAO.getMonster(id), DBmonster1);
        assertEquals(monsterDAO.countMonstersInRange(coord1,6,6),2);
        List<Monster> DBmonsterList = monsterDAO.getMonstersInRange(new WorldCoord(1,0,0),6,6);
        assertEquals(DBmonsterList.size(),2);

        monsterDAO.setMonsterHp(id,100);
        monsterDAO.setMonsterStatus(id,false);
        monsterDAO.setMonstersStatus(DBmonsterList,false);
        monsterDAO.updateMonsterCoord(id,-1,1);
        assertEquals(monsterDAO.getMonster(id).getHp(),100);
        assertEquals(monsterDAO.getMonster(id).getCoord(),new WorldCoord(1,-1,1));
        assertEquals(monsterDAO.getMonster(id).isNeedUpdate(),false);
        assertEquals(monsterDAO.getMonster(id2).isNeedUpdate(),false);
    }

}
