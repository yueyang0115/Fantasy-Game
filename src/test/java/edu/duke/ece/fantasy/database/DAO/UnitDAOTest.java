package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.Soldier;
import edu.duke.ece.fantasy.database.Unit;
import edu.duke.ece.fantasy.database.levelUp.TableInitializer;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnitDAOTest {
    public static Session session;
    private PlayerDAO playerDAO;
    private SoldierDAO soldierDAO;
    private UnitDAO unitDAO;

    @BeforeAll
    public static void setUpSession(){
        session = HibernateUtil.getSessionFactory().openSession();
    }

    @AfterAll
    public static void closeSession(){
        session.close();
    }

    @BeforeEach
    public void setUp(){
        session.beginTransaction();
        playerDAO = new PlayerDAO(session);
        soldierDAO = new SoldierDAO(session);
        unitDAO = new UnitDAO(session);
        playerDAO.addPlayer("testname","testpassword");
    }

    @AfterEach
    public void shutDown(){
        //session.getTransaction().commit();
        session.getTransaction().rollback();
    }

    @Test
    public void testSoldierUnitDAO(){
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
    }

    @Test
    public void testDelete(){
//        Player p = playerDAO.getPlayer("testname");
//        int playerID = p.getId();
//        List<Soldier> soldierList = soldierDAO.getSoldiers(playerID);
//        int soldierID = soldierList.get(0).getId();
//
//        unitDAO.deleteUnit(soldierID);
//        assertEquals(session.get(Unit.class, soldierID),null);
    }
}
