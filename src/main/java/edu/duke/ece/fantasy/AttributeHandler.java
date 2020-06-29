package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.SoldierDAO;
import edu.duke.ece.fantasy.json.AttributeRequestMessage;
import edu.duke.ece.fantasy.json.AttributeResultMessage;
import org.hibernate.Session;

public class AttributeHandler {
    private MetaDAO metaDAO;

    public AttributeHandler(MetaDAO metaDAO) {
        this.metaDAO = metaDAO;
    }

    public AttributeResultMessage handle(AttributeRequestMessage request, int playerID){
        AttributeResultMessage result = new AttributeResultMessage();
        SoldierDAO sm = metaDAO.getSoldierDAO();
        result.setSoldiers(sm.getSoldiers(playerID));
        return result;
    }
}
