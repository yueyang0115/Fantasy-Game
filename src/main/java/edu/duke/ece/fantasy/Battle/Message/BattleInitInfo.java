package edu.duke.ece.fantasy.Battle.Message;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.Account.CmdAccount;
import edu.duke.ece.fantasy.Battle.CmdBattle;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.net.Message;
import edu.duke.ece.fantasy.net.MessageMeta;
import edu.duke.ece.fantasy.net.Modules;

public class BattleInitInfo extends Message {
    private List<Monster> monsters;  //all monsters in the territory
    private List<Soldier> soldiers;  //all soldiers the player has
    private List<Integer> units;
    /* unitIDs: records units's ID engaged in the battle, first sorted by unit's speed,
    the units will take turns to attack in the order of the list,
    first unitID in the list will be set as next round's attacker in next battleRequestMsg */

    public BattleInitInfo() {
    }

    public BattleInitInfo(List<Monster> monsters, List<Soldier> soldiers, List<Integer> units) {
        this.monsters = monsters;
        this.soldiers = soldiers;
        this.units = units;
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

    public List<Integer> getUnits() {
        return units;
    }

    public void setUnits(List<Integer> units) {
        this.units = units;
    }
}
