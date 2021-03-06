package edu.duke.ece.fantasy.Soldier.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.database.levelUp.Skill;
import edu.duke.ece.fantasy.net.Message;
import edu.duke.ece.fantasy.net.MessageMeta;
import edu.duke.ece.fantasy.net.Modules;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@MessageMeta(module = Modules.SOLDIER, cmd = CmdSoldier.REQ_LEVELUP)
public class LevelUpRequestMessage extends Message {
    private String action; // "start" "choose"
    private int unitID; // id of the unit which wants to be leveled up
    private Skill skill; // the skill that wants to be added/updated for the unit

    public LevelUpRequestMessage(){}

    public LevelUpRequestMessage(String action, int unitID, Skill skill) {
        this.action = action;
        this.unitID = unitID;
        this.skill = skill;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getUnitID() { return unitID; }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }
}
