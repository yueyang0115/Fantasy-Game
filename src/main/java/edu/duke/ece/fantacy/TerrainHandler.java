package edu.duke.ece.fantacy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Random;

public class TerrainHandler {
    SessionFactory sf = HibernateUtil.getSessionFactory();

    public void addTerrain(String type) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Terrain t = getTerrain(type);
            if (t == null) {
                t = new Terrain(type);
                session.save(t);
            }
            session.getTransaction().commit();
        }
    }

    public Terrain getTerrain(String type) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Query q = session.createQuery("From Terrain T where T.type =:type");
            q.setParameter("type", type);
            Terrain res = (Terrain) q.uniqueResult();
            session.getTransaction().commit();
            return res;
        }
    }

    public Terrain getRandomTerrain() {
        try (Session session = sf.openSession()) {
            session.beginTransaction();

            Long count = (Long) session.createQuery("select count(*) from Terrain ").uniqueResult();
            Random rand = new Random();
            int rand_id = rand.nextInt(count.intValue()) + 1;
            Terrain res = session.get(Terrain.class, rand_id);
            session.getTransaction().commit();
            return res;
        }
    }

    public void initialTerrain() {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            addTerrain("grass");
            addTerrain("river");
            addTerrain("mountain");
            session.getTransaction().commit();
        }
    }
}
