package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.database.WorldCoord;

import java.util.Map;

public class ShopRequestMessage {
    private WorldCoord coord;
    private Map<Integer, Integer> itemMap;
    private String action;//"list""buy""sell"

    public ShopRequestMessage() {
    }

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

    public Map<Integer, Integer> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<Integer, Integer> itemMap) {
        this.itemMap = itemMap;
    }
}
