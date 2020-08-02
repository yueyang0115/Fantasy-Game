package edu.duke.ece.fantasy.database.DAO;

import java.util.HashMap;
import java.util.List;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Tile;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.database.WorldInfo;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class TileDAO {

    public Tile addTile(WorldCoord wc, String name) {
        Tile t = new Tile(wc, name);
        HibernateUtil.saveOrUpdate(t);
        return t;
    }

    public Tile getTileAt(WorldCoord wc) {
        return HibernateUtil.get(Tile.class, wc);
    }


    public HashMap<WorldCoord, Tile> getAllNearPoint(WorldCoord wc, int range) {
        HashMap<WorldCoord, Tile> ans = new HashMap<WorldCoord, Tile>();
        List<Tile> tileList = HibernateUtil.queryList("FROM Tile T WHERE T.where.wid =:wid AND abs(T.where.x-:x)<=:range AND abs(T.where.y-:y)<:range", Tile.class,
                new String[]{"wid", "x", "y", "range"}, new Object[]{wc.getWid(), wc.getX(), wc.getY(), range});
        for (Tile t : tileList) {
            ans.put(t.getWhere(), t);
        }
        return ans;
    }

    public boolean doesWorldHaveStartTile(WorldInfo winfo) {
        return getTileAt(winfo.getFirstTile()) != null;
    }

}
