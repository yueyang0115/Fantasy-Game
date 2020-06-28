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
        //System.out.println("incoming message: " + input);

            if (loginMsg != null) {
                LoginHandler lh = new LoginHandler(metaDAO, sharedData);
                result.setLoginResultMessage(lh.handle(loginMsg));
            }

            if (signupMsg != null) {
                SignUpHandler sh = new SignUpHandler(metaDAO);
                result.setSignUpResultMessage(sh.handle(signupMsg));
            }

            if (positionMsg != null) {
                PositionUpdateHandler positionUpdateHandler = new PositionUpdateHandler(metaDAO);
                result.setPositionResultMessage(positionUpdateHandler.handle(sharedData.getPlayer().getWid(), positionMsg));
                // we add wid field in currentCoord from the client-sent-msg
                WorldCoord currentCoord  = positionMsg.getCurrentCoord();
                currentCoord.setWid(sharedData.getPlayer().getWid());
                // update player info in sharedData between taskScheduler and messageHandler
                sharedData.getPlayer().setStatus(Player.Status.INMAIN);
                sharedData.getPlayer().setCurrentCoord(currentCoord);
            }

            if (battleMsg != null) {
                sharedData.getPlayer().setStatus(Player.Status.INBATTLE);
                if (battleMsg.getTerritoryCoord() != null) battleMsg.getTerritoryCoord().setWid(sharedData.getPlayer().getWid());
                BattleResultMessage battleResult = myBattleHandler.handle(battleMsg, sharedData.getPlayer().getId(), metaDAO);
                result.setBattleResultMessage(battleResult);
            }

            if (attributeMsg != null) {
                AttributeHandler ah = new AttributeHandler(metaDAO);
                result.setAttributeResultMessage(ah.handle(attributeMsg, sharedData.getPlayer().getId()));
            }

            if (shopRequestMessage != null) {
                sharedData.getPlayer().setStatus(Player.Status.INBUILDING);
                ShopHandler shopHandler = new ShopHandler(metaDAO);
                if (shopRequestMessage.getCoord() != null) shopRequestMessage.getCoord().setWid(sharedData.getPlayer().getWid());
                result.setShopResultMessage(shopHandler.handle(shopRequestMessage, sharedData.getPlayer().getId()));
            }

            if(inventoryRequestMessage != null){
                sharedData.getPlayer().setStatus(Player.Status.INBAG);
                InventoryHandler inventoryHandler = new InventoryHandler(metaDAO);
                result.setInventoryResultMessage(inventoryHandler.handle(inventoryRequestMessage, sharedData.getPlayer().getId()));
            }

            if (buildingRequestMessage != null) {
                sharedData.getPlayer().setStatus(Player.Status.INMAIN);
                BuildingHandler buildingHandler = new BuildingHandler(metaDAO);
                if (buildingRequestMessage.getCoord() != null) buildingRequestMessage.getCoord().setWid(sharedData.getPlayer().getWid());
                result.setBuildingResultMessage(buildingHandler.handle(buildingRequestMessage, sharedData.getPlayer().getId()));
            }

        return result;
    }

//    public void sendResult(MessagesS2C result){
//        try {
//            this.TCPcm.send(result);
//            if (this.TCPcm.isClosed()){
//                return;
//            }
//            String result_str = "";
//            result_str = new ObjectMapper().writeValueAsString(result);
//            System.out.println("[DEBUG] TCPcommunicator successfully send " + result_str);
//        }
//        catch(IOException e){
//            e.printStackTrace();
//            if(this.TCPcm.isClosed()) {
//                System.out.println("[DEBUG] In messageHandler, Client socket might closed, prepare to exit");
//            }
//        }
//    }
}
