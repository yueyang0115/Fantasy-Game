package edu.duke.ece.fantasy.building;

import edu.duke.ece.fantasy.database.DBBuilding;
import edu.duke.ece.fantasy.database.DBBuildingDAO;
import edu.duke.ece.fantasy.database.WorldCoord;
import org.hibernate.Session;

import java.util.List;
import java.util.Map;

public class Building {
    WorldCoord coord;
    String name;
    int cost;
    List<Prerequisite> prerequisites;
    Map<String, Building> UpgradeTo;


    public WorldCoord getCoord() {
        return coord;
    }

    public void setCoord(WorldCoord coord) {
        this.coord = coord;
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

    protected DBBuilding SaveToBuildingTable(Session session, WorldCoord coord) {
        DBBuildingDAO dbBuildingDAO = new DBBuildingDAO(session);
        return dbBuildingDAO.addBuilding(coord, this);
    }

    public void onCreate(Session session, WorldCoord coord) {
        this.coord = coord;
        SaveToBuildingTable(session, coord);
    }
}
