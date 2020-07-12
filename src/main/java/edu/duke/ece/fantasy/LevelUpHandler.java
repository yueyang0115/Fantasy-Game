package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.SkillDAO;
import edu.duke.ece.fantasy.database.DAO.UnitDAO;
import edu.duke.ece.fantasy.database.Unit;
import edu.duke.ece.fantasy.json.LevelUpRequestMessage;
import edu.duke.ece.fantasy.json.LevelUpResultMessage;

public class LevelUpHandler {
    private MetaDAO metaDAO;
    private UnitDAO unitDAO;
    private SkillDAO skillDAO;

    public LevelUpHandler(MetaDAO metaDAO){
        this.metaDAO = metaDAO;
        this.unitDAO = metaDAO.getUnitDAO();
        this.skillDAO = metaDAO.getSkillDAO();
    }

    public LevelUpResultMessage handle(LevelUpRequestMessage request){
        if(request.getAction().equals("start")){
            return doStart(request);
        }
        if(request.getAction().equals("choose")){
            return doChoose(request);
        }
        return null;
    }

    public LevelUpResultMessage doStart(LevelUpRequestMessage request){
        LevelUpResultMessage result = new LevelUpResultMessage();
        Unit unit = unitDAO.getUnit(request.getUnitID());

        result.setUnit(unit);
        result.setAvailableSkills(skillDAO.getAvailableSkills(unit));
        result.setResult("start");
        return result;
    }

    public LevelUpResultMessage doChoose(LevelUpRequestMessage request){
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
        return result;
    }

}
