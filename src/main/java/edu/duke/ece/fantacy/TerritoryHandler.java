package edu.duke.ece.fantacy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;

public class TerritoryHandler {
    Session session;
    private int width_unit = 10;
    private int height_unit = 10;
    private static ArrayList<Integer> x_offset = new ArrayList<>(Arrays.asList(-10, 0, 10));
    private static ArrayList<Integer> y_offset = new ArrayList<>(Arrays.asList(-10, 0, 10));

    public TerritoryHandler(Session session) {
        this.session = session;
    }

    public int[] MillierConvertion(double latitude, double longitude) {
        double L = 6381372 * Math.PI * 2;// perimeter of earth
        double W = L;
        double H = L / 2;
        double mill = 2.3;// miller projection parameter
        double x = (longitude * Math.PI) / 180;
        double y = (latitude * Math.PI) / 180;
        y = (1.25 * Math.log(Math.tan(0.25 * Math.PI + 0.4 * y)));
        x = ((W / 2) + (W / (2 * Math.PI))) * x;
        y = ((H / 2) - (H / (2 * mill))) * y;
        int[] result = new int[2];
        result[0] = (int) x;
        result[1] = (int) y;
        return result;
    }


    // given coordination, return list of territory
    public List<Territory> getTerritories(int wid, double latitude, double longitude) {
        List<Territory> res = new ArrayList<>();
        int[] coor = MillierConvertion(latitude, longitude);

        // get neighbor territories
        for (int i = 0; i < x_offset.size(); i++) {
            for (int j = 0; j < x_offset.size(); j++) {
                int target_x = coor[0] + x_offset.get(i);
                int target_y = coor[1] + y_offset.get(j);
                Territory t = getTerritory(wid, target_x, target_y);
                if (t != null) {
                    t.setX(i - 1);// set relative coordinate
                    t.setY(j - 1);
                    res.add(t);
                }
            }
        }
        return res;
    }

    public void addTerritories(int wid, double latitude, double longitude) {
        // add 9 squares to database
        int[] coor = MillierConvertion(latitude, longitude);

        for (int x_off : x_offset) {
            for (int y_off : y_offset) {
                int target_x = coor[0] + x_off;
                int target_y = coor[1] + y_off;
                addTerritory(wid, target_x, target_y, "unexplored");
            }
        }
    }

    public boolean addTerritory(int wid, int x, int y, String status) {
        // insert territory to world
        TerrainHandler terrainHandler = new TerrainHandler(session);
        // find the center of block
        int center_x = (x > 0) ? (x / width_unit) * width_unit + width_unit / 2 : (x / width_unit) * width_unit - width_unit / 2;
        int center_y = (y > 0) ? (y / height_unit) * height_unit + height_unit / 2 : (y / height_unit) * height_unit - height_unit / 2;
        Territory t = getTerritory(wid, center_x, center_y);
        boolean res = false;
        if (t == null) {
            t = new Territory(wid, center_x, center_y, status);
            t.addMonster(new Monster("wolf", 100, 10)); // add monster
            // add terrain
            Terrain terrain = terrainHandler.getRandomTerrain();
//            session.update(terrain); // reattach the object to session
            t.setTerrain(terrain);
            session.save(t);
            res = true;
            session.getTransaction().commit();
        }
        return res;
    }


    public boolean updateTerritory(int wid, int x, int y, String status) {
        session.beginTransaction();
        boolean res = false;
        Territory t = getTerritory(wid, x, y);
        if (t == null) { // don't have territory
            return res;
        }
        t.setStatus(status);
        session.update(t);
        session.getTransaction().commit();
        return res;

    }

    public Territory getTerritory(int wid, int x, int y) {
        // select territory according to conditions
        session.beginTransaction();
        int center_x = (x > 0) ? (x / width_unit) * width_unit + width_unit / 2 : (x / width_unit) * width_unit - width_unit / 2;
        int center_y = (y > 0) ? (y / height_unit) * height_unit + height_unit / 2 : (y / height_unit) * height_unit - height_unit / 2;
        Query q = session.createQuery("From Territory M where M.wid =:wid and M.x =:x and M.y = :y");
        q.setParameter("wid", wid);
        q.setParameter("x", center_x);
        q.setParameter("y", center_y);
        Territory res = (Territory) q.uniqueResult();
        return res;

    }
}
