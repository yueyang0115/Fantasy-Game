package edu.duke.ece.fantasy.building;

import edu.duke.ece.fantasy.database.DBBuilding;
import edu.duke.ece.fantasy.database.DBBuildingDAO;
import edu.duke.ece.fantasy.database.WorldCoord;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Building {
    WorldCoord coord;
    String name;
    int cost;
    List<Prerequisite> prerequisites = new ArrayList<>();
    Map<String, Building> UpgradeTo = new HashMap<>();
    DBBuilding dbBuilding;


    public WorldCoord getCoord() {
        return coord;
    }

    public void setCoord(WorldCoord coord) {
        this.coord = coord;
    }

    public List<Building> getUpgradeList() {
        return new ArrayList<>(UpgradeTo.values());
    }

    public Map<String, Building> getUpgradeTo() {
        return UpgradeTo;
    }

    public void setUpgradeTo(Map<String, Building> upgradeTo) {
        UpgradeTo = upgradeTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Prerequisite> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<Prerequisite> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public Building(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }


    public void onCreate(Session session, WorldCoord coord) {
        this.coord = coord;
        DBBuildingDAO dbBuildingDAO = new DBBuildingDAO(session);
        DBBuilding tmp = dbBuildingDAO.getBuilding(coord);
        if (tmp != null) { // delete existing building in this coord
            session.delete(tmp);
        }
        dbBuilding = dbBuildingDAO.addBuilding(coord, this);
    }

}
