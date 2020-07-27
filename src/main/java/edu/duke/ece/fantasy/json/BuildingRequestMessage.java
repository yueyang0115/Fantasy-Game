package edu.duke.ece.fantasy.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.database.WorldCoord;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BuildingRequestMessage {
    WorldCoord coord;

    String action; //"createList","create","upgradeList","upgrade","destruct"

    String buildingName;

    public WorldCoord getCoord() {
        return coord;
    }

    public void setCoord(WorldCoord coord) {
        this.coord = coord;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
}
