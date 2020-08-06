package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.Account.LoginHandler;
import edu.duke.ece.fantasy.Account.SignUpHandler;
import edu.duke.ece.fantasy.Battle.BattleHandler;
import edu.duke.ece.fantasy.Building.BuildingHandler;
import edu.duke.ece.fantasy.Building.Prototype.Shop;
import edu.duke.ece.fantasy.Building.ShopHandler;
import edu.duke.ece.fantasy.Item.InventoryController;
import edu.duke.ece.fantasy.Item.InventoryHandler;
import edu.duke.ece.fantasy.Soldier.AttributeHandler;
import edu.duke.ece.fantasy.World.PositionUpdateHandler;

import javax.persistence.criteria.CriteriaBuilder;

public class GameContext {
    private static LoginHandler loginHandler = new LoginHandler();
    private static SignUpHandler signUpHandler = new SignUpHandler();
    private static PositionUpdateHandler positionUpdateHandler = new PositionUpdateHandler();
    private static AttributeHandler attributeHandler = new AttributeHandler();
    private static InventoryHandler inventoryHandler = new InventoryHandler();
    private static BuildingHandler buildingHandler = new BuildingHandler();
    private static ShopHandler shopHandler = new ShopHandler();
    private static BattleHandler battleHandler = new BattleHandler();

    public static LoginHandler getLoginHandler() {
        return loginHandler;
    }

    public static SignUpHandler getSignUpHandler() {
        return signUpHandler;
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
}
