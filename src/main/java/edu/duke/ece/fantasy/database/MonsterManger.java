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

    //get a monster from database based on the provided monsterID
    public Monster getMonster(int monsterID) {
        Query q = session.createQuery("From Monster M where M.id =:id");
        q.setParameter("id", monsterID);
        Monster res = (Monster) q.uniqueResult();
        return res;
    }

    //get all monsters in the provided territory from database
    public List<Monster> getMonsters(int territoryID){
        List<Monster> monsterList = new ArrayList<>();
        Query q = session.createQuery("From Monster M where M.territory.id =:territoryID");
        q.setParameter("territoryID", territoryID);
        for(Object o : q.list()) {
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
        }
    }
}
