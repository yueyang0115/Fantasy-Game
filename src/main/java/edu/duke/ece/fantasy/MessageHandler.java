package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.json.*;
import org.hibernate.Session;

public class MessageHandler {
    private int wid;
    public MessageHandler() {}

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
                th.addTerritories(wid, positionMsg.getX(), positionMsg.getY());
                session.getTransaction().commit();
                session.beginTransaction();
                positionResultMessage.setTerritoryArray(th.getTerritories(wid, positionMsg.getX(), positionMsg.getY()));
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
