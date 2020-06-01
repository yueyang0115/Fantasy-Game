package edu.duke.ece.fantasy.database;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class SoldierManger {
    private Session session;

    public SoldierManger(Session session) {
        this.session = session;
    }

    public Soldier getSoldier(int id) {
        Query q = session.createQuery("From Monster M where M.id =:id");
        q.setParameter("id", id);
        Soldier res = (Soldier) q.uniqueResult();
        return res;
    }

    public boolean setSoldierHp(int id, int hp){
        Soldier s = getSoldier(id);
        if (s == null) { // don't have that monster
            return false;
        }
        s.setHp(hp);
        session.update(s);
        return true;
    }
}
