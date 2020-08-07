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
    Session session;

    public PlayerInventoryDAO(Session session) {
        this.session = session;
    }

    public playerInventory getInventory(int id) {
        Query<playerInventory> q = session.createQuery("From playerInventory I where I.id=:id", playerInventory.class);
        q.setParameter("id", id);
        return q.uniqueResult();
    }

    public List<Inventory> getInventories(Player player) {
        Query<Inventory> q = session.createQuery("From playerInventory I where I.player=:player order by I.id", Inventory.class);
        q.setParameter("player", player);
        return q.getResultList();
    }
}
