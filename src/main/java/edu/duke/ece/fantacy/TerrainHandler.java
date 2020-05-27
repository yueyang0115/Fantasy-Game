package edu.duke.ece.fantacy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Random;

public class TerrainHandler {
    Session session;

    TerrainHandler(Session session) {
        this.session = session;
    }

    public void addTerrain(String type) {
        session.beginTransaction();
        Terrain t = getTerrain(type);
        if (t == null) {
            t = new Terrain(type);
            session.save(t);
        }
        session.getTransaction().commit();

    }

    public Terrain getTerrain(String type) {

        session.beginTransaction();
        Query q = session.createQuery("From Terrain T where T.type =:type");
        q.setParameter("type", type);
        Terrain res = (Terrain) q.uniqueResult();
        session.getTransaction().commit();
        return res;

    }

    public Terrain getRandomTerrain() {

        session.beginTransaction();

        Long count = (Long) session.createQuery("select count(*) from Terrain ").uniqueResult();
        Random rand = new Random();
        int rand_id = rand.nextInt(count.intValue()) + 1;
        Terrain res = session.get(Terrain.class, rand_id);
        session.getTransaction().commit();
        return res;
    }

    public void initialTerrain() {
        session.beginTransaction();
        addTerrain("grass");
        addTerrain("river");
        addTerrain("mountain");
        session.getTransaction().commit();
    }
}
