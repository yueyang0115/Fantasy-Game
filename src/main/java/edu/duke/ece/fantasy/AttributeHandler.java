package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.SoldierManger;
import edu.duke.ece.fantasy.json.AttributeRequestMessage;
import edu.duke.ece.fantasy.json.AttributeResultMessage;
import org.hibernate.Session;

public class AttributeHandler {
    private Session session;

    public AttributeHandler(Session session) {
        this.session = session;
    }

    public AttributeResultMessage handle(AttributeRequestMessage request, int playerID){
        AttributeResultMessage result = new AttributeResultMessage();
        SoldierManger sm = new SoldierManger(session);
        result.setSoldiers(sm.getSoldiers(playerID));
        return result;
    }
}
