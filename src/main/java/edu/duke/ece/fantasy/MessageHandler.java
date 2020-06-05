package edu.duke.ece.fantasy;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.*;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler {
    private int wid;
    private int playerID;
    private BattleHandler myBattleHandler = new BattleHandler();

    public MessageHandler() {
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

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            if (loginMsg != null) {
                LoginHandler lh = new LoginHandler(session);
                result.setLoginResultMessage(lh.handle(loginMsg));
                wid = result.getLoginResultMessage().getWid();
                playerID = result.getLoginResultMessage().getId();
            } else if (signupMsg != null) {
                SignUpHandler sh = new SignUpHandler(session);
                result.setSignUpResultMessage(sh.handle(signupMsg));

            } else if (positionMsg != null) {
                PositionUpdateHandler positionUpdateHandler = new PositionUpdateHandler(session);
                PositionResultMessage positionResultMessage = new PositionResultMessage();
//                th.addTerritories(wid, positionMsg.getX(), positionMsg.getY());
//                log.info("wid is {} when handle positionMsg",wid);
                positionResultMessage.setTerritoryArray(positionUpdateHandler.handle(wid, positionMsg.getX(), positionMsg.getY(), 3, 3));
                result.setPositionResultMessage(positionResultMessage);

            } else if (battleMsg != null) {
                result.setBattleResultMessage(myBattleHandler.handle(battleMsg, playerID, session));
            } else if (attributeMsg != null) {
                AttributeHandler ah = new AttributeHandler(session);
                result.setAttributeResultMessage(ah.handle(attributeMsg, playerID));
            } else if (shopRequestMessage != null) {
                ShopHandler shopHandler = new ShopHandler(session);
                result.setShopResultMessage(shopHandler.handle(shopRequestMessage));
            } else {

            }
            session.getTransaction().commit();
            try {
                String tmp = objectMapper.writeValueAsString(result); // fix lazy initialization problem
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    public int getWid() {
        return this.wid;
    }
}
