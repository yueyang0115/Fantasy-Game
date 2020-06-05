package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.*;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler {
    private int wid;
    private int playerID;
    public MessageHandler() {}
    Logger log = LoggerFactory.getLogger(MessageHandler.class);

    public MessagesS2C handle(MessagesC2S input) {
        MessagesS2C result = new MessagesS2C();
        LoginRequestMessage loginMsg = input.getLoginRequestMessage();
        SignUpRequestMessage signupMsg = input.getSignUpRequestMessage();
        PositionRequestMessage positionMsg = input.getPositionRequestMessage();
        BattleRequestMessage battleMsg = input.getBattleRequestMessage();
        AttributeRequestMessage attributeMsg = input.getAttributeRequestMessage();

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
                positionResultMessage.setTerritoryArray(positionUpdateHandler.handle(wid, positionMsg.getX(), positionMsg.getY(),3,3));
                result.setPositionResultMessage(positionResultMessage);

            }else if(battleMsg != null){
                BattleHandler bh = new BattleHandler(session);
                result.setBattleResultMessage(bh.handle(battleMsg, playerID));
            }

            else if(attributeMsg != null){
                AttributeHandler ah = new AttributeHandler(session);
                result.setAttributeResultMessage(ah.handle(attributeMsg, playerID));
            }

            else {

            }
            session.getTransaction().commit();
            //session.close();
        }
        return result;
    }

    public int getWid() {
        return this.wid;
    }
}
