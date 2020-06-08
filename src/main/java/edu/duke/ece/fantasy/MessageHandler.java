package edu.duke.ece.fantasy;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.*;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class MessageHandler {
    private int wid;
    private int playerID;
    private BattleHandler myBattleHandler = new BattleHandler();
    private TCPCommunicator TCPcm;

    public MessageHandler() {
    }

    public MessageHandler(TCPCommunicator TCPcm) {
        this.TCPcm = TCPcm;
    }

    Logger log = LoggerFactory.getLogger(MessageHandler.class);
    ObjectMapper objectMapper = new ObjectMapper();

    public void handle(MessagesC2S input) {
        MessagesS2C result = new MessagesS2C();
        LoginRequestMessage loginMsg = input.getLoginRequestMessage();
        SignUpRequestMessage signupMsg = input.getSignUpRequestMessage();
        PositionRequestMessage positionMsg = input.getPositionRequestMessage();
        BattleRequestMessage battleMsg = input.getBattleRequestMessage();
        AttributeRequestMessage attributeMsg = input.getAttributeRequestMessage();
        ShopRequestMessage shopRequestMessage = input.getShopRequestMessage();
        InventoryRequestMessage inventoryRequestMessage = input.getInventoryRequestMessage();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            if (loginMsg != null) {
                LoginHandler lh = new LoginHandler(session);
                result.setLoginResultMessage(lh.handle(loginMsg));
                wid = result.getLoginResultMessage().getWid();
                playerID = result.getLoginResultMessage().getId();
                sendResult(result);
            }

            if (signupMsg != null) {
                SignUpHandler sh = new SignUpHandler(session);
                result.setSignUpResultMessage(sh.handle(signupMsg));
                sendResult(result);
            }

            if (positionMsg != null) {
                PositionUpdateHandler positionUpdateHandler = new PositionUpdateHandler(session);
                PositionResultMessage positionResultMessage = new PositionResultMessage();
//                th.addTerritories(wid, positionMsg.getX(), positionMsg.getY());
//                log.info("wid is {} when handle positionMsg",wid);
                positionResultMessage.setTerritoryArray(positionUpdateHandler.handle(wid, positionMsg.getX(), positionMsg.getY(), 3, 3));
                result.setPositionResultMessage(positionResultMessage);
                sendResult(result);
            }

            if (battleMsg != null) {
                List<BattleResultMessage> battleResults = myBattleHandler.handle(battleMsg, playerID, session);
                for(BattleResultMessage r: battleResults) {
                    result.setBattleResultMessage(r);
                    sendResult(result);
                }
            }

            if (attributeMsg != null) {
                AttributeHandler ah = new AttributeHandler(session);
                result.setAttributeResultMessage(ah.handle(attributeMsg, playerID));
                sendResult(result);
            }

            if (shopRequestMessage != null) {
                ShopHandler shopHandler = new ShopHandler(session);
                result.setShopResultMessage(shopHandler.handle(shopRequestMessage, playerID));
                sendResult(result);
            }

            if(inventoryRequestMessage != null){
                InventoryHandler inventoryHandler = new InventoryHandler(session);
                result.setInventoryResultMessage(inventoryHandler.handle(playerID));
                sendResult(result);
            }

            session.getTransaction().commit();
            try {
                String tmp = objectMapper.writeValueAsString(result); // fix lazy initialization problem
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
//        return result;
    }

    public void sendResult(MessagesS2C result){
        try {
            this.TCPcm.send(result);
            if (this.TCPcm.isClosed()) return;
            String result_str = "";
            result_str = new ObjectMapper().writeValueAsString(result);
            System.out.println("[DEBUG] TCPcommunicator successfully send " + result_str);
        }
        catch(IOException e){
            e.printStackTrace();
            if(this.TCPcm.isClosed()) {
                System.out.println("[DEBUG] Client socket might closed, prepare to exit");
            }
        }
    }

    public int getWid() {
        return this.wid;
    }
}
