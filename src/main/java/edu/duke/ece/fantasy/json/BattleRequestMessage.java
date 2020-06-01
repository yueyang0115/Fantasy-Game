package edu.duke.ece.fantasy.json;

public class BattleRequestMessage {
    private int territoryID;
    private int monsterID;
    private int soldierID;
    private String action;//"attack" "escape"

    public BattleRequestMessage() {
    }

    public BattleRequestMessage(int territoryID, int monsterID, int soldierID, String action) {
        this.territoryID = territoryID;
        this.monsterID = monsterID;
        this.soldierID = soldierID;
        this.action = action;
    }

    public int getTerritoryID() {
        return territoryID;
    }

    public void setTerritoryID(int territoryID) {
        this.territoryID = territoryID;
    }

    public int getMonsterID() {
        return monsterID;
    }

    public void setMonsterID(int monsterID) {
        this.monsterID = monsterID;
    }

    public int getSoldierID() {
        return soldierID;
    }

    public void setSoldierID(int soldierID) {
        this.soldierID = soldierID;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
