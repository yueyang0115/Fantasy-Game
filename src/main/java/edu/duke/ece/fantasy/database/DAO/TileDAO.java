package edu.duke.ece.fantasy.database.DAO;

import java.util.HashMap;
import java.util.List;

import edu.duke.ece.fantasy.database.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class TileDAO {

    Session session;

    public TileDAO(Session session) {
        this.session = session;
    }

    public Tile addTile(WorldCoord wc, String name) {
        Tile t = new Tile(wc, name);
        session.saveOrUpdate(t);
        return t;
    }

    public Tile getTileAt(WorldCoord wc) {
        return session.get(Tile.class, wc);
    }


    public HashMap<WorldCoord, Tile> getAllNearPoint(WorldCoord wc, int range) {
        HashMap<WorldCoord, Tile> ans = new HashMap<WorldCoord, Tile>();
        Query<Tile> q = session.createQuery("FROM Tile T WHERE T.where.wid =:wid AND abs(T.where.x-:x)<=:range AND abs(T.where.y-:y)<:range", Tile.class);
        String[] paraName = new String[]{"wid", "x", "y", "range"};
        Object[] para = new Object[]{wc.getWid(), wc.getX(), wc.getY(), range};
        HibernateUtil.assignMutilplePara(q,paraName,para);
        List<Tile> tileList = q.getResultList();
        for (Tile t : tileList) {
            ans.put(t.getWhere(), t);
        }
        return ans;
    }

    public boolean doesWorldHaveStartTile(WorldInfo winfo) {
        return getTileAt(winfo.getFirstTile()) != null;
    }

}
