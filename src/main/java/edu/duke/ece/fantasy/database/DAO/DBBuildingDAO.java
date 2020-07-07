package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.building.Building;
import edu.duke.ece.fantasy.database.DBBuilding;
import edu.duke.ece.fantasy.database.WorldCoord;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class DBBuildingDAO {
    Session session;

    public DBBuildingDAO(Session session) {
        this.session = session;
    }

    public DBBuilding addBuilding(DBBuilding building) {
        session.saveOrUpdate(building);
        return building;
    }

    public DBBuilding addBuilding(WorldCoord where, DBBuilding building) {
        building.setCoord(where);
        session.saveOrUpdate(building);
        return building;
    }

    public DBBuilding addBuilding(WorldCoord where, Building building) {
        DBBuilding DB_building = new DBBuilding(building.getClass().getName(), where);
        session.saveOrUpdate(DB_building);
        return DB_building;
    }

    public DBBuilding getBuilding(String name) {
        Query q = session.createQuery("From DBBuilding b where b.name =:name");
        q.setParameter("name", name);
        DBBuilding res = (DBBuilding) q.uniqueResult();
        return res;
    }

    public DBBuilding getBuilding(WorldCoord coord) {
        Query<DBBuilding> q = session.createQuery("From DBBuilding b where b.coord =:coord", DBBuilding.class);
        q.setParameter("coord", coord);
        DBBuilding res = q.uniqueResult();
        return res;
    }
}
