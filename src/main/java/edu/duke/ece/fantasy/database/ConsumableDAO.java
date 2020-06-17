package edu.duke.ece.fantasy.database;

import edu.duke.ece.fantasy.Item.Consumable;
import edu.duke.ece.fantasy.Item.Item;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ConsumableDAO extends ItemDAO {

    public ConsumableDAO(Session session) {
        super(session);
    }

//    public List<Consumable> getAllConsumable() {
//        Query q = session.createQuery("SELECT I From Consumable I");
//        return q.getResultList();
//    }
//
//    public void addConsumable(String name, int cost, int hp) {
//        Item item = getItem(name);
//        if (item == null) {
//            item = new Consumable(name, cost, hp);
//            session.save(item);
//        }
//    }
//
//    public void initial() {
//        addConsumable("medicine", 10, 20);
//    }
}
