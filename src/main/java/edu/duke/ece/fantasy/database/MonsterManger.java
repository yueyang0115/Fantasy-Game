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

    public synchronized void addMonster(Monster m, WorldCoord where){
        m.setCoord(where);
        session.save(m);
    }

    //get a monster from database based on the provided monsterID
    public synchronized Monster getMonster(int monsterID) {
        Query q = session.createQuery("From Monster M where M.id =:id");
        q.setParameter("id", monsterID);
        Monster res = (Monster) q.uniqueResult();
        return res;
    }

    //get all monsters in the provided coord from database
    public synchronized List<Monster> getMonsters(WorldCoord where){
        List<Monster> monsterList = new ArrayList<>();
        Query q = session.createQuery("From Monster M where M.coord =:coord");
        q.setParameter("coord", where);
        for(Object o : q.list()) {
            monsterList.add((Monster) o);
        }
        return monsterList;
    }

    //update a monster's hp
    public synchronized boolean setMonsterHp(int monsterID, int hp){
        Monster m = getMonster(monsterID);
        if (m == null) { // don't have that monster
            return false;
        }
        m.setHp(hp);
        session.update(m);
        return true;
    }

    //delete a monster from database
    public synchronized void deleteMonster(int monsterID){
        Monster monster;
        if ((monster = (Monster) session.get(Monster.class, monsterID)) != null) {
            session.delete(monster);
            System.out.println("[DEBUG] Delete monster " + monsterID);
        }
    }
    
    public synchronized void setMonsterStatus(int monsterID, boolean status){
        Monster m = getMonster(monsterID);
        if (m == null) { // don't have that monster
            return;
        }
        m.setNeedUpdate(status);
        session.update(m);
    }

    public synchronized void setMonstersStatus(List<Monster> monsterList, boolean status){
        for(Monster m : monsterList) setMonsterStatus(m.getId(), false);
    }

    public synchronized List<Monster> getUpdatedMonsters(){
        List<Monster> monsterList = new ArrayList<>();
        Query q = session.createQuery("From Monster M where M.needUpdate =:needUpdate");
        q.setParameter("needUpdate", true);
        for(Object o : q.list()) {
            monsterList.add((Monster) o);
        }
        return monsterList;
    }

    public synchronized Long countMonstersInRange(WorldCoord where, int x_range, int y_range){
        List<Monster> monsterList = new ArrayList<>();
        Query q = session.createQuery("select count(*) From Monster M where M.coord.wid =:wid and M.coord.x between :xlower and :xupper and M.coord.y between :ylower and :yupper");
        q.setParameter("wid", where.getWid());
        q.setParameter("xlower", where.getX() - x_range/2);
        q.setParameter("xupper", where.getX() + x_range/2);
        q.setParameter("ylower", where.getY() - y_range/2);
        q.setParameter("yupper", where.getY() + y_range/2);
        Long cnt = (Long) q.uniqueResult();
        return cnt;
    }

    public synchronized List<Monster> getMonstersInRange(WorldCoord where, int x_range, int y_range){
        List<Monster> monsterList = new ArrayList<>();
        Query q = session.createQuery("From Monster M where M.coord.wid =:wid and M.coord.x between :xlower and :xupper and M.coord.y between :ylower and :yupper");
        q.setParameter("wid", where.getWid());
        q.setParameter("xlower", where.getX() - x_range/2);
        q.setParameter("xupper", where.getX() + x_range/2);
        q.setParameter("ylower", where.getY() - y_range/2);
        q.setParameter("yupper", where.getY() + y_range/2);
        for(Object o : q.list()) {
            monsterList.add((Monster) o);
        }
        return monsterList;
    }

    public synchronized void updateMonsterCoord(int monsterID, int x, int y){
        Monster m = getMonster(monsterID);
        if(m != null){
            m.getCoord().setX(x);
            m.getCoord().setY(y);
            session.update(m);
        }
    }
}
