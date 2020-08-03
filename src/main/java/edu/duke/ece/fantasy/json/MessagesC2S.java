package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.Battle.Message.BattleRequestMessage;
import edu.duke.ece.fantasy.Building.Message.BuildingRequestMessage;
import edu.duke.ece.fantasy.Building.Message.ShopRequestMessage;
import edu.duke.ece.fantasy.Friend.Message.FriendRequestMessage;
import edu.duke.ece.fantasy.Item.Message.InventoryRequestMessage;
import edu.duke.ece.fantasy.Soldier.Message.AttributeRequestMessage;
import edu.duke.ece.fantasy.Soldier.Message.LevelUpRequestMessage;
import edu.duke.ece.fantasy.Soldier.Message.ReviveRequestMessage;
import edu.duke.ece.fantasy.Account.Message.LoginRequestMessage;
import edu.duke.ece.fantasy.Account.Message.SignUpRequestMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.World.Message.PositionRequestMessage;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessagesC2S {
    private LoginRequestMessage loginRequestMessage;
    private SignUpRequestMessage signUpRequestMessage;
    private PositionRequestMessage positionRequestMessage;
    private BattleRequestMessage battleRequestMessage;
    private ShopRequestMessage shopRequestMessage;
    private AttributeRequestMessage attributeRequestMessage;
    private InventoryRequestMessage inventoryRequestMessage;
    private BuildingRequestMessage buildingRequestMessage;
    private LevelUpRequestMessage levelUpRequestMessage;
    private RedirectMessage redirectMessage;
    private ReviveRequestMessage reviveRequestMessage;
    private FriendRequestMessage friendRequestMessage;

    public MessagesC2S(){ }

    public BuildingRequestMessage getBuildingRequestMessage() {
        return buildingRequestMessage;
    }

    public void setBuildingRequestMessage(BuildingRequestMessage buildingRequestMessage) {
        this.buildingRequestMessage = buildingRequestMessage;
    }

    public FriendRequestMessage getFriendRequestMessage() {
        return friendRequestMessage;
    }

    public void setFriendRequestMessage(FriendRequestMessage friendRequestMessage) {
        this.friendRequestMessage = friendRequestMessage;
    }

    public MessagesC2S(LoginRequestMessage loginRequestMessage) {
        this.loginRequestMessage = loginRequestMessage;
    }

    public MessagesC2S(SignUpRequestMessage signUpRequestMessage) {
        this.signUpRequestMessage = signUpRequestMessage;
    }

    public MessagesC2S(PositionRequestMessage positionRequestMessage) { this.positionRequestMessage = positionRequestMessage; }

    public MessagesC2S(BattleRequestMessage battleRequestMessage) { this.battleRequestMessage = battleRequestMessage; }

    public MessagesC2S(LevelUpRequestMessage levelUpRequestMessage){this.levelUpRequestMessage = levelUpRequestMessage; }

    public LoginRequestMessage getLoginRequestMessage() {
        return loginRequestMessage;
    }

    public void setLoginRequestMessage(LoginRequestMessage loginRequestMessage) {
        this.loginRequestMessage = loginRequestMessage;
    }

    public SignUpRequestMessage getSignUpRequestMessage() {
        return signUpRequestMessage;
    }

    public void setSignUpRequestMessage(SignUpRequestMessage signUpRequestMessage) {
        this.signUpRequestMessage = signUpRequestMessage;
    }

    public PositionRequestMessage getPositionRequestMessage() {
        return positionRequestMessage;
    }

    public void setPositionRequestMessage(PositionRequestMessage positionRequestMessage) {
        this.positionRequestMessage = positionRequestMessage;
    }

    public BattleRequestMessage getBattleRequestMessage() {
        return battleRequestMessage;
    }

    public void setBattleRequestMessage(BattleRequestMessage battleRequestMessage) {
        this.battleRequestMessage = battleRequestMessage;
    }

    public AttributeRequestMessage getAttributeRequestMessage() { return attributeRequestMessage; }

    public void setAttributeRequestMessage(AttributeRequestMessage attributeRequestMessage) {
        this.attributeRequestMessage = attributeRequestMessage;
    }

    public ShopRequestMessage getShopRequestMessage() {
        return shopRequestMessage;
    }

    public void setShopRequestMessage(ShopRequestMessage shopRequestMessage) {
        this.shopRequestMessage = shopRequestMessage;
    }

    public InventoryRequestMessage getInventoryRequestMessage() {
        return inventoryRequestMessage;
    }

    public void setInventoryRequestMessage(InventoryRequestMessage inventoryRequestMessage) {
        this.inventoryRequestMessage = inventoryRequestMessage;
    }

    public LevelUpRequestMessage getLevelUpRequestMessage() { return levelUpRequestMessage; }

    public void setLevelUpRequestMessage(LevelUpRequestMessage levelUpRequestMessage) {
        this.levelUpRequestMessage = levelUpRequestMessage;
    }

    public RedirectMessage getRedirectMessage() { return redirectMessage; }

    public void setRedirectMessage(RedirectMessage redirectMessage) {
        this.redirectMessage = redirectMessage;
    }

    public ReviveRequestMessage getReviveRequestMessage() { return reviveRequestMessage; }

    public void setReviveRequestMessage(ReviveRequestMessage reviveRequestMessage) {
        this.reviveRequestMessage = reviveRequestMessage;
    }
}
