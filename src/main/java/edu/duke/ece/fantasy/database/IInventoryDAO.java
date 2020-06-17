package edu.duke.ece.fantasy.database;

public interface IInventoryDAO {
    public Inventory getInventory(int owner_id,String name);
}
