package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.database.Unit;
import edu.duke.ece.fantasy.database.levelUp.Skill;
import java.util.Set;

public class LevelUpResultMessage {
    private String result; // "success" "fail"
    private Set<Skill> availableSkills; //available skills that can be added/updated, field set when action is "start"
    private Unit unit; //updated unit after level up, field set when action is "choose"

}
