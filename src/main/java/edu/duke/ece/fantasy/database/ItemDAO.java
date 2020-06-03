package edu.duke.ece.fantasy.database;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ItemDAO {
    Session session;

    public ItemDAO(Session session) {
        this.session = session;
    }

    public List<Item> getAllItem(){
        Query q = session.createQuery("SELECT I From Item I");
        return q.getResultList();
    }

    public Item getItem(String name) {
        Query q = session.createQuery("From Item I where I.name =:name");
        q.setParameter("name", name);
        Item res = (Item) q.uniqueResult();
        return res;
    }

    public void addItem(String name, int cost) {
        Item item = getItem(name);
        if (item == null) {
            item = new Item(name, cost);
            session.save(item);
        }
    }

    public void initial() {
        addItem("medicine",10);
    }
}
