package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.Soldier;
import edu.duke.ece.fantasy.database.Unit;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class SoldierDAO {
    private Session session;

    public SoldierDAO(Session session) {
        this.session = session;
    }

    //get a soldier from database based on the provided soldierID
    public Soldier getSoldier(int soldierID) {
        Query<Soldier> q = session.createQuery("From Soldier S where S.id =:id",
                Soldier.class);
        q.setParameter("id", soldierID);
        return q.uniqueResult();
    }

    //get all soldiers a player has
    public List<Soldier> getSoldiers(int playerID) {
        Query<Soldier> q = session.createQuery("From Soldier S where S.player.id =:playerID",
                Soldier.class);
        q.setParameter("playerID", playerID);
        List<Soldier> soldierList = new ArrayList<>();
        for(Object o : q.list()) {
            soldierList.add((Soldier) o);
        }
        return soldierList;
    }

}
