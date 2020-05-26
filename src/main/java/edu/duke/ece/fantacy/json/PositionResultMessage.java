package edu.duke.ece.fantacy.json;

import edu.duke.ece.fantacy.Territory;

import java.util.ArrayList;
import java.util.List;

public class PositionResultMessage {
    public PositionResultMessage(){}

    public List<Territory> getTerritory_array() {
        return territory_array;
    }

    public void setTerritory_array(List<Territory> territory_array) {
        this.territory_array = territory_array;
    }

    private List<Territory> territory_array;

}
