package edu.duke.ece.fantasy.database.DAO;

import java.util.HashMap;

import edu.duke.ece.fantasy.database.Tile;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.database.WorldInfo;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class TileDAO {
  Session session;

  public TileDAO(TerritoryDAO terrDAO) {
    this.session = terrDAO.session;
  }
  public TileDAO(Session session) {
    this.session = session;
  }

  public Tile addTile(WorldCoord wc, String name) {
    Tile t = new Tile(wc, name);
    session.saveOrUpdate(t);
    return t;
  }

  public Tile getTileAt(WorldCoord wc) {
    return (Tile) session.get(Tile.class, wc);
  }

  
  public HashMap<WorldCoord,Tile> getAllNearPoint(WorldCoord wc, int range) {
    Query<Tile> q = session.createQuery("FROM Tile T WHERE T.where.wid =:wid AND abs(T.where.x-:x)<=:range AND abs(T.where.y-:y)<:range",Tile.class);
    q.setParameter("wid", wc.getWid());
    q.setParameter("x", wc.getX());
    q.setParameter("y", wc.getY());
    q.setParameter("range", range);
    HashMap<WorldCoord, Tile> ans = new HashMap<WorldCoord, Tile>();
    for(Tile t: q.getResultList()){
      ans.put(t.getWhere(), t);
    }
    return ans;
  }
  public boolean doesWorldHaveStartTile(WorldInfo winfo) {
    return getTileAt(winfo.getFirstTile()) != null;
  }

}
