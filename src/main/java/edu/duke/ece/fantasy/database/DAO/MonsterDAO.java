package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.WorldCoord;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MonsterDAO {
    private Session session;

    public MonsterDAO(Session session) {
        this.session = session;
    }

    // add monster to the given coord
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
        for(Iterator<Object> iterator = q.list().iterator(); iterator.hasNext();){
            Object o = iterator.next();
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


    // change monster's needUpdate field to the given status
    public void setMonsterStatus(int monsterID, boolean status){
        Monster m = getMonster(monsterID);
        if (m == null) { // don't have that monster
            return;
        }
        m.setNeedUpdate(status);
        session.update(m);
    }

    // change given monsters' needUpdate field to the given status
    public void setMonstersStatus(List<Monster> monsterList, boolean status){
        //for(Monster m : monsterList) setMonsterStatus(m.getId(), false);
        for(Iterator<Monster> iterator = monsterList.iterator(); iterator.hasNext();){
            Monster m = iterator.next();
            setMonsterStatus(m.getId(), status);
        }
    }

    // count num of monsters within an area
    public Long countMonstersInRange(WorldCoord where, int x_range, int y_range){
        List<Monster> monsterList = new ArrayList<>();
        Query q = session.createQuery("select count(*) From Monster M where M.coord.wid =:wid"
                +" and M.coord.x >=:xlower and M.coord.x <=:xupper"
                +" and M.coord.y >=:ylower and M.coord.y <=:yupper"
        );
        q.setParameter("wid", where.getWid());
        q.setParameter("xlower", where.getX() - x_range/2);
        q.setParameter("xupper", where.getX() + x_range/2);
        q.setParameter("ylower", where.getY() - y_range/2);
        q.setParameter("yupper", where.getY() + y_range/2);
        Long cnt = (Long) q.uniqueResult();
        return cnt;
    }

    //get all monsters within an area, not including monsters that located in the center
    public List<Monster> getMonstersInRange(WorldCoord where, int x_range, int y_range){
        List<Monster> monsterList = new ArrayList<>();
        Query q = session.createQuery("From Monster M where M.coord.wid =:wid "
                +" and M.coord.x >=:xlower and M.coord.x <=:xupper"
                +" and M.coord.y >=:ylower and M.coord.y <=:yupper"
                +" and M.coord != :coord "
        );
        q.setParameter("wid", where.getWid());
        q.setParameter("coord", where);
        q.setParameter("xlower", where.getX() - x_range/2);
        q.setParameter("xupper", where.getX() + x_range/2);
        q.setParameter("ylower", where.getY() - y_range/2);
        q.setParameter("yupper", where.getY() + y_range/2);
//        for(Object o : q.list()) {
//            monsterList.add((Monster) o);
//        }
        for(Iterator iterator = q.list().iterator(); iterator.hasNext();){
            Object o = iterator.next();
            monsterList.add((Monster) o);
        }
        return monsterList;
    }

    // update monster's coord to the given x and y
    public void updateMonsterCoord(int monsterID, int x, int y){
        Monster m = getMonster(monsterID);
        if(m != null){
            m.getCoord().setX(x);
            m.getCoord().setY(y);
            session.update(m);
        }
    }
}
