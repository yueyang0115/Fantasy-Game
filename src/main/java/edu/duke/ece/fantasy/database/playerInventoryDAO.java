package edu.duke.ece.fantasy.database;

import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import java.util.List;

public class playerInventoryDAO {
    Session session;

    public playerInventoryDAO(Session session) {
        this.session = session;
    }

//    public Inventory getInventory(int owner_id, String name) {
//        Query q = session.createQuery("From playerInventory I where I.player =:owner_id and I.item_name=:name");
//        q.setParameter("owner_id", owner_id);
//        q.setParameter("name", name);
//        Inventory res = (Inventory) q.uniqueResult();
//        return res;
//    }

    public List<playerInventory> getInventories(Player player) {
        TypedQuery<playerInventory> q = session.createQuery("From playerInventory I where I.player=:player order by I.id", playerInventory.class);
        q.setParameter("player", player);
        return q.getResultList();
    }
}
