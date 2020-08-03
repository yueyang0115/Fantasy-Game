package edu.duke.ece.fantasy.Soldier;

import edu.duke.ece.fantasy.Soldier.Message.AttributeRequestMessage;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.SoldierDAO;
import edu.duke.ece.fantasy.Soldier.Message.AttributeResultMessage;

public class AttributeHandler {


    public AttributeResultMessage handle(AttributeRequestMessage request, int playerID){
        AttributeResultMessage result = new AttributeResultMessage();
        SoldierDAO sm = MetaDAO.getSoldierDAO();
        result.setSoldiers(sm.getSoldiers(playerID));
        return result;
    }
}
