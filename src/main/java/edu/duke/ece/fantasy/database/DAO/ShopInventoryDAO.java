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

    public void addInventory(Inventory inventory) {
        HibernateUtil.save(inventory);
    }

    public void deleteInventory(WorldCoord coord) {
        HibernateUtil.execute("delete From shopInventory I where I.coord = :coord","coord",coord);
    }

//    public Inventory getInventory(int owner_id, DBItem dbItem) {
//        Query q = session.createQuery("From shopInventory I where I.Shop =:owner_id and I.item=:item");
//        q.setParameter("owner_id", owner_id);
//        q.setParameter("item", dbItem);
//        Inventory res = (Inventory) q.uniqueResult();
//        return res;
//    }

    public List<Inventory> getInventories(WorldCoord coord) {
        return HibernateUtil.queryList("From shopInventory I where I.coord=:coord", Inventory.class,
                new String[]{"coord"}, new Object[]{coord});
    }


}
