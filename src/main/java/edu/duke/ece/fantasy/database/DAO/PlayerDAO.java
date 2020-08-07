package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.*;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jasypt.util.password.BasicPasswordEncryptor;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;


public class PlayerDAO {
    private Session session;
    BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();


    public PlayerDAO(Session session) {
        this.session = session;
    }

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
        session.save(player);
    }


    public Player getPlayerByWid(int wid) {
        Query<Player> q = session.createQuery("From Player U where U.curWorldId =:wid", Player.class);
        q.setParameter("wid", wid);
        return q.uniqueResult();
    }

    public Player getPlayer(int id) {
        Query<Player> q = session.createQuery("From Player U where U.id =:id", Player.class);
        q.setParameter("id", id);
        return q.uniqueResult();
    }

    public Player getPlayer(String username) {
        Query<Player> q = session.createQuery("From Player U where U.username =:username", Player.class);
        q.setParameter("username", username);
        return q.uniqueResult();
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


    public void removeSoldier(int playerID, int soldierID) {
        Player p = getPlayer(playerID);
        Query<Soldier> q = session.createQuery("From Soldier S where S.id =:id",
                Soldier.class);
        q.setParameter("id", soldierID);
        Soldier soldier = q.uniqueResult();
        p.getSoldiers().remove(soldier);
        session.update(p);
    }


    public void setBattleInfo(int playerID, List<Integer> unitList) {
        Player p = getPlayer(playerID);
        p.setBattleInfo(unitList);
        session.update(p);
    }

    public List<Integer> getBattleInfo(int playerID) {
        Query<Player> q = session.createQuery("From Player U where U.id =:id", Player.class);
        q.setParameter("id", playerID);
        Player player = q.uniqueResult();
        return player.getBattleInfo();
    }
}
