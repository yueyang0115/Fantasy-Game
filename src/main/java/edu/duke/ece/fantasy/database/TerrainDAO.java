package edu.duke.ece.fantasy.database;

import edu.duke.ece.fantasy.database.Terrain;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Random;

public class TerrainDAO {
    Session session;

    public TerrainDAO(Session session) {
        this.session = session;
    }

    public void addTerrain(String type) {
        Terrain t = getTerrain(type);
        if (t == null) {
            t = new Terrain(type);
            session.save(t);
        }
    }

    public Terrain getTerrain(String type) {
        Query q = session.createQuery("From Terrain T where T.type =:type");
        q.setParameter("type", type);
        Terrain res = (Terrain) q.uniqueResult();
        return res;
    }

    public Terrain getRandomTerrain() {
        Long count = (Long) session.createQuery("select count(*) from Terrain ").uniqueResult();
        Random rand = new Random();
        int rand_id = rand.nextInt(count.intValue()) + 1;
        Terrain res = session.get(Terrain.class, rand_id);
        return res;
    }

    public void initialTerrain() {
        addTerrain("grass");
        addTerrain("river");
        addTerrain("mountain");
    }
}
