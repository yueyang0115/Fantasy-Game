package edu.duke.ece.fantasy.database;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Random;

public class ShopDAO {
    Session session;

    public ShopDAO(Session session) {
        this.session = session;
    }

    public Shop addShop(String name){
        Shop shop = getShop(name);
        if (shop == null) {
            shop = new Shop(name);
            session.save(shop);
        }
        return shop;
    }

    public Shop getShop(String name) {
        Query q = session.createQuery("From Shop s where s.name =:name");
        q.setParameter("name", name);
        Shop res = (Shop) q.uniqueResult();
        return res;
    }

    public Shop getShop(int id) {
        Query q = session.createQuery("From Shop s where s.id =:id");
        q.setParameter("id", id);
        Shop res = (Shop) q.uniqueResult();
        return res;
    }

    public Shop getRandomShop() {
        Long count = (Long) session.createQuery("select count(*) from Shop ").uniqueResult();
        Random rand = new Random();
        int rand_id = rand.nextInt(count.intValue()) + 1;
        Shop res = session.get(Shop.class, rand_id);
        return res;
    }

    public void initialShop(List<Item> items) {
        Shop shop = addShop("shop");
        for (Item item:items){
            shop.addInventory(item);
        }
    }
}
