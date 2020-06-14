package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.Item.Item;
import edu.duke.ece.fantasy.database.Inventory;
import edu.duke.ece.fantasy.database.shopInventory;

import java.util.ArrayList;
import java.util.List;

public class ShopResultMessage{
    private InventoryResultMessage inventoryResultMessage;
    private List<Inventory> items = new ArrayList<>(); //all items in the shop
    private String result; //status: "valid","invalid"

    public ShopResultMessage() {
    }

    public InventoryResultMessage getInventoryResultMessage() {
        return inventoryResultMessage;
    }

    public void setInventoryResultMessage(InventoryResultMessage inventoryResultMessage) {
        this.inventoryResultMessage = inventoryResultMessage;
    }

    public List<Inventory> getItems() {
        return items;
    }

    public void setItems(List<Inventory> items) {
        this.items = items;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
