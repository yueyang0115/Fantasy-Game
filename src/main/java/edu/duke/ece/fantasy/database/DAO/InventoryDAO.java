package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Inventory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class InventoryDAO {

    public List<Inventory> getAllItemPack() {
        return HibernateUtil.queryList("SELECT I From Inventory I", Inventory.class, null, null);
    }

    public Inventory getInventory(int id) {
        return HibernateUtil.queryOne("From Inventory I where I.id =:id", Inventory.class,
                new String[]{"id"}, new Object[]{id});
    }

}
