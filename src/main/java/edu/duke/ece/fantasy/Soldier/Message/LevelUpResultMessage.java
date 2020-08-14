package edu.duke.ece.fantasy.Soldier.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.database.Unit;
import edu.duke.ece.fantasy.database.levelUp.Skill;
import edu.duke.ece.fantasy.net.Message;
import edu.duke.ece.fantasy.net.MessageMeta;
import edu.duke.ece.fantasy.net.Modules;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@MessageMeta(module = Modules.SOLDIER, cmd = CmdSoldier.RES_LEVELUP)
public class LevelUpResultMessage extends Message {
    private String result; // "success" "fail"
    private Set<Skill> availableSkills; // available skills that can be added/updated
    private Unit unit; // updated unit after level up, field set when action is "choose"

    public LevelUpResultMessage(){}

    public LevelUpResultMessage(String result, Set<Skill> availableSkills, Unit unit) {
        this.result = result;
        this.availableSkills = availableSkills;
        this.unit = unit;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Set<Skill> getAvailableSkills() {
        return availableSkills;
    }

    public void setAvailableSkills(Set<Skill> availableSkills) {
        this.availableSkills = availableSkills;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
