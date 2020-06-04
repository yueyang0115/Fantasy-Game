package edu.duke.ece.fantasy.json;

public class BattleRequestMessage {
    private int territoryID;
    private int attackeeID;
    private int attackerID;
    private String action;//"attack" "escape" "start"

    public BattleRequestMessage() {
    }

    public BattleRequestMessage(int territoryID, int attackeeID, int attackerID, String action) {
        this.territoryID = territoryID;
        this.attackeeID = attackeeID;
        this.attackerID = attackerID;
        this.action = action;
    }

    public int getTerritoryID() {
        return territoryID;
    }

    public void setTerritoryID(int territoryID) {
        this.territoryID = territoryID;
    }

    public int getAttackeeID() {
        return attackeeID;
    }

    public void setAttackeeID(int attackeeID) {
        this.attackeeID = attackeeID;
    }

    public int getAttackerID() {
        return attackerID;
    }

    public void setAttackerID(int attackerID) {
        this.attackerID = attackerID;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
