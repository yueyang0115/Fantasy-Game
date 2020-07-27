package edu.duke.ece.fantasy.json;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryRequestMessage {
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
