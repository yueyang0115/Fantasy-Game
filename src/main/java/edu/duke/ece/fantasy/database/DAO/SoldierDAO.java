package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.Soldier;
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
        Query q = session.createQuery("From Soldier S where S.id =:id");
        q.setParameter("id", soldierID);
        Soldier res = (Soldier) q.uniqueResult();
        return res;
    }

    //get all soldiers a player has
    public List<Soldier> getSoldiers(int playerID){
        List<Soldier> soldierList = new ArrayList<>();
        Query q = session.createQuery("From Soldier S where S.player.id =:playerID");
        q.setParameter("playerID", playerID);
        for(Object o : q.list()) {
            soldierList.add((Soldier) o);
        }
        return soldierList;
    }

}
