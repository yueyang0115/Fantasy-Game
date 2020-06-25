package edu.duke.ece.fantasy;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.*;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class MessageHandler {
    private int wid;
    private int playerID;
    private BattleHandler myBattleHandler = new BattleHandler();
    private WorldCoord[] currentCoord;
    private boolean[] canGenerateMonster;

    public MessageHandler(WorldCoord[] currentCoord, boolean[] canGenerateMonster) {
        this.currentCoord = currentCoord;
        this.canGenerateMonster = canGenerateMonster;
    }

    Logger log = LoggerFactory.getLogger(MessageHandler.class);
    ObjectMapper objectMapper = new ObjectMapper();

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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (loginMsg != null) {
                session.beginTransaction();
                LoginHandler lh = new LoginHandler(session);
                result.setLoginResultMessage(lh.handle(loginMsg));
                wid = result.getLoginResultMessage().getWid();
                playerID = result.getLoginResultMessage().getId();
                session.getTransaction().commit();
            }

            if (signupMsg != null) {
                session.beginTransaction();
                SignUpHandler sh = new SignUpHandler(session);
                result.setSignUpResultMessage(sh.handle(signupMsg));
                session.getTransaction().commit();
            }

            if (positionMsg != null) {
                session.beginTransaction();
                PositionUpdateHandler positionUpdateHandler = new PositionUpdateHandler(session);
                //PositionResultMessage positionResultMessage = new PositionResultMessage();
//                th.addTerritories(wid, positionMsg.getX(), positionMsg.getY());
//                log.info("wid is {} when handle positionMsg",wid);
                //positionResultMessage.setTerritoryArray(positionUpdateHandler.handle(wid, positionMsg));
                currentCoord[0] = positionMsg.getCurrentCoord();
                if(currentCoord[0] != null) currentCoord[0].setWid(wid);
                result.setPositionResultMessage(positionUpdateHandler.handle(wid, positionMsg));
                session.getTransaction().commit();
                canGenerateMonster[0] = true;
            }

            if (battleMsg != null) {
                canGenerateMonster[0] = false;
                session.beginTransaction();
                if (battleMsg.getTerritoryCoord() != null) battleMsg.getTerritoryCoord().setWid(this.wid);
                BattleResultMessage battleResult = myBattleHandler.handle(battleMsg, playerID, session);
                result.setBattleResultMessage(battleResult);
                session.getTransaction().commit();
            }

            if (attributeMsg != null) {
                session.beginTransaction();
                AttributeHandler ah = new AttributeHandler(session);
                result.setAttributeResultMessage(ah.handle(attributeMsg, playerID));
                session.getTransaction().commit();
            }

            if (shopRequestMessage != null) {
                canGenerateMonster[0] = false;
                session.beginTransaction();
                ShopHandler shopHandler = new ShopHandler(session);
                if (shopRequestMessage.getCoord() != null) shopRequestMessage.getCoord().setWid(this.wid);
                result.setShopResultMessage(shopHandler.handle(shopRequestMessage, playerID));
                session.getTransaction().commit();
            }

            if(inventoryRequestMessage != null){
                canGenerateMonster[0] = false;
                session.beginTransaction();
                InventoryHandler inventoryHandler = new InventoryHandler(session);
                result.setInventoryResultMessage(inventoryHandler.handle(inventoryRequestMessage, playerID));
                session.getTransaction().commit();
            }

            if (buildingRequestMessage != null) {
                session.beginTransaction();
                BuildingHandler buildingHandler = new BuildingHandler(session);
                if (buildingRequestMessage.getCoord() != null) buildingRequestMessage.getCoord().setWid(this.wid);
                result.setBuildingResultMessage(buildingHandler.handle(buildingRequestMessage, playerID));
                session.getTransaction().commit();
            }

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
