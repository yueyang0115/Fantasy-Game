package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.Account.AccountHandler;
import edu.duke.ece.fantasy.Battle.BattleHandler;
import edu.duke.ece.fantasy.Building.BuildingHandler;
import edu.duke.ece.fantasy.Building.ShopHandler;
import edu.duke.ece.fantasy.Friend.FriendHandler;
import edu.duke.ece.fantasy.Item.InventoryHandler;
import edu.duke.ece.fantasy.Soldier.AttributeHandler;
import edu.duke.ece.fantasy.Soldier.LevelUpHandler;
import edu.duke.ece.fantasy.Soldier.ReviveHandler;
import edu.duke.ece.fantasy.World.PositionUpdateHandler;

public class GameContext {
    private static AccountHandler accountHandler = new AccountHandler();
    private static PositionUpdateHandler positionUpdateHandler = new PositionUpdateHandler();
    private static AttributeHandler attributeHandler = new AttributeHandler();
    private static InventoryHandler inventoryHandler = new InventoryHandler();
    private static BuildingHandler buildingHandler = new BuildingHandler();
    private static ShopHandler shopHandler = new ShopHandler();
    private static BattleHandler battleHandler = new BattleHandler();
    private static LevelUpHandler levelUpHandler = new LevelUpHandler();
    private static ReviveHandler reviveHandler = new ReviveHandler();
    private static FriendHandler friendHandler = new FriendHandler();

    public static FriendHandler getFriendHandler() {
        return friendHandler;
    }

    public static AccountHandler getAccountHandler() {
        return accountHandler;
    }
    public static PositionUpdateHandler getPositionUpdateHandler() {
        return positionUpdateHandler;
    }

    public static BattleHandler getBattleHandler(){
        return battleHandler;
    }

    public static AttributeHandler getAttributeHandler() {
        return attributeHandler;
    }

    public static InventoryHandler getInventoryHandler() {
        return inventoryHandler;
    }

    public static ShopHandler getShopHandler() {
        return shopHandler;
    }

    public static BuildingHandler getBuildingHandler() {
        return buildingHandler;
    }

    public static LevelUpHandler getLevelUpHandler() { return levelUpHandler; }

    public static ReviveHandler getReviveHandler() { return reviveHandler; }

}
