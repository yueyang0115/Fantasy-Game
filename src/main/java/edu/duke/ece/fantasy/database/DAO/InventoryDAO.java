package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Inventory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class InventoryDAO {
    Session session;

    public InventoryDAO(Session session) {
        this.session = session;
    }

    public List<Inventory> getAllItemPack() {
        Query<Inventory> q = session.createQuery("SELECT I From Inventory I", Inventory.class);
        return q.getResultList();
    }

    public Inventory getInventory(int id) {
        Query<Inventory> q = session.createQuery("From Inventory I where I.id =:id", Inventory.class);
        q.setParameter("id", id);
        return q.uniqueResult();
    }

}
