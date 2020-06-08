package edu.duke.ece.fantasy.database;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ConsumableDAO {
    Session session;

    public ConsumableDAO(Session session) {
        this.session = session;
    }

    public List<Consumable> getAllConsumable(){
        Query q = session.createQuery("SELECT I From Consumable I");
        return q.getResultList();
    }

    public Consumable getConsumable(String name) {
        Query q = session.createQuery("From Consumable I where I.name =:name");
        q.setParameter("name", name);
        Consumable res = (Consumable) q.uniqueResult();
        return res;
    }

    public void addConsumable(String name, int cost, int hp) {
        Consumable item = getConsumable(name);
        if (item == null) {
            item = new Consumable(name, cost,hp);
            session.save(item);
        }
    }

    public void initial() {
        addConsumable("medicine",10,20);
    }
}
