package edu.duke.ece.fantasy.World.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.Account.CmdAccount;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.net.Message;
import edu.duke.ece.fantasy.net.MessageMeta;
import edu.duke.ece.fantasy.net.Modules;

import java.util.List;

@MessageMeta(module = Modules.POSITION, cmd = CmdWorld.REQ_POSITION)
public class PositionRequestMessage extends Message {
    private List<WorldCoord> coords;
    private WorldCoord currentCoord;

    public PositionRequestMessage() {
    }

    public PositionRequestMessage(List<WorldCoord> coords, WorldCoord currentCoord) {
        this.coords = coords;
        this.currentCoord = currentCoord;
    }

    public List<WorldCoord> getCoords() {
        return coords;
    }

    public void setCoords(List<WorldCoord> coords) {
        this.coords = coords;
    }

    public WorldCoord getCurrentCoord() { return currentCoord; }

    public void setCurrentCoord(WorldCoord currentCoord) { this.currentCoord = currentCoord; }
}