package edu.duke.ece.fantasy.Soldier;

import edu.duke.ece.fantasy.Soldier.Message.AttributeRequestMessage;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.SoldierDAO;
import edu.duke.ece.fantasy.Soldier.Message.AttributeResultMessage;
import edu.duke.ece.fantasy.net.UserSession;

public class AttributeHandler {
    public void handle(UserSession session, AttributeRequestMessage request){
        AttributeResultMessage result = new AttributeResultMessage();
        SoldierDAO sm = MetaDAO.getSoldierDAO();
        result.setSoldiers(sm.getSoldiers(session.getPlayer().getId()));
        session.sendMsg(result);
    }
}
