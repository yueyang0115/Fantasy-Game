package edu.duke.ece.fantasy.Item.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.Item.Item;
import edu.duke.ece.fantasy.database.Inventory;
import edu.duke.ece.fantasy.database.playerInventory;
import edu.duke.ece.fantasy.net.Message;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryResultMessage extends Message {
    private List<Inventory> items = new ArrayList<>(); //all items of player
    private String result; //status: "valid","invalid"
    private int money;

    public InventoryResultMessage() {
        super();
    }


    public List<Inventory> getItems() {
        return items;
    }

    public void setItems(List<Inventory> items) {
        this.items = items;
    }

    public void addItem(Inventory item) {
        this.items.add(item);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

}
