package edu.duke.ece.fantasy.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.duke.ece.fantasy.TerritoryBlock;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TerritoryDAO {
    Session session;
    private int width_unit = 10;
    private int height_unit = 10;
    Logger log = LoggerFactory.getLogger(TerritoryDAO.class);

    public TerritoryDAO(Session session) {
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
    public List<Territory> getTerritories(int wid, int x, int y, int x_block_num, int y_block_num) {
        List<Territory> res = new ArrayList<>();

        // get neighbor territories
        for (int i = 0; i < x_block_num; i++) {
            for (int j = 0; j < y_block_num; j++) {
                int target_x = x + (i - x_block_num / 2) * 10;
                int target_y = y + (j - y_block_num / 2) * 10;
                Territory t = getTerritory(wid, target_x, target_y);
                if (t != null) {
//                if (t != null && !t.getStatus().equals("unexplored")) {
                    Territory new_t = new Territory(t);
                    res.add(new_t);
                }
            }
        }
        return res;
    }


    public void addTerrainToTerritory(Territory territory, Terrain terrain) {
        territory.setTerrain(terrain);
        session.update(territory);
    }

    public void addMonsterToTerritory(Territory territory, Monster monster) {
        territory.addMonster(monster);
        session.update(territory);
    }

    public void addBuildingToTerritory(Territory territory, Building building) {
        territory.setBuilding(building);
        session.update(territory);
    }

    public Territory addTerritory(int wid, int x, int y, String status, Terrain terrain, List<Monster> monsters) throws IllegalArgumentException {
        // insert territory to world
        TerrainDAO terrainDAO = new TerrainDAO(session);
        // find the center of block
        int center_x = (x > 0) ? (x / width_unit) * width_unit + width_unit / 2 : (x / width_unit) * width_unit - width_unit / 2;
        int center_y = (y > 0) ? (y / height_unit) * height_unit + height_unit / 2 : (y / height_unit) * height_unit - height_unit / 2;
        Territory t = getTerritory(wid, center_x, center_y);
        if (t != null) {
            throw new IllegalArgumentException("already exist");
        }
        t = new Territory(wid, center_x, center_y, status);
        // add terrain
//        Terrain terrain = terrainDAO.getTerrain(terrain_type);
        t.setTerrain(terrain);
        // add monster
//        if (terrain.getType().equals("mountain")) {
//            t.addMonster(new Monster("wolf", 100, 10));
//        }
        for (Monster monster : monsters) {
            t.addMonster(monster);
        }
        session.save(t);
        return t;
    }


    public boolean updateTerritory(int wid, int x, int y, String status) {
        Territory t = getTerritory(wid, x, y);
        if (t == null) { // don't have territory
            return false;
        }
        t.setStatus(status);
        session.update(t);
        return true;
    }

    public Territory getTerritory(int wid, int x, int y) {
        // select territory according to conditions
        int center_x = (x > 0) ? (x / width_unit) * width_unit + width_unit / 2 : (x / width_unit) * width_unit - width_unit / 2;
        int center_y = (y > 0) ? (y / height_unit) * height_unit + height_unit / 2 : (y / height_unit) * height_unit - height_unit / 2;
        Query q = session.createQuery("From Territory M where M.wid =:wid and M.x =:x and M.y = :y");
        q.setParameter("wid", wid);
        q.setParameter("x", center_x);
        q.setParameter("y", center_y);
//        q.setParameter("x", x);
//        q.setParameter("y", y);
        Territory res = (Territory) q.uniqueResult();
        return res;

    }
}
