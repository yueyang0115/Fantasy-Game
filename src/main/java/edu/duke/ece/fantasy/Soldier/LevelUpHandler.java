package edu.duke.ece.fantasy.Soldier;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.SkillDAO;
import edu.duke.ece.fantasy.database.DAO.UnitDAO;
import edu.duke.ece.fantasy.database.Unit;
import edu.duke.ece.fantasy.Soldier.Message.LevelUpRequestMessage;
import edu.duke.ece.fantasy.Soldier.Message.LevelUpResultMessage;
import edu.duke.ece.fantasy.net.UserSession;

public class LevelUpHandler {
    private UnitDAO unitDAO;
    private SkillDAO skillDAO;

    public LevelUpHandler(){
    }

    public LevelUpResultMessage handle(UserSession session, LevelUpRequestMessage request){
        this.unitDAO = session.getMetaDAO().getUnitDAO();
        this.skillDAO = session.getMetaDAO().getSkillDAO();

        if(request.getAction().equals("start")){
            return doStart(session, request);
        }
        if(request.getAction().equals("choose")){
            return doChoose(session, request);
        }
        return null;
    }

    public LevelUpResultMessage doStart(UserSession session, LevelUpRequestMessage request){
        LevelUpResultMessage result = new LevelUpResultMessage();
        Unit unit = unitDAO.getUnit(request.getUnitID());

        result.setUnit(unit);
        result.setAvailableSkills(skillDAO.getAvailableSkills(unit));
        result.setResult("start");
        session.sendMsg(result);
        return result;
    }

    public LevelUpResultMessage doChoose(UserSession session, LevelUpRequestMessage request){
        LevelUpResultMessage result = new LevelUpResultMessage();
        int unitID = request.getUnitID();
        //valid whether the player has the required level and required Skill to add that skill
        String name = request.getSkill().getName();
        boolean valid = unitDAO.addSkill(unitID, name);
        if(valid == true) result.setResult("success");
        else result.setResult("fail");

        Unit unit = unitDAO.getUnit(unitID);
        result.setUnit(unit);
        result.setAvailableSkills(skillDAO.getAvailableSkills(unit));
        session.sendMsg(result);
        return result;
    }

}
