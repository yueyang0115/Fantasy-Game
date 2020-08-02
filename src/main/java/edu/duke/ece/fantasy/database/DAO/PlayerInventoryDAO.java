package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Inventory;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.playerInventory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import java.util.List;

public class PlayerInventoryDAO {

//    public Inventory getInventory(int owner_id, String name) {
//        Query q = session.createQuery("From playerInventory I where I.player =:owner_id and I.item_name=:name");
//        q.setParameter("owner_id", owner_id);
//        q.setParameter("name", name);
//        Inventory res = (Inventory) q.uniqueResult();
//        return res;
//    }

    public playerInventory getInventory(int id) {
        return HibernateUtil.queryOne("From playerInventory I where I.id=:id", playerInventory.class,
                new String[]{"id"}, new Object[]{id});
    }

    public List<Inventory> getInventories(Player player) {
        return HibernateUtil.queryList("From playerInventory I where I.player=:player order by I.id", Inventory.class,
                new String[]{"player"}, new Object[]{player});
    }
}
