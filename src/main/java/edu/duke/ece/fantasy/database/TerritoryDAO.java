package edu.duke.ece.fantasy.database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public Session getSession() {
        return session;
    }

    // given coordination, return list of territory
    public List<Territory> getTerritories(WorldCoord where, int x_block_num, int y_block_num) {
        List<Territory> res = new ArrayList<>();
        int wid = where.getWid();
        int x = where.getX();
        int y = where.getY();
        // get neighbor territories
        for (int i = -x_block_num/2; i <= x_block_num/2; i++) {
            for (int j = -y_block_num/2; j <= y_block_num/2; j++) {
                int target_x = x + i;
                int target_y = y + j;
                Territory t = getTerritory(new WorldCoord(wid, target_x, target_y));
                if (t != null) {
//                if (t != null && !t.getStatus().equals("unexplored")) {
                    res.add(t);
//                    Territory new_t = new Territory(t);
//                    res.add(new_t);
                }
            }
        }
        return res;
    }


    public void addMonsterToTerritory(Territory territory, Monster monster) {
        //territory.addMonster(monster);
        //session.update(territory);
    }



    public Territory addTerritory(WorldCoord where, int status, String terrain, List<Monster> monsters) {
        // insert territory to world
        Territory t = new Territory(where, status);
        // add terrain
//        Terrain terrain = terrainDAO.getTerrain(terrain_type);
        t.setTerrainType(terrain);
        // add monster
        /*for (Monster monster : monsters) {
            t.addMonster(monster);
            }*/
        session.save(t);
        return t;
    }


    public boolean updateTerritory(WorldCoord where, int status) {
        Territory t = getTerritory(where);
        if (t == null) { // don't have territory
            return false;
        }
        t.setTame(status);
        session.update(t);
        return true;
    }

    public Territory getTerritory(WorldCoord where) {
        // select territory according to conditions
        //       int center_x = (x > 0) ? (x / width_unit) * width_unit + width_unit / 2 : (x / width_unit) * width_unit - width_unit / 2;
        //int center_y = (y > 0) ? (y / height_unit) * height_unit + height_unit / 2 : (y / height_unit) * height_unit - height_unit / 2;
        Query q = session.createQuery("From Territory M where M.coord=:coord");
        q.setParameter("coord",where);
//        q.setParameter("x", x);
//        q.setParameter("y", y);
        Territory res = (Territory) q.uniqueResult();
        return res;

    }

    public synchronized WorldCoord getWildestCoordInRange(WorldCoord where, int x_range, int y_range){
        Query q = session.createQuery("Select T From Territory T where T.coord.wid =:wid"
                +" and T.coord.x >:xlower and T.coord.x <:xupper"
                +" and T.coord.y >:ylower and T.coord.y <:yupper"
                +" and not exists (from Monster M where M.coord = T.coord)"
                +" order by T.tame DESC"
        ).setMaxResults(1);
        q.setParameter("wid", where.getWid());
        q.setParameter("xlower", where.getX() - x_range/2);
        q.setParameter("xupper", where.getX() + x_range/2);
        q.setParameter("ylower", where.getY() - y_range/2);
        q.setParameter("yupper", where.getY() + y_range/2);

        Territory t = (Territory) q.uniqueResult();
        WorldCoord res = t.getCoord();

        return res;
    }

    public int getTameByCoord(WorldCoord where){
        Territory t = getTerritory(where);
        return t.getTame();
    }
}
