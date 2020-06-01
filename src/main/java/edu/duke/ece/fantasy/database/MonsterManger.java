package edu.duke.ece.fantasy.database;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class MonsterManger {
    private Session session;

    public MonsterManger(Session session) {
        this.session = session;
    }

    public Monster getMonster(int id) {
        Query q = session.createQuery("From Monster M where M.id =:id");
        q.setParameter("id", id);
        Monster res = (Monster) q.uniqueResult();
        return res;
    }

    public boolean setMonsterHp(int id, int hp){
        Monster m = getMonster(id);
        if (m == null) { // don't have that monster
            return false;
        }
        m.setHp(hp);
        session.update(m);
        return true;
    }
}
