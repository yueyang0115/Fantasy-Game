package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Inventory;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.database.shopInventory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import java.util.List;

public class ShopInventoryDAO {

    Session session;

    public ShopInventoryDAO(Session session) {
        this.session = session;
    }

    public void addInventory(Inventory inventory) {
        session.save(inventory);
    }

    public void deleteInventory(WorldCoord coord) {
        Query<Inventory> q = session.createQuery("delete From shopInventory I where I.coord = :coord", Inventory.class);
        q.setParameter("coord", coord);
        q.executeUpdate();
    }

    public List<Inventory> getInventories(WorldCoord coord) {
        Query<Inventory> q = session.createQuery("From shopInventory I where I.coord=:coord", Inventory.class);
        q.setParameter("coord", coord);
        return q.getResultList();
    }


}
