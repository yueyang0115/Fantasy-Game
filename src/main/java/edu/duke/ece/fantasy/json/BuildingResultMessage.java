package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.database.Building;

import java.util.List;

public class BuildingResultMessage {
    List<String> BuildingList;
    Building building;
    String result; // "success","error"

    public List<String> getBuildingList() {
        return BuildingList;
    }

    public void addBuilding(String building){
        BuildingList.add(building);
    }

    public void setBuildingList(List<String> buildingList) {
        BuildingList = buildingList;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
