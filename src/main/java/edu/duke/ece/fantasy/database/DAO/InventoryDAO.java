package edu.duke.ece.fantasy.database.DAO;

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
        Query q = session.createQuery("SELECT I From Inventory I");
        return q.getResultList();
    }


    public Inventory getInventory(int id) {
        Query q = session.createQuery("From Inventory I where I.id =:id");
        q.setParameter("id", id);
        Inventory res = (Inventory) q.uniqueResult();
        return res;
    }

}
