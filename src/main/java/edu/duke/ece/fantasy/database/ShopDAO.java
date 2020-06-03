package edu.duke.ece.fantasy.database;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class ShopDAO {
    Session session;

    public ShopDAO(Session session) {
        this.session = session;
    }
//
//    public void addShop(String ){
//
//    }
//
//    public Terrain getShop(int id) {
//        Query q = session.createQuery("From Terrain T where T.type =:type");
//        q.setParameter("type", type);
//        Terrain res = (Terrain) q.uniqueResult();
//        return res;
//    }
//
//    public void initialShop() {
//        addTerrain("grass");
//        addTerrain("river");
//        addTerrain("mountain");
//    }
}
