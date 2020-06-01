package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.Soldier;
import java.util.*;

public class BattleResultMessage {
    private List<Monster> monsters = new ArrayList<>();
    private List<Soldier> soldiers = new ArrayList<>();
    private String result;//"win","lose","continue","escaped"

    public BattleResultMessage() {
    }

    public BattleResultMessage(List<Monster> monsters, List<Soldier> soldiers, String result) {
        this.monsters = monsters;
        this.soldiers = soldiers;
        this.result = result;
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
}
