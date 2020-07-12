package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.database.levelUp.Skill;

import java.util.Set;

public class LevelUpRequestMessage {
    private String action; // "start" "choose"
    private int unitID; // id of the unit which wants to be leveled up
    private Set<Skill> skills; // the skills that wants to be added/updated for the unit
}
