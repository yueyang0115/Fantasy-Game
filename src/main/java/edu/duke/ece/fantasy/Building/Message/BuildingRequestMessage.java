package edu.duke.ece.fantasy.Building.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.Account.CmdAccount;
import edu.duke.ece.fantasy.Building.CmdBuilding;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.net.Message;
import edu.duke.ece.fantasy.net.MessageMeta;
import edu.duke.ece.fantasy.net.Modules;

@MessageMeta(module = Modules.BUILDING, cmd = CmdBuilding.REQ_BUILDING)
public class BuildingRequestMessage extends Message {
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
