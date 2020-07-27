package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler {
    private BattleHandler myBattleHandler = new BattleHandler();
    private MetaDAO metaDAO;
    private SharedData sharedData;
    Logger log = LoggerFactory.getLogger(MessageHandler.class);

    public MessageHandler(MetaDAO metaDAO, SharedData sharedData) {
        this.metaDAO = metaDAO;
        this.sharedData = sharedData;
    }

    public MessagesS2C handle(MessagesC2S input) {
        MessagesS2C result = new MessagesS2C();
        LoginRequestMessage loginMsg = input.getLoginRequestMessage();
        SignUpRequestMessage signupMsg = input.getSignUpRequestMessage();
        PositionRequestMessage positionMsg = input.getPositionRequestMessage();
        BattleRequestMessage battleMsg = input.getBattleRequestMessage();
        AttributeRequestMessage attributeMsg = input.getAttributeRequestMessage();
        ShopRequestMessage shopRequestMessage = input.getShopRequestMessage();
        InventoryRequestMessage inventoryRequestMessage = input.getInventoryRequestMessage();
        BuildingRequestMessage buildingRequestMessage = input.getBuildingRequestMessage();
        LevelUpRequestMessage levelUpMsg = input.getLevelUpRequestMessage();
        RedirectMessage redirectMsg = input.getRedirectMessage();
        ReviveRequestMessage reviveMsg = input.getReviveRequestMessage();
        FriendRequestMessage friendRequestMessage = input.getFriendRequestMessage();

            // set redirectMsg and player's status
            if(redirectMsg != null){
                if(sharedData.getPlayer() != null) sharedData.getPlayer().setStatus(redirectMsg.getDestination());
                result.setRedirectMessage(redirectMsg);
            }

            if(friendRequestMessage != null){

            }

            if(reviveMsg != null){
                ReviveHandler rh = new ReviveHandler(metaDAO);
                result.setReviveResultMessage(rh.handle(sharedData.getPlayer().getId()));
            }

            if (loginMsg != null) {
                // if login succeed, sharedData will hold login-player's info
                LoginHandler lh = new LoginHandler(metaDAO, sharedData);
                result.setLoginResultMessage(lh.handle(loginMsg));
            }

            if (signupMsg != null) {
                SignUpHandler sh = new SignUpHandler(metaDAO);
                result.setSignUpResultMessage(sh.handle(signupMsg));
            }

            if (positionMsg != null) {
                PositionUpdateHandler positionUpdateHandler = new PositionUpdateHandler(metaDAO);
                result.setPositionResultMessage(positionUpdateHandler.handle(sharedData.getPlayer(), positionMsg));
                // received currentCoord in the request only hold x/y_coord, not hold wid
                // we add wid to it
                WorldCoord currentCoord  = positionMsg.getCurrentCoord();
                currentCoord.setWid(sharedData.getPlayer().getCurWorldId());
                // update player info in sharedData between taskScheduler and messageHandler
//                sharedData.getPlayer().setStatus("MAIN");
                sharedData.getPlayer().setCurrentCoord(currentCoord);
            }

            if (battleMsg != null) {
//                sharedData.getPlayer().setStatus(Player.Status.INBATTLE);
                // add wid to the received currentCoord in the request
                if (battleMsg.getTerritoryCoord() != null) battleMsg.getTerritoryCoord().setWid(sharedData.getPlayer().getCurWorldId());
                BattleResultMessage battleResult = myBattleHandler.handle(battleMsg, sharedData.getPlayer().getId(), metaDAO);
                result.setBattleResultMessage(battleResult);
                if(battleResult.getResult().equals("lose")) sharedData.getPlayer().setStatus(WorldInfo.DeathWorld);
//                if(battleMsg.getAction().equals("start")){
//                    RedirectMessage redirectMsg = new RedirectMessage();
//                    redirectMsg.setDestination("battle");
//                    result.setRedirectMessage(redirectMsg);
//                }
            }

            if(levelUpMsg != null){
//                sharedData.getPlayer().setStatus(Player.Status.INLEVELUP);
                LevelUpHandler luh = new LevelUpHandler(metaDAO);
                result.setLevelUpResultMessage(luh.handle(levelUpMsg));
//                if(levelUpMsg.getAction().equals("start")) {
//                    RedirectMessage redirectMsg = new RedirectMessage();
//                    redirectMsg.setDestination("levelup");
//                    result.setRedirectMessage(redirectMsg);
//                }
            }

            if (attributeMsg != null) {
                AttributeHandler ah = new AttributeHandler(metaDAO);
                result.setAttributeResultMessage(ah.handle(attributeMsg, sharedData.getPlayer().getId()));
            }

            if (shopRequestMessage != null) {
//                if(sharedData.getPlayer().getStatus()!=Player.Status.INBUILDING){ // make sure only send redirect once
//                    RedirectMessage redirectMessage = new RedirectMessage();
//                    redirectMessage.setDestination("shop");
//                    result.setRedirectMessage(redirectMessage);
//                }
//                sharedData.getPlayer().setStatus(Player.Status.INBUILDING);

                ShopHandler shopHandler = new ShopHandler(metaDAO);
                InventoryHandler inventoryHandler = new InventoryHandler(metaDAO);

                if (shopRequestMessage.getCoord() != null) shopRequestMessage.getCoord().setWid(sharedData.getPlayer().getCurWorldId());
                ShopResultMessage shopResultMessage = shopHandler.handle(shopRequestMessage, sharedData.getPlayer().getId());
                // add inventory result to shop
                InventoryRequestMessage shopInventoryRequestMessage = new InventoryRequestMessage();
                shopInventoryRequestMessage.setAction("list");

                result.setInventoryResultMessage(inventoryHandler.handle(shopInventoryRequestMessage, sharedData.getPlayer().getId()));
                result.setShopResultMessage(shopResultMessage);
            }

            if(inventoryRequestMessage != null){
//                if(sharedData.getPlayer().getStatus()!=Player.Status.INBAG){ // make sure only send redirect once
//                    RedirectMessage redirectMessage = new RedirectMessage();
//                    redirectMessage.setDestination("inventory");
//                    result.setRedirectMessage(redirectMessage);
//                }
//                sharedData.getPlayer().setStatus(Player.Status.INBAG);
                InventoryHandler inventoryHandler = new InventoryHandler(metaDAO);
                result.setInventoryResultMessage(inventoryHandler.handle(inventoryRequestMessage, sharedData.getPlayer().getId()));
                AttributeRequestMessage attributeRequestMessage = new AttributeRequestMessage();
                result.setAttributeResultMessage((new AttributeHandler(metaDAO)).handle(attributeRequestMessage, sharedData.getPlayer().getId()));
            }

            if (buildingRequestMessage != null) {
//                sharedData.getPlayer().setStatus(Player.Status.INMAIN);
                BuildingHandler buildingHandler = new BuildingHandler(metaDAO);
                if (buildingRequestMessage.getCoord() != null) buildingRequestMessage.getCoord().setWid(sharedData.getPlayer().getCurWorldId());
                result.setBuildingResultMessage(buildingHandler.handle(buildingRequestMessage, sharedData.getPlayer().getId()));
            }

        return result;
    }
}
