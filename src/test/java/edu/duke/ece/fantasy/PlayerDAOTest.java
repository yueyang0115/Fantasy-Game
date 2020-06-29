package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.DAO.MonsterDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Player;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

public class PlayerDAOTest {
    private PlayerDAO playerDAO;
    private Session session;

    public PlayerDAOTest() {
        this.session = HibernateUtil.getSessionFactory().openSession();
        this.playerDAO = new PlayerDAO(this.session);
    }

    @Test
    public void TestAll(){
        testGetPlayer();
    }

    public void testGetPlayer(){
        session.beginTransaction();
        playerDAO.addPlayer("testname","testpassword");
        Player p = playerDAO.getPlayer("testname");
        int id = p.getId();
        p = playerDAO.getPlayer(id);
        System.out.println(p.getWid());
        session.getTransaction().commit();
    }
}
