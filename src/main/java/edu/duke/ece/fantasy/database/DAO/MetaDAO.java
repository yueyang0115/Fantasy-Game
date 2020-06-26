package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.Tile;
import org.hibernate.Session;

public class MetaDAO {
    PlayerDAO playerDAO;
    InventoryDAO inventoryDAO;
    TileDAO tileDAO;
    WorldDAO worldDAO;
    DBBuildingDAO dbBuildingDAO;
    PlayerInventoryDAO playerInventoryDAO;
    ShopInventoryDAO shopInventoryDAO;

    public MetaDAO(Session session){
        playerDAO = new PlayerDAO(session);
        inventoryDAO = new InventoryDAO(session);
        tileDAO = new TileDAO(session);
        worldDAO = new WorldDAO(session);
        dbBuildingDAO = new DBBuildingDAO(session);
        playerInventoryDAO = new PlayerInventoryDAO(session);
        shopInventoryDAO = new ShopInventoryDAO(session);
    }

    public PlayerDAO getPlayerDAO() {
        return playerDAO;
    }

    public InventoryDAO getInventoryDAO() {
        return inventoryDAO;
    }

    public TileDAO getTileDAO() {
        return tileDAO;
    }

    public WorldDAO getWorldDAO() {
        return worldDAO;
    }

    public DBBuildingDAO getDbBuildingDAO() {
        return dbBuildingDAO;
    }

    public PlayerInventoryDAO getPlayerInventoryDAO() {
        return playerInventoryDAO;
    }

    public ShopInventoryDAO getShopInventoryDAO() {
        return shopInventoryDAO;
    }

}
