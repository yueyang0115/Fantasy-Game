package edu.duke.ece.fantasy.database;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ItemPackDAO {
    Session session;

    public ItemPackDAO(Session session) {
        this.session = session;
    }

    public List<ItemPack> getAllItemPack() {
        Query q = session.createQuery("SELECT I From ItemPack I");
        return q.getResultList();
    }

    public void addItemPack(Item item, int amount) {
        ItemPack itemPack = new ItemPack(item, amount);
        session.save(itemPack);
    }

    public void updateItemPack(ItemPack itemPack) {
        session.update(itemPack);
    }

    public ItemPack getItemPack(int id) {
        Query q = session.createQuery("From ItemPack I where I.id =:id");
        q.setParameter("id", id);
        ItemPack res = (ItemPack) q.uniqueResult();
        return res;
    }

}
