package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.Building.Prototype.Building;
import edu.duke.ece.fantasy.database.DBBuilding;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.WorldCoord;

public class DBBuildingDAO {

    public DBBuilding addBuilding(DBBuilding building) {
        HibernateUtil.saveOrUpdate(building);
        return building;
    }

    public DBBuilding addBuilding(WorldCoord where, DBBuilding building) {
        building.setCoord(where);
        HibernateUtil.saveOrUpdate(building);
        return building;
    }

    public DBBuilding addBuilding(WorldCoord where, Building building) {
        DBBuilding DB_building = new DBBuilding(building.getClass().getName(), where);
        HibernateUtil.saveOrUpdate(DB_building);
        return DB_building;
    }

    public DBBuilding getBuilding(String name) {
        DBBuilding res = HibernateUtil.queryOne("From DBBuilding b where b.name =:name",DBBuilding.class,
                new String[]{"name"},new Object[]{name});
        return res;
    }

    public DBBuilding getBuilding(WorldCoord coord) {
        DBBuilding res = HibernateUtil.queryOne("From DBBuilding b where b.coord =:coord",DBBuilding.class,
                new String[]{"coord"},new Object[]{coord});
        return res;
    }
}
