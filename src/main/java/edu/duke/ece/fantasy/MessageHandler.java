package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.json.*;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler {
    private int wid;
    public MessageHandler() {}
    Logger log = LoggerFactory.getLogger(MessageHandler.class);

    public MessagesS2C handle(MessagesC2S input) {
        MessagesS2C result = new MessagesS2C();
        LoginRequestMessage loginMsg = input.getLoginRequestMessage();
        SignUpRequestMessage signupMsg = input.getSignUpRequestMessage();
        PositionRequestMessage positionMsg = input.getPositionRequestMessage();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            if (loginMsg != null) {
                LoginHandler lh = new LoginHandler(session);
                result.setLoginResultMessage(lh.handle(loginMsg));
                wid = result.getLoginResultMessage().getWid();
            } else if (signupMsg != null) {
                SignUpHandler sh = new SignUpHandler(session);
                result.setSignUpResultMessage(sh.handle(signupMsg));
            } else if (positionMsg != null) {
                TerritoryHandler th = new TerritoryHandler(session);
                PositionResultMessage positionResultMessage = new PositionResultMessage();
//                th.addTerritories(wid, positionMsg.getX(), positionMsg.getY());
//                log.info("wid is {} when handle positionMsg",wid);
                positionResultMessage.setTerritoryArray(th.handle(wid, positionMsg.getX(), positionMsg.getY(),1,1));
                result.setPositionResultMessage(positionResultMessage);
            } else {

            }
            session.getTransaction().commit();
        }
        return result;
    }

    public int getWid() {
        return this.wid;
    }
}
