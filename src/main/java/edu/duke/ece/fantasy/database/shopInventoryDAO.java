package edu.duke.ece.fantasy.database;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class shopInventoryDAO implements IInventoryDAO {
    Session session;

    public shopInventoryDAO(Session session) {
        this.session = session;
    }

    @Override
    public Inventory getInventory(int owner_id, String name) {
        Query q = session.createQuery("From shopInventory I where I.shop =:owner_id and I.item_name=:name");
        q.setParameter("owner_id", owner_id);
        q.setParameter("name", name);
        Inventory res = (Inventory) q.uniqueResult();
        return res;
    }
}
