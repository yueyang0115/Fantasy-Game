package edu.duke.ece.fantasy.database;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class MonsterManger {
    private Session session;

    public MonsterManger(Session session) {
        this.session = session;
    }

    public void addMonster(Monster m, WorldCoord where){
        m.setCoord(where);
        session.save(m);
    }

    //get a monster from database based on the provided monsterID
    public Monster getMonster(int monsterID) {
        Query q = session.createQuery("From Monster M where M.id =:id");
        q.setParameter("id", monsterID);
        Monster res = (Monster) q.uniqueResult();
        return res;
    }

    public Monster getMonsterWhere(WorldCoord where) {
        Query q = session.createQuery("From Monster M where M.coord.wid =:wid and M.coord.x =:x and M.coord.y = :y");
        q.setParameter("wid", where.getWid());
        q.setParameter("x", where.getX());
        q.setParameter("y", where.getY());
//        q.setParameter("x", x);
//        q.setParameter("y", y);
        Monster res = (Monster) q.uniqueResult();
        return res;

    }

    //get all monsters in the provided coord from database
    public List<Monster> getMonsters(WorldCoord where){
        List<Monster> monsterList = new ArrayList<>();
        Query q2 = session.createQuery("From Monster M where M.coord.x =:x and M.coord.y =:y");
        q2.setParameter("x", where.getX());
        q2.setParameter("y", where.getY());
        for(Object o : q2.list()) {
            monsterList.add((Monster) o);
        }
        return monsterList;
    }

    //update a monster's hp
    public boolean setMonsterHp(int monsterID, int hp){
        Monster m = getMonster(monsterID);
        if (m == null) { // don't have that monster
            return false;
        }
        m.setHp(hp);
        session.update(m);
        return true;
    }

    //delete a monster from database
    public void deleteMonster(int monsterID){
        Monster monster;
        if ((monster = (Monster) session.get(Monster.class, monsterID)) != null) {
            session.delete(monster);
            System.out.println("[DEBUG] Delete monster " + monsterID);
        }
    }
}
