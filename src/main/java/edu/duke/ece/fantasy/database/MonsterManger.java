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

    //get all monsters in the provided coord from database
    public List<Monster> getMonsters(WorldCoord where){
        List<Monster> monsterList = new ArrayList<>();
        Query q = session.createQuery("From Monster M where M.coord =:coord");
        q.setParameter("coord", where);
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
            System.out.println("[DEBUG] Delete monster " + monsterID);
        }
    }
    
    public void setMonsterStatus(int monsterID, boolean status){
        Monster m = getMonster(monsterID);
        if (m == null) { // don't have that monster
            return;
        }
        m.setNeedUpdate(status);
        session.update(m);
    }

    public void setMonstersStatus(List<Monster> monsterList, boolean status){
        for(Monster m : monsterList) setMonsterStatus(m.getId(), false);
    }

    public List<Monster> getUpdatedMonsters(){
        List<Monster> monsterList = new ArrayList<>();
        Query q = session.createQuery("From Monster M where M.needUpdate =:needUpdate");
        q.setParameter("needUpdate", true);
        for(Object o : q.list()) {
            monsterList.add((Monster) o);
        }
        return monsterList;
    }
}
