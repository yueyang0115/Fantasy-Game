package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.Soldier;
import edu.duke.ece.fantasy.database.Tile;
import org.hibernate.Session;

public class MetaDAO {
    private static PlayerDAO playerDAO = new PlayerDAO();
    private static InventoryDAO inventoryDAO;
    private static TileDAO tileDAO;
    private static WorldDAO worldDAO;
    private static DBBuildingDAO dbBuildingDAO;
    private static PlayerInventoryDAO playerInventoryDAO;
    private static ShopInventoryDAO shopInventoryDAO;
    private static TerritoryDAO territoryDAO;
    private static MonsterDAO monsterDAO;
    private static SoldierDAO soldierDAO;
    private static UnitDAO unitDAO;
    private static SkillDAO skillDAO;
    private static RelationshipDAO relationshipDAO;
    Session session;

    public MetaDAO(Session session) {
//        playerDAO = new PlayerDAO(session);
        inventoryDAO = new InventoryDAO(session);
        tileDAO = new TileDAO(session);
        worldDAO = new WorldDAO(session);
        dbBuildingDAO = new DBBuildingDAO(session);
        playerInventoryDAO = new PlayerInventoryDAO(session);
        shopInventoryDAO = new ShopInventoryDAO(session);
        territoryDAO = new TerritoryDAO(session);
        monsterDAO = new MonsterDAO(session);
        soldierDAO = new SoldierDAO(session);
        unitDAO = new UnitDAO(session);
        skillDAO = new SkillDAO(session);
        relationshipDAO = new RelationshipDAO(session);
        this.session = session;
    }

    public RelationshipDAO getRelationshipDAO() {
        return relationshipDAO;
    }


    static public PlayerDAO getPlayerDAO() {
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

    public TerritoryDAO getTerritoryDAO() {
        return territoryDAO;
    }

    public MonsterDAO getMonsterDAO() {
        return monsterDAO;
    }

    public SoldierDAO getSoldierDAO() {
        return soldierDAO;
    }

    public UnitDAO getUnitDAO() {
        return unitDAO;
    }

    public SkillDAO getSkillDAO() { return skillDAO; }

    public Session getSession() {
        return session;
    }
}
