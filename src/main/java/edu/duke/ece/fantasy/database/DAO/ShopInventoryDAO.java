package edu.duke.ece.fantasy.database.DAO;

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

//    public shopInventory addInventory(Inventory inventory,WorldCoord where) {
//        shopInventory newInventory = new shopInventory(inventory.getDBItem(), inventory.getAmount(), where);
//        session.save(newInventory);
//        return newInventory;
//    }

    public void deleteInventory(WorldCoord coord) {
        Query<shopInventory> q = session.createQuery("delete From shopInventory I where I.coord = :coord", shopInventory.class);
        q.setParameter("coord", coord);
        q.executeUpdate();
    }

//    public Inventory getInventory(int owner_id, DBItem dbItem) {
//        Query q = session.createQuery("From shopInventory I where I.Shop =:owner_id and I.item=:item");
//        q.setParameter("owner_id", owner_id);
//        q.setParameter("item", dbItem);
//        Inventory res = (Inventory) q.uniqueResult();
//        return res;
//    }

    public List<Inventory> getInventories(WorldCoord coord) {
        TypedQuery<Inventory> q = session.createQuery("From shopInventory I where I.coord=:coord", Inventory.class);
        q.setParameter("coord", coord);
        return q.getResultList();
    }


}
