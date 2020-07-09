package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.skill.Skill;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DAOTest {
    public static Session session;
    private PlayerDAO playerDAO;
    private SoldierDAO soldierDAO;
    private MonsterDAO monsterDAO;
    private UnitDAO unitDAO;
    private TerritoryDAO territoryDAO;

    @BeforeAll
    public static void setUpSession(){
        //System.out.println("executing beforeAll in DAOTest");
        session = HibernateUtil.getSessionFactory().openSession();

    }

    @AfterAll
    public static void closeSession(){
        //System.out.println("executing afterAll in DAOTest");

        session.close();
    }

    @BeforeEach
    public void setUp(){
        session.beginTransaction();
        playerDAO = new PlayerDAO(session);
        monsterDAO = new MonsterDAO(session);
        soldierDAO = new SoldierDAO(session);
        unitDAO = new UnitDAO(session);
        territoryDAO = new TerritoryDAO(session);

        playerDAO.addPlayer("testname","testpassword");
    }

    @AfterEach
    public void shutDown(){
        session.getTransaction().rollback();
    }

//    @Test
//    public void TestAll(){
//
//        testPlayerDAO();
//        testMonsterDAO();
//        testTerritoryDAO();
//        testSoldierUnitDAO();
//    }

    @Test
    public void testPlayerDAO(){
        //System.out.println("test playerDAO");

        Player p = playerDAO.getPlayer("testname");
        int id = p.getId();
        int wid = p.getWid();
        assertEquals(playerDAO.getPlayer(id).getUsername(),playerDAO.getPlayerByWid(wid).getUsername());
        assertEquals(playerDAO.getPlayer("testname","testpassword"), p);
        playerDAO.setStatus(p, Player.Status.INBATTLE);
        playerDAO.setCurrentCoord(p, new WorldCoord(wid,1,1));
        assertEquals(playerDAO.getPlayer(id).getCurrentCoord(),new WorldCoord(wid,1,1));
        assertEquals(playerDAO.getPlayer(id).getStatus(), Player.Status.INBATTLE);
    }

   @Test
    public void testMonsterDAO(){
        //System.out.println("test monsterDAO");

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
        assertEquals(monsterDAO.countMonstersInRange(coord1,4,4),2);
        List<Monster> DBmonsterList = monsterDAO.getMonstersInRange(new WorldCoord(1,0,0),4,4);
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

    @Test
    public void testSoldierUnitDAO(){
        //System.out.println("test soldierDAO");

        Player p = playerDAO.getPlayer("testname");
        int playerID = p.getId();
        List<Soldier> soldierList = soldierDAO.getSoldiers(playerID);
        int soldierID = soldierList.get(0).getId();
        Soldier soldier = soldierDAO.getSoldier(soldierID);
        Unit unit = unitDAO.getUnit(soldierID);
        assertEquals(soldier,soldierList.get(0));
        assertEquals(soldier.getHp(),soldierList.get(0).getHp());
        assertEquals(unit.getHp(), soldier.getHp());
        assertEquals(unit.getSpeed(), soldier.getSpeed());

        int hp = unit.getHp();
        unitDAO.setUnitHp(soldierID,hp-5);
        assertEquals(unitDAO.getUnit(soldierID).getHp(),hp-5);

        unitDAO.deleteUnit(soldierID);
        assertEquals(session.get(Unit.class, soldierID),null);

    }

    @Test
    public void testTerritoryDAO(){
        WorldCoord center = new WorldCoord(1,0,0);
        territoryDAO.addTerritory(new WorldCoord(1,-1,-1),90,"grass",null);
        territoryDAO.addTerritory(new WorldCoord(1,-1,0),90,"grass",null);
        territoryDAO.addTerritory(new WorldCoord(1,-1,1),90,"grass",null);
        territoryDAO.addTerritory(new WorldCoord(1,0,-1),90,"grass",null);
        territoryDAO.addTerritory(center,100,"grass",null);
        territoryDAO.addTerritory(new WorldCoord(1,0,1),90,"grass",null);
        territoryDAO.addTerritory(new WorldCoord(1,1,-1),90,"grass",null);
        territoryDAO.addTerritory(new WorldCoord(1,1,0),90,"grass",null);
        territoryDAO.addTerritory(new WorldCoord(1,1,1),95,"grass",null);
        Monster m = new Monster("testWolf",50,5,15);
        monsterDAO.addMonster(m,center);

        List<Territory> territoryList = territoryDAO.getTerritories(center,3,3);
        assertEquals(territoryList.size(),9);
        assertEquals(territoryList.get(0),territoryDAO.getTerritory(new WorldCoord(1,-1,-1)));

        WorldCoord wildestCoord = territoryDAO.getWildestCoordInRange(center,3,3);
        assertEquals(wildestCoord,new WorldCoord(1,1,1));

        territoryDAO.updateTameByRange(center,3,3,10,5);
        assertNotEquals(territoryDAO.getTameByCoord(new WorldCoord(1,-1,-1)),90);
        assertNotEquals(territoryDAO.getTameByCoord(new WorldCoord(1,-1,1)),90);
        assertNotEquals(territoryDAO.getTameByCoord(center),100);
        assertNotEquals(territoryDAO.getTameByCoord(new WorldCoord(1,1,1)),95);
        assertNotEquals(territoryDAO.getTameByCoord(new WorldCoord(1,1,-1)),90);

        territoryDAO.updateTerritory(center, 99);
        assertEquals(territoryDAO.getTameByCoord(center), 99);
    }

}
