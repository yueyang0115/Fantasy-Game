package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.database.ItemPack;

import java.util.ArrayList;
import java.util.List;

public class ItemResultMessage {
    private List<ItemPack> items = new ArrayList<>(); //all items in the territory
    private String result; //status: "valid","invalid"

    public ItemResultMessage() {
    }

    public List<ItemPack> getItems() {
        return items;
    }

    public void setItems(List<ItemPack> items) {
        this.items = items;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
