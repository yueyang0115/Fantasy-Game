package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.DAO.SoldierDAO;
import edu.duke.ece.fantasy.database.DAO.UnitDAO;
import edu.duke.ece.fantasy.database.Soldier;
import edu.duke.ece.fantasy.json.ReviveRequestMessage;
import edu.duke.ece.fantasy.json.ReviveResultMessage;

import java.util.List;

public class ReviveHandler {
    private SoldierDAO soldierDAO;
    private UnitDAO unitDAO;

    public ReviveHandler(MetaDAO metaDAO) {
        this.soldierDAO = metaDAO.getSoldierDAO();
        this.unitDAO = metaDAO.getUnitDAO();
    }

    public ReviveResultMessage handle(int playerID){
        List<Soldier> soldierList = soldierDAO.getSoldiers(playerID);
        for(Soldier s : soldierList) unitDAO.setUnitHp(s.getId(), 1);
        ReviveResultMessage result = new ReviveResultMessage();
        result.setResult("success");
        return result;
    }
}
