package edu.duke.ece.fantasy.database.DAO;

import org.hibernate.Session;

public class MetaDAO {
    private  PlayerDAO playerDAO;
    private  InventoryDAO inventoryDAO;
    private  TileDAO tileDAO;
    private  WorldDAO worldDAO;
    private  DBBuildingDAO dbBuildingDAO;
    private  PlayerInventoryDAO playerInventoryDAO;
    private  ShopInventoryDAO shopInventoryDAO;
    private  TerritoryDAO territoryDAO;
    private  MonsterDAO monsterDAO;
    private  SoldierDAO soldierDAO;
    private  UnitDAO unitDAO;
    private  SkillDAO skillDAO;
    private  RelationshipDAO relationshipDAO;


    public MetaDAO(Session session) {
        playerDAO = new PlayerDAO(session);
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
    }

    public TileDAO getTileDAO() {
        return tileDAO;
    }

    public RelationshipDAO getRelationshipDAO() {
        return relationshipDAO;
    }


     public PlayerDAO getPlayerDAO() {
        return playerDAO;
    }

    public  InventoryDAO getInventoryDAO() {
        return inventoryDAO;
    }

    public  WorldDAO getWorldDAO() {
        return worldDAO;
    }

    public  DBBuildingDAO getDbBuildingDAO() {
        return dbBuildingDAO;
    }

    public  PlayerInventoryDAO getPlayerInventoryDAO() {
        return playerInventoryDAO;
    }

    public  ShopInventoryDAO getShopInventoryDAO() {
        return shopInventoryDAO;
    }

     public TerritoryDAO getTerritoryDAO() {
        return territoryDAO;
    }

    public  MonsterDAO getMonsterDAO() {
        return monsterDAO;
    }

    public  SoldierDAO getSoldierDAO() {
        return soldierDAO;
    }

    public  UnitDAO getUnitDAO() {
        return unitDAO;
    }

    public  SkillDAO getSkillDAO() { return skillDAO; }

}
