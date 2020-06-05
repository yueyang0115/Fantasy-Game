package edu.duke.ece.fantasy.database;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class BuildingDAO {
    Session session;

    public BuildingDAO(Session session) {
        this.session = session;
    }

    public Building getBuilding(String name){
        Query q = session.createQuery("From Building b where b.name =:name");
        q.setParameter("name", name);
        Building res = (Building) q.uniqueResult();
        return res;
    }
}