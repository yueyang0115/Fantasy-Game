package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.database.Unit;
import edu.duke.ece.fantasy.database.levelUp.Skill;
import java.util.Set;

public class LevelUpResultMessage {
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
