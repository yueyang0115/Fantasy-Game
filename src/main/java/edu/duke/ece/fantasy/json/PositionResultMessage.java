package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.database.DBBuilding;
import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.Territory;

import java.util.List;

public class PositionResultMessage {
    private List<Territory> territoryArray;
    private List<Monster> monsterArray;
    private List<DBBuilding> buildingArray;

    public PositionResultMessage(){}

    public List<Territory> getTerritoryArray() {
        return territoryArray;
    }

    public void setTerritoryArray(List<Territory> territoryArray) {
        this.territoryArray = territoryArray;
    }

    public List<Monster> getMonsterArray() { return monsterArray; }

    public void setMonsterArray(List<Monster> monsterArray) { this.monsterArray = monsterArray; }

    public List<DBBuilding> getBuildingArray() {
        return buildingArray;
    }

    public void setBuildingArray(List<DBBuilding> buildingArray) {
        this.buildingArray = buildingArray;
    }
}
