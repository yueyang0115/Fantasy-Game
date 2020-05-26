package edu.duke.ece.fantacy.json;

import edu.duke.ece.fantacy.Territory;

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
