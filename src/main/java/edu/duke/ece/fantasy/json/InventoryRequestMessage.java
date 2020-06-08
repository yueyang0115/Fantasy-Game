package edu.duke.ece.fantasy.json;

public class InventoryRequestMessage {
    private String action;
    private int itemPackID;
    private int unitID;

    public InventoryRequestMessage() {
    }

    public InventoryRequestMessage(String action) {
        this.action = action;
    }

    public InventoryRequestMessage(String action, int itemPackID, int unitID) {
        this.action = action;
        this.itemPackID = itemPackID;
        this.unitID = unitID;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getItemPackID() {
        return itemPackID;
    }

    public void setItemPackID(int itemPackID) {
        this.itemPackID = itemPackID;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }
}
