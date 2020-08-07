package edu.duke.ece.fantasy.Soldier;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.SoldierDAO;
import edu.duke.ece.fantasy.database.DAO.UnitDAO;
import edu.duke.ece.fantasy.database.Soldier;
import edu.duke.ece.fantasy.Soldier.Message.ReviveResultMessage;
import edu.duke.ece.fantasy.net.UserSession;

import java.util.List;

public class ReviveHandler {
    private SoldierDAO soldierDAO;
    private UnitDAO unitDAO;

    public ReviveHandler() {
    }

    public ReviveResultMessage handle(UserSession session){
        this.soldierDAO = session.getMetaDAO().getSoldierDAO();
        this.unitDAO = session.getMetaDAO().getUnitDAO();

        List<Soldier> soldierList = soldierDAO.getSoldiers(session.getPlayer().getId());
        for(Soldier s : soldierList) unitDAO.setUnitHp(s.getId(), 1);
        ReviveResultMessage result = new ReviveResultMessage();
        result.setResult("success");
        session.sendMsg(result);
        return result;
    }
}
