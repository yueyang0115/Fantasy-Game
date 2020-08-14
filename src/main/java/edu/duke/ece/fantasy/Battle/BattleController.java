package edu.duke.ece.fantasy.Battle;

import edu.duke.ece.fantasy.Annotation.RequestMapping;
import edu.duke.ece.fantasy.Battle.Message.BattleRequestMessage;
import edu.duke.ece.fantasy.GameContext;
import edu.duke.ece.fantasy.net.UserSession;

public class BattleController {
    @RequestMapping
    public void ReqBattle(UserSession session, BattleRequestMessage msg){
        session.getPlayer().setStatus("IN_BATTLE");
        GameContext.getBattleHandler().handle(session, msg);
    }
}
