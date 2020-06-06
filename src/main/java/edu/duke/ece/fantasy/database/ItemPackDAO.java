package edu.duke.ece.fantasy.database;

import org.hibernate.Session;

public class ItemPackDAO {
    Session session;

    public ItemPackDAO(Session session) {
        this.session = session;
    }

    public void addItemPack(Item item, int amount) {
        ItemPack itemPack = new ItemPack(item,amount);
        session.save(itemPack);
    }

    public void updateItemPack(ItemPack itemPack){
        session.update(itemPack);
    }

}
