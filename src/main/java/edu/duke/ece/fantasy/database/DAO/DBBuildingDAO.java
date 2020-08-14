package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.Building.Prototype.Building;
import edu.duke.ece.fantasy.database.DBBuilding;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.WorldCoord;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class DBBuildingDAO {

    Session session;

    public DBBuildingDAO(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public DBBuilding addBuilding(WorldCoord where, Building building) {
        DBBuilding DB_building = new DBBuilding(building.getClass().getName(), where);
        session.saveOrUpdate(DB_building);
        return DB_building;
    }

    public DBBuilding getBuilding(String name) {
        Query<DBBuilding> q = session.createQuery("From DBBuilding b where b.name =:name", DBBuilding.class);
        q.setParameter("name", name);
        return q.uniqueResult();
    }

    public DBBuilding getBuilding(WorldCoord coord) {
        Query<DBBuilding> q = session.createQuery("From DBBuilding b where b.coord =:coord", DBBuilding.class);
        q.setParameter("coord", coord);
        return q.uniqueResult();
    }
}
