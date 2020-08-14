package edu.duke.ece.fantasy.Battle.Message;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.duke.ece.fantasy.Account.CmdAccount;
import edu.duke.ece.fantasy.Battle.CmdBattle;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.net.Message;
import edu.duke.ece.fantasy.net.MessageMeta;
import edu.duke.ece.fantasy.net.Modules;

@MessageMeta(module = Modules.BATTLE, cmd = CmdBattle.REQ_BATTLE)
public class BattleRequestMessage extends Message {
    private WorldCoord territoryCoord; //territoryCoord includes: x,y,wid
    private String action;//"attack" "escape" "start"
    private BattleAction battleAction; //including attackerID, attackeeID, action("normal, magical")

    public BattleRequestMessage() {
    }

    public BattleRequestMessage(WorldCoord territoryCoord, String action, BattleAction battleAction) {
        this.territoryCoord = territoryCoord;
        this.action = action;
        this.battleAction = battleAction;
    }

    public WorldCoord getTerritoryCoord() { return territoryCoord; }

    public void setTerritoryCoord(WorldCoord territoryCoord) { this.territoryCoord = territoryCoord; }

    public BattleAction getBattleAction() {
        return battleAction;
    }

    public void setBattleAction(BattleAction battleAction) {
        this.battleAction = battleAction;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
