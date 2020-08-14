package edu.duke.ece.fantasy.Building.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.Building.CmdBuilding;
import edu.duke.ece.fantasy.Item.Item;
import edu.duke.ece.fantasy.database.Inventory;
import edu.duke.ece.fantasy.database.shopInventory;
import edu.duke.ece.fantasy.net.Message;
import edu.duke.ece.fantasy.net.MessageMeta;
import edu.duke.ece.fantasy.net.Modules;

import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = Modules.BUILDING, cmd = CmdBuilding.RES_SHOP)
public class ShopResultMessage extends Message {
    private List<Inventory> items = new ArrayList<>(); //all items in the shop
    private String result; //status: "valid","invalid"

    public ShopResultMessage() {
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

}
