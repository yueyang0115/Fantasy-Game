package edu.duke.ece.fantasy.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.Item.Consumable;
import edu.duke.ece.fantasy.Item.Item;
import edu.duke.ece.fantasy.Item.Potion;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShopDAO {
    Session session;
    ItemDAO itemDAO;

    public ShopDAO(Session session) {
        this.session = session;
        itemDAO = new ItemDAO(session);
    }

    public Shop createShop() {
//        List<Item> items = new ArrayList<>();
//        items.add(new Consumable("Potion", 10, 20));

        List<shopInventory> shopInventories = new ArrayList<>();
        Shop shop = new Shop();
//        for (Item item : items) {
        shopInventories.add(new shopInventory(new Consumable("Potion", 10, 20).toDBItem(), 20, shop));
//        }
        shop.setItems(shopInventories);

        session.save(shop);
        return shop;
    }

    public Shop addShop(int territory_id, List<shopInventory> items) {
        Shop shop = getShopByTerritoryID(territory_id);
        if (shop == null) {
            shop = new Shop("shop");
//            shop.setInventory(items);
            for (shopInventory item : items) {
                shop.addInventory(item);
            }
            session.save(shop);
        }
        return shop;
    }

    public Shop addShop(String name) {
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

    public Shop getShopByTerritoryID(int territory_id) {
        Query q = session.createQuery("From Shop s where s.territory =:id");
        q.setParameter("id", territory_id);
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

//    public void initialShop(List<Item> items) {
//        Shop shop = addShop("shop");
//        for (Item item:items){
//            shop.addInventory(item);
//        }
//    }
}
