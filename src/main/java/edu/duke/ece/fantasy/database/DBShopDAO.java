//package edu.duke.ece.fantasy.database;
//
//import edu.duke.ece.fantasy.Item.Potion;
//import org.hibernate.Session;
//import org.hibernate.query.Query;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class DBShopDAO {
//    Session session;
//    ItemDAO itemDAO;
//
//    public DBShopDAO(Session session) {
//        this.session = session;
//        itemDAO = new ItemDAO(session);
//    }
//
//    public DBShopDAO(){
//
//    }
//
//    public DBShop createShop() {
//        List<shopInventory> shopInventories = new ArrayList<>();
//        DBShop DBShop = new DBShop();
//        shopInventories.add(new shopInventory(new Potion().toDBItem(), 20, DBShop));
//        DBShop.setItems(shopInventories);
//        return DBShop;
//    }
//
//    public DBShop addShop(WorldCoord where, DBShop DBShop) {
//        DBShop.setCoord(where);
//        session.saveOrUpdate(DBShop);
//        return DBShop;
//    }
//
//    public DBShop getShop(WorldCoord coord) {
//        Query q = session.createQuery("From DBShop s where s.coord =:coord");
//        q.setParameter("coord", coord);
//        DBShop res = (DBShop) q.uniqueResult();
//        return res;
//    }
//
//    public DBShop getShop(int id) {
//        Query q = session.createQuery("From DBShop s where s.id =:id");
//        q.setParameter("id", id);
//        DBShop res = (DBShop) q.uniqueResult();
//        return res;
//    }
//
//    public DBShop getRandomShop() {
//        Long count = (Long) session.createQuery("select count(*) from DBShop ").uniqueResult();
//        Random rand = new Random();
//        int rand_id = rand.nextInt(count.intValue()) + 1;
//        DBShop res = session.get(DBShop.class, rand_id);
//        return res;
//    }
//
////    public void initialShop(List<Item> items) {
////        Shop shop = addShop("shop");
////        for (Item item:items){
////            shop.addInventory(item);
////        }
////    }
//}
