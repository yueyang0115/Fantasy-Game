package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.WorldCoord;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MonsterDAO {

    // add monster to the given coord
    public void addMonster(Monster m, WorldCoord where){
        m.setCoord(where);
        HibernateUtil.save(m);
    }

    //get a monster from database based on the provided monsterID
    public Monster getMonster(int monsterID) {
        Monster res = HibernateUtil.queryOne("From Monster M where M.id =:id",
                Monster.class, new String[]{"id"}, new Object[]{monsterID});
        return res;
    }

    //get all monsters in the provided coord from database
    public List<Monster> getMonsters(WorldCoord where){
        List<Monster> monsterList = HibernateUtil.queryList("From Monster M where M.coord =:coord",
                Monster.class, new String[]{"coord"}, new Object[]{where});
        return monsterList;
    }

    //update a monster's hp
    public boolean setMonsterHp(int monsterID, int hp){
        Monster m = getMonster(monsterID);
        if (m == null) { // don't have that monster
            return false;
        }
        m.setHp(hp);
        HibernateUtil.update(m);
        return true;
    }


    // change monster's needUpdate field to the given status
    public void setMonsterStatus(int monsterID, boolean status){
        Monster m = getMonster(monsterID);
        if (m == null) { // don't have that monster
            return;
        }
        m.setNeedUpdate(status);
        HibernateUtil.update(m);
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
    public int countMonstersInRange(WorldCoord where, int x_range, int y_range){
        List<Monster> monsterList = HibernateUtil.queryList(
                "From Monster M where M.coord.wid =:wid"
                        +" and M.coord.x >=:xlower and M.coord.x <=:xupper"
                        +" and M.coord.y >=:ylower and M.coord.y <=:yupper",
                Monster.class, new String[]{"wid", "xlower", "xupper", "ylower", "yupper"},
                new Object[]{where.getWid(), where.getX() - x_range/2, where.getX() + x_range/2,
                        where.getY() - y_range/2, where.getY() + y_range/2});

        int cnt = monsterList.size();
        return cnt;
    }

    //get all monsters within an area, not including monsters that located in the center
    public List<Monster> getMonstersInRange(WorldCoord where, int x_range, int y_range){
        List<Monster> monsterList = HibernateUtil.queryList(
                "From Monster M where M.coord.wid =:wid "
                        +" and M.coord.x >=:xlower and M.coord.x <=:xupper"
                        +" and M.coord.y >=:ylower and M.coord.y <=:yupper"
                        +" and M.coord != :coord ",
                Monster.class, new String[]{"wid", "coord", "xlower", "xupper", "ylower", "yupper"},
                new Object[]{where.getWid(), where, where.getX() - x_range/2, where.getX() + x_range/2,
                        where.getY() - y_range/2, where.getY() + y_range/2});
        return monsterList;
    }

    // update monster's coord to the given x and y
    public void updateMonsterCoord(int monsterID, int x, int y){
        Monster m = getMonster(monsterID);
        if(m != null){
            m.getCoord().setX(x);
            m.getCoord().setY(y);
            HibernateUtil.update(m);
        }
    }
}
