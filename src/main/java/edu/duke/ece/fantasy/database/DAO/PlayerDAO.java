package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.*;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jasypt.util.password.BasicPasswordEncryptor;

import java.util.List;


public class PlayerDAO {
    private Session session;
    BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();


//    public PlayerDAO(Session session) {
//        this.session = session;
//    }

    public void addPlayer(String username, String password) {
        String encryptPassword = passwordEncryptor.encryptPassword(password);
        Player player = new Player(username, encryptPassword);

        //add two default soldier for each player
        Soldier soldier = new Soldier("wizard", 100, 5, 20);
//        Soldier soldier2 = new Soldier("soldier", 12, 3, 18);
//        Skill basicSkill = new Skill("ironball",2);
//        soldier.addSkill(basicSkill);
//        soldier2.addSkill(basicSkill);
        player.addSoldier(soldier);
//        player.addSoldier(soldier2);

        // add default money
        player.setMoney(2000);
        HibernateUtil.save(player);
    }


    public Player getPlayerByWid(int wid) {
//        Query<Player> q = session.createQuery("From Player U where U.curWorldId =:wid", Player.class);
//        q.setParameter("wid", wid);
//        session.
//        return q.uniqueResult();
        return HibernateUtil.queryOne("From Player U where U.curWorldId =:wid", Player.class, new String[]{"wid"}, new Object[]{wid});
    }

    public Player getPlayer(int id) {
//        Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
//        dbSession.beginTransaction();
//        Query<Player> q = HibernateUtil.getSessionFactory().getCurrentSession().createQuery("From Player U where U.id =:id", Player.class);
//        q.setParameter("id", id);
//        Player res = q.uniqueResult();
//        dbSession.getTransaction().commit();
//        return res;
        return HibernateUtil.queryOne("From Player U where U.id =:id", Player.class, new String[]{"id"}, new Object[]{id});
    }

    public Player getPlayer(String username) {
//        Session dbSession = HibernateUtil.getSessionFactory().getCurrentSession();
//        dbSession.beginTransaction();
//        Query<Player> q = HibernateUtil.getSessionFactory().getCurrentSession().createQuery("From Player U where U.username =:username", Player.class);
//        q.setParameter("username", username);
//        Player res = q.uniqueResult();
//        dbSession.getTransaction().commit();
//        return res;
        return HibernateUtil.queryOne("From Player U where U.username =:username", Player.class, new String[]{"username"}, new Object[]{username});
    }

    public Player getPlayer(String username, String password) {
        // select territory according to conditions
        Player res = getPlayer(username);
        if (res == null) {
            return null;
        }
        if (passwordEncryptor.checkPassword(password, res.getPassword())) {
            return res;
        } else {
            return null;
        }

    }

    // update player's status first in cache then in database
//    public void setStatus(Player p, String status){
//        p.setStatus(status);
//        session.update(p);
//    }
//
//    // update player's coord first in cache then in database
//    public void setCurrentCoord(Player p, WorldCoord currentCoord){
//        p.setCurrentCoord(currentCoord);
//        session.update(p);
//    }

    public void removeSoldier(int playerID, int soldierID) {
        Player p = getPlayer(playerID);
        Query q = session.createQuery("From Soldier S where S.id =:id");
        q.setParameter("id", soldierID);
        Soldier soldier = (Soldier) q.uniqueResult();
        p.getSoldiers().remove(soldier);
        session.update(p);
    }

    public void addWorld(int playerID, WorldInfo info) {
        Player player = getPlayer(playerID);
//        player.addWorldInfo(info);
        session.update(player);
    }

    public void setBattleInfo(int playerID, List<Integer> unitList){
        Player p = getPlayer(playerID);
        p.setBattleInfo(unitList);
        session.update(p);
    }

    public List<Integer> getBattleInfo(int playerID){
        Player p = getPlayer(playerID);
        return p.getBattleInfo();
    }
}
