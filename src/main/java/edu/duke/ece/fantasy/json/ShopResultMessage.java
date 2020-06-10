package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.database.Item;
import edu.duke.ece.fantasy.database.ItemPack;
import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.Soldier;

import java.util.ArrayList;
import java.util.List;

public class ShopResultMessage{
    private InventoryResultMessage inventoryResultMessage;
    private List<ItemPack> items = new ArrayList<>(); //all items in the territory
    private String result; //status: "valid","invalid"

    public ShopResultMessage() {
    }

    public InventoryResultMessage getInventoryResultMessage() {
        return inventoryResultMessage;
    }

    public void setInventoryResultMessage(InventoryResultMessage inventoryResultMessage) {
        this.inventoryResultMessage = inventoryResultMessage;
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
