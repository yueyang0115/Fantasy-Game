package edu.duke.ece.fantasy.Battle.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.net.Message;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BattleResultMessage extends Message {
    // battleInitInfo is set only when request is "start"
    private BattleInitInfo battleInitInfo;
    // final battle status: "win","lose","continue","escaped"
    private String result;
    // details of each round's battle, including each round's attacker and attackee, result and units order in next round
    // since monster can attack soldier back, one request can has more than one battle action
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
