package edu.duke.ece.fantasy.json;

public class InventoryRequestMessage {
    private String action;

    public InventoryRequestMessage() {
    }

    public InventoryRequestMessage(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
