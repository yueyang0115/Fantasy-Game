package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.Soldier;
import java.util.*;

public class BattleResultMessage {
    private List<Monster> monsters = new ArrayList<>(); //all monsters in the territory
    private List<Soldier> soldiers = new ArrayList<>(); //all soldiers the player has
    private String result; //status: "win","lose","continue","escaped","invalid"
    private BattleAction battleAction;

    /* unitIDs: records units's ID engaged in the battle, first sorted by unit's speed,
    the units will take turns to attack in the order of the list,
    first unitID in the list will be set as next round's attacker in next battleRequestMsg */
    private List<Integer> unitIDs = new ArrayList<>();

    public BattleResultMessage() {
    }

    public BattleResultMessage(List<Monster> monsters, List<Soldier> soldiers, String result, List<Integer> unitIDs) {
        this.monsters = monsters;
        this.soldiers = soldiers;
        this.result = result;
        this.unitIDs = unitIDs;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
    }

    public List<Soldier> getSoldiers() {
        return soldiers;
    }

    public void setSoldiers(List<Soldier> soldiers) {
        this.soldiers = soldiers;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Integer> getUnitIDs() { return unitIDs; }

    public void setUnitIDs(List<Integer> unitIDs) { this.unitIDs = unitIDs; }

    public BattleAction getBattleAction() { return battleAction; }

    public void setBattleAction(BattleAction battleAction) { this.battleAction = battleAction; }
}
