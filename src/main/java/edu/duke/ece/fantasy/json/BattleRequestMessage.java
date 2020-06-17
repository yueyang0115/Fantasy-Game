package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.database.WorldCoord;

public class BattleRequestMessage {
    private WorldCoord territoryCoord; //territoryCoord includes: x,y,wid
    private String action;//"attack" "escape" "start"
    private BattleAction battleAction; //inclide attackerID, attackeeID, action("normal, magical")

    public BattleRequestMessage() {
    }

    public BattleRequestMessage(WorldCoord territoryCoord, String action, BattleAction battleAction) {
        this.territoryCoord = territoryCoord;
        this.action = action;
        this.battleAction = battleAction;
    }

    public WorldCoord getTerritoryCoord() { return territoryCoord; }

    public void setTerritoryCoord(WorldCoord territoryCoord) { this.territoryCoord = territoryCoord; }

    public BattleAction getBattleAction() {
        return battleAction;
    }

    public void setBattleAction(BattleAction battleAction) {
        this.battleAction = battleAction;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
