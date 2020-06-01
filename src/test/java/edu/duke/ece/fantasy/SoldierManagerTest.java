package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import org.hibernate.Session;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SoldierManagerTest {
    private SoldierManger mySoldierManager;
    private Session session;

    public SoldierManagerTest(){
        this.session = createSession();
        this.mySoldierManager = new SoldierManger(this.session);
    }

    private Session createSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return session;
    }

    @Test
    public void testAll(){
        session.beginTransaction();
        Player player = initPlayer();
        getSoldierTest();
        getSoldiersTest(player);

        //session.getTransaction().commit();
        //session.close(); call session.close() will write data to database
        //HibernateUtil.shutdown();
    }

    public Player initPlayer(){
        String username = "TestName";
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        String password = passwordEncryptor.encryptPassword("TestPassword");
        Player player = new Player(username, password);

        Soldier soldier = new Soldier("soldier",49,1);
        player.addSoldier(soldier);

        session.save(player);

        Player savedplayer = (new UserHandler(session)).getUser(username);
        System.out.println("newly saved player has id " + savedplayer.getId());
        return savedplayer;
    }

    public void getSoldierTest(){
        Soldier soldier = mySoldierManager.getSoldier(1);
        assertNotNull(soldier);
    }

    public void getSoldiersTest(Player player){
        int playerID = player.getId();
        List<Soldier> soldierList = mySoldierManager.getSoldiers(playerID);
        System.out.println("Player with wid = " + playerID + " has " + soldierList.size() + " soldiers");
        assertNotEquals(soldierList.size(),0);
    }

}
