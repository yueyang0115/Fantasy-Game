package edu.duke.ece.fantasy.database;

import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import java.util.List;

public class shopInventoryDAO {
    Session session;

    public shopInventoryDAO(Session session) {
        this.session = session;
    }

//    public shopInventory addInventory(DBItem item, int amount, DBBuilding DBShop) {
//        shopInventory new_inventory = new shopInventory(item, amount, DBShop);
//        session.save(new_inventory);
//        return new_inventory;
//    }

    public void deleteInventory(WorldCoord coord) {
        Query q = session.createQuery("delete From shopInventory I where I.coord = :coord");
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

    public List<shopInventory> getInventories(WorldCoord coord) {
        TypedQuery<shopInventory> q = session.createQuery("From shopInventory I where I.coord=:coord", shopInventory.class);
        q.setParameter("coord", coord);
        return q.getResultList();
    }


}
