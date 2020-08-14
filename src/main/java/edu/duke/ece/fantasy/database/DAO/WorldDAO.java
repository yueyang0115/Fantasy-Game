package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.database.WorldInfo;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorldDAO {
    Logger log = LoggerFactory.getLogger(WorldDAO.class);
    Session session;

    public WorldDAO(Session session) {
        this.session = session;
    }

    public WorldInfo getInfo(int wid) {
        return session.get(WorldInfo.class, wid);
    }

    public WorldInfo initWorld(WorldCoord where, String owner, int tilesize) {
        WorldInfo wi = new WorldInfo(where, owner, tilesize);
        session.save(wi);
        return wi;
    }
}
