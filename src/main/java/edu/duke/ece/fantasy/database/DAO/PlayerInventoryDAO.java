package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.Inventory;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.playerInventory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import java.util.List;

public class PlayerInventoryDAO {
    Session session;

    public PlayerInventoryDAO(Session session) {
        this.session = session;
    }

//    public Inventory getInventory(int owner_id, String name) {
//        Query q = session.createQuery("From playerInventory I where I.player =:owner_id and I.item_name=:name");
//        q.setParameter("owner_id", owner_id);
//        q.setParameter("name", name);
//        Inventory res = (Inventory) q.uniqueResult();
//        return res;
//    }

    public playerInventory getInventory(int id){
        Query<playerInventory> q = session.createQuery("From playerInventory I where I.id=:id",playerInventory.class);
        q.setParameter("id",id);
        return q.uniqueResult();
    }

    public List<Inventory> getInventories(Player player) {
        TypedQuery<Inventory> q = session.createQuery("From playerInventory I where I.player=:player order by I.id", Inventory.class);
        q.setParameter("player", player);
        return q.getResultList();
    }
}
