package edu.duke.ece.fantasy.Item.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.Building.CmdBuilding;
import edu.duke.ece.fantasy.Item.CmdInventory;
import edu.duke.ece.fantasy.net.Message;
import edu.duke.ece.fantasy.net.MessageMeta;
import edu.duke.ece.fantasy.net.Modules;

@MessageMeta(module = Modules.INVENTORY, cmd = CmdInventory.REQ_INVENTORY)
public class InventoryRequestMessage extends Message {
    private String action;
    private int inventoryID;
    private int unitID;

    public InventoryRequestMessage() {
    }

    public InventoryRequestMessage(String action) {
        this.action = action;
    }

    public InventoryRequestMessage(String action, int itemPackID, int unitID) {
        this.action = action;
        this.inventoryID = itemPackID;
        this.unitID = unitID;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }
}
