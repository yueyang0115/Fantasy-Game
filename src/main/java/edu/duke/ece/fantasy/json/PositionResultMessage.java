package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.database.Territory;

import java.util.List;

public class PositionResultMessage {
    public PositionResultMessage(){}

    public List<Territory> getTerritoryArray() {
        return territoryArray;
    }

    public void setTerritoryArray(List<Territory> territoryArray) {
        this.territoryArray = territoryArray;
    }

    private List<Territory> territoryArray;

}