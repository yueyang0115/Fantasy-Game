package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.HibernateUtil;
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
    public SoldierDAO() {}

    //get a soldier from database based on the provided soldierID
    public Soldier getSoldier(int soldierID) {
        Soldier s = HibernateUtil.queryOne("From Soldier S where S.id =:id",
                Soldier.class, new String[]{"id"}, new Object[]{soldierID});
        return s;
    }

    //get all soldiers a player has
    public List<Soldier> getSoldiers(int playerID){
        List<Soldier> soldierList =  HibernateUtil.queryList("From Soldier S where S.player.id =:playerID",
                Soldier.class, new String[]{"playerID"}, new Object[]{playerID});
        return soldierList;
    }

}
