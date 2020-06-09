package edu.duke.ece.fantasy.json;

public class BattleRequestMessage {
    private int territoryID;
    private String action;//"attack" "escape" "start"
    private BattleAction battleAction; //inclide attackerID, attackeeID, action("normal, magical")

    public BattleRequestMessage() {
    }

    public BattleRequestMessage(int territoryID, String action, BattleAction battleAction) {
        this.territoryID = territoryID;
        this.action = action;
    }

    public int getTerritoryID() {
        return territoryID;
    }

    public void setTerritoryID(int territoryID) {
        this.territoryID = territoryID;
    }

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
