package edu.duke.ece.fantasy.Building.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.database.Inventory;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.net.Message;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopRequestMessage extends Message {
    private WorldCoord coord;
//    private Map<Integer, Integer> itemMap;
    private List<Inventory> selectedItems;
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

    public List<Inventory> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<Inventory> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
