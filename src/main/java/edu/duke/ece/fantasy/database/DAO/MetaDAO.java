package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.RandomGenerator;
import edu.duke.ece.fantasy.building.Shop;
import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.Soldier;
import edu.duke.ece.fantasy.database.Tile;
import org.hibernate.Session;

public class MetaDAO {
    private static PlayerDAO playerDAO = new PlayerDAO();
    private static InventoryDAO inventoryDAO = new InventoryDAO();
    private static TileDAO tileDAO = new TileDAO();
    private static WorldDAO worldDAO = new WorldDAO();
    private static DBBuildingDAO dbBuildingDAO  = new DBBuildingDAO();
    private static PlayerInventoryDAO playerInventoryDAO = new PlayerInventoryDAO();
    private static ShopInventoryDAO shopInventoryDAO = new ShopInventoryDAO();
    private static TerritoryDAO territoryDAO = new TerritoryDAO();
    private static MonsterDAO monsterDAO = new MonsterDAO();
    private static SoldierDAO soldierDAO = new SoldierDAO();
    private static UnitDAO unitDAO = new UnitDAO();
    private static SkillDAO skillDAO = new SkillDAO();
    private static RelationshipDAO relationshipDAO = new RelationshipDAO();


    public MetaDAO() {
//        playerDAO = new PlayerDAO();
//        inventoryDAO = new InventoryDAO();
//        tileDAO = new TileDAO();
//        worldDAO = new WorldDAO();
//        dbBuildingDAO = new DBBuildingDAO();
//        playerInventoryDAO = new PlayerInventoryDAO();
//        shopInventoryDAO = new ShopInventoryDAO();
//        territoryDAO = new TerritoryDAO();
//        monsterDAO = new MonsterDAO();
//        soldierDAO = new SoldierDAO();
//        unitDAO = new UnitDAO();
//        skillDAO = new SkillDAO();
//        relationshipDAO = new RelationshipDAO();
    }

    static public RelationshipDAO getRelationshipDAO() {
        return relationshipDAO;
    }


    static public PlayerDAO getPlayerDAO() {
        return playerDAO;
    }

    public static InventoryDAO getInventoryDAO() {
        return inventoryDAO;
    }

    public TileDAO getTileDAO() {
        return tileDAO;
    }

    public static WorldDAO getWorldDAO() {
        return worldDAO;
    }

    public static DBBuildingDAO getDbBuildingDAO() {
        return dbBuildingDAO;
    }

    public static PlayerInventoryDAO getPlayerInventoryDAO() {
        return playerInventoryDAO;
    }

    public static ShopInventoryDAO getShopInventoryDAO() {
        return shopInventoryDAO;
    }

    static public TerritoryDAO getTerritoryDAO() {
        return territoryDAO;
    }

    public static MonsterDAO getMonsterDAO() {
        return monsterDAO;
    }

    public static SoldierDAO getSoldierDAO() {
        return soldierDAO;
    }

    public static UnitDAO getUnitDAO() {
        return unitDAO;
    }

    public static SkillDAO getSkillDAO() { return skillDAO; }

}
