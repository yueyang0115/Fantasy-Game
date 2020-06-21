package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.building.Building;
import edu.duke.ece.fantasy.database.DBBuilding;

import java.util.ArrayList;
import java.util.List;

public class BuildingResultMessage {
    List<Building> BuildingList = new ArrayList<>();
    Building building;
    String action; //"createList","create","upgradeList","upgrade","destruct"
    String result; // "success","error"

    public List<Building> getBuildingList() {
        return BuildingList;
    }

    public void addBuilding(Building building){
        BuildingList.add(building);
    }

    public void setBuildingList(List<Building> buildingList) {
        BuildingList = buildingList;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
