package edu.duke.ece.fantasy.Building.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.Building.Building;
import edu.duke.ece.fantasy.net.Message;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BuildingResultMessage extends Message {
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
