package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.database.Monster;
import edu.duke.ece.fantasy.database.Soldier;
import java.util.*;

public class BattleResultMessage {
    private BattleInitInfo battleInitInfo;
    private String result; //status: "win","lose","continue","escaped","invalid"
    private List<BattleAction> actions;

    public BattleResultMessage() {
    }

    public BattleResultMessage(BattleInitInfo battleInitInfo, String result, List<BattleAction> actions) {
        this.battleInitInfo = battleInitInfo;
        this.result = result;
        this.actions = actions;
    }

    public BattleInitInfo getBattleInitInfo() {
        return battleInitInfo;
    }

    public void setBattleInitInfo(BattleInitInfo battleInitInfo) {
        this.battleInitInfo = battleInitInfo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<BattleAction> getActions() {
        return actions;
    }

    public void setActions(List<BattleAction> actions) { this.actions = actions; }
}
