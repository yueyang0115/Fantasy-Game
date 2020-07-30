package edu.duke.ece.fantasy.json;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessagesS2C {
    private PositionResultMessage positionResultMessage;
    private LoginResultMessage loginResultMessage;
    private SignUpResultMessage signUpResultMessage;
    private BattleResultMessage battleResultMessage;
    private ShopResultMessage shopResultMessage;
    private AttributeResultMessage attributeResultMessage;
    private InventoryResultMessage inventoryResultMessage;
    private BuildingResultMessage buildingResultMessage;
    private RedirectMessage redirectMessage;
    private LevelUpResultMessage levelUpResultMessage;
    private ReviveResultMessage reviveResultMessage;
    private FriendResultMessage friendResultMessage;
    public MessagesS2C(){ }

    public BuildingResultMessage getBuildingResultMessage() {
        return buildingResultMessage;
    }

    public void setBuildingResultMessage(BuildingResultMessage buildingResultMessage) {
        this.buildingResultMessage = buildingResultMessage;
    }

    public FriendResultMessage getFriendResultMessage() {
        return friendResultMessage;
    }

    public void setFriendResultMessage(FriendResultMessage friendResultMessage) {
        this.friendResultMessage = friendResultMessage;
    }

    public MessagesS2C(LoginResultMessage msg){
        this.loginResultMessage = msg;
    }

   public MessagesS2C(SignUpResultMessage msg){
        this.signUpResultMessage = msg;
   }

   public MessagesS2C(PositionResultMessage msg){
        this.positionResultMessage = msg;
   }

    public MessagesS2C(BattleResultMessage msg){
        this.battleResultMessage = msg;
    }

    public MessagesS2C(LevelUpResultMessage msg) {this.levelUpResultMessage = msg;}

    public PositionResultMessage getPositionResultMessage() {
        return positionResultMessage;
    }

    public void setPositionResultMessage(PositionResultMessage positionResultMessage) {
        this.positionResultMessage = positionResultMessage;
    }

    public LoginResultMessage getLoginResultMessage() {
        return loginResultMessage;
    }

    public void setLoginResultMessage(LoginResultMessage loginResultMessage) {
        this.loginResultMessage = loginResultMessage;
    }

    public SignUpResultMessage getSignUpResultMessage() {
        return signUpResultMessage;
    }

    public void setSignUpResultMessage(SignUpResultMessage signUpResultMessage) {
        this.signUpResultMessage = signUpResultMessage;
    }

    public BattleResultMessage getBattleResultMessage() {
        return battleResultMessage;
    }

    public void setBattleResultMessage(BattleResultMessage battleResultMessage) {
        this.battleResultMessage = battleResultMessage;
    }

    public AttributeResultMessage getAttributeResultMessage() { return attributeResultMessage; }

    public void setAttributeResultMessage(AttributeResultMessage attributeResultMessage) {
        this.attributeResultMessage = attributeResultMessage;
    }

    public ShopResultMessage getShopResultMessage() {
        return shopResultMessage;
    }

    public void setShopResultMessage(ShopResultMessage shopResultMessage) {
        this.shopResultMessage = shopResultMessage;
    }

    public InventoryResultMessage getInventoryResultMessage() {
        return inventoryResultMessage;
    }

    public void setInventoryResultMessage(InventoryResultMessage inventoryResultMessage) {
        this.inventoryResultMessage = inventoryResultMessage;
    }

    public RedirectMessage getRedirectMessage() {
        return redirectMessage;
    }

    public void setRedirectMessage(RedirectMessage redirectMessage) {
        this.redirectMessage = redirectMessage;
    }

    public LevelUpResultMessage getLevelUpResultMessage() { return levelUpResultMessage; }

    public void setLevelUpResultMessage(LevelUpResultMessage levelUpResultMessage) {
        this.levelUpResultMessage = levelUpResultMessage;
    }

    public ReviveResultMessage getReviveResultMessage() { return reviveResultMessage; }

    public void setReviveResultMessage(ReviveResultMessage reviveResultMessage) {
        this.reviveResultMessage = reviveResultMessage;
    }
}
