package edu.duke.ece.fantasy.DAO;

import edu.duke.ece.fantasy.database.DAO.MonsterDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Player;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PlayerDAOTest {
    private PlayerDAO playerDAO;
    public static Session session;

    public PlayerDAOTest() {
//        this.session = HibernateUtil.getSessionFactory().openSession();
//
    }

    @BeforeAll
    public static void setUpSession(){
        System.out.println("executing beforeAll in playerDAO");
        session = HibernateUtil.getSessionFactory().openSession();
    }

    @AfterAll
    public static void closeSession(){
        System.out.println("executing afterAll in playerDAO");
        session.close();
    }

    @Test
    public void TestAll(){
        testGetPlayer();
    }

    public void testGetPlayer(){
        session.beginTransaction();
        this.playerDAO = new PlayerDAO(this.session);
        playerDAO.addPlayer("testname","testpassword");
        Player p = playerDAO.getPlayer("testname");
        int id = p.getId();
        p = playerDAO.getPlayer(id);
        System.out.println(p.getWid());
        session.getTransaction().commit();
    }
}
