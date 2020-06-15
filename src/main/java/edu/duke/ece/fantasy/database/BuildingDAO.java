package edu.duke.ece.fantasy.database;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class BuildingDAO {
    Session session;


    public BuildingDAO(Session session) {
        this.session = session;
    }

    public Building addBuilding(WorldCoord where, Building building) {
        building.setCoord(where);
        session.saveOrUpdate(building);
        return building;
    }

    public Building getBuilding(String name){
        Query q = session.createQuery("From Building b where b.name =:name");
        q.setParameter("name", name);
        Building res = (Building) q.uniqueResult();
        return res;
    }

    public Shop getBuilding(WorldCoord coord) {
        Query q = session.createQuery("From Building b where b.coord =:coord");
        q.setParameter("coord", coord);
        Shop res = (Shop) q.uniqueResult();
        return res;
    }
}
