package edu.duke.ece.fantasy.json;

public class MessagesS2C {
    private PositionResultMessage positionResultMessage;
    private LoginResultMessage loginResultMessage;
    private SignUpResultMessage signUpResultMessage;
    private BattleResultMessage battleResultMessage;
    private ShopResultMessage shopResultMessage;
    private AttributeResultMessage attributeResultMessage;
    private InventoryResultMessage inventoryResultMessage;
    private BuildingResultMessage buildingResultMessage;

    public MessagesS2C(){ }

    public BuildingResultMessage getBuildingResultMessage() {
        return buildingResultMessage;
    }

    public void setBuildingResultMessage(BuildingResultMessage buildingResultMessage) {
        this.buildingResultMessage = buildingResultMessage;
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
}
