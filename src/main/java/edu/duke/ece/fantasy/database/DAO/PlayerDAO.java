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
        return HibernateUtil.queryOne("From Player U where U.curWorldId =:wid", Player.class, new String[]{"wid"}, new Object[]{wid});
    }

    public Player getPlayer(int id) {
        return HibernateUtil.queryOne("From Player U where U.id =:id", Player.class, new String[]{"id"}, new Object[]{id});
    }

    public Player getPlayer(String username) {
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
        Soldier soldier = HibernateUtil.queryOne("From Soldier S where S.id =:id",
                Soldier.class, new String[]{"id"}, new Object[]{soldierID});
        p.getSoldiers().remove(soldier);
        HibernateUtil.update(p);
    }

    public void addWorld(int playerID, WorldInfo info) {
        Player player = getPlayer(playerID);
//        player.addWorldInfo(info);
        HibernateUtil.update(player);
    }

    public void setBattleInfo(int playerID, List<Integer> unitList){
        Player p = getPlayer(playerID);
        p.setBattleInfo(unitList);
        HibernateUtil.update(p);
    }

    public List<Integer> getBattleInfo(int playerID){
        Player p = getPlayer(playerID);
        List<Integer> res = p.getBattleInfo();
        return res;
    }
}
