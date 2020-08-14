package edu.duke.ece.fantasy.Soldier;

import edu.duke.ece.fantasy.Annotation.Controller;
import edu.duke.ece.fantasy.Annotation.RequestMapping;
import edu.duke.ece.fantasy.GameContext;
import edu.duke.ece.fantasy.Soldier.Message.AttributeRequestMessage;
import edu.duke.ece.fantasy.Soldier.Message.LevelUpRequestMessage;
import edu.duke.ece.fantasy.Soldier.Message.ReviveRequestMessage;
import edu.duke.ece.fantasy.net.UserSession;

@Controller
public class SoldierController {
    @RequestMapping
    public void ReqAttribute(UserSession session, AttributeRequestMessage msg) {
        GameContext.getAttributeHandler().handle(session, msg);
    }

    @RequestMapping
    public void ReqLevelUp(UserSession session, LevelUpRequestMessage msg) {
        GameContext.getLevelUpHandler().handle(session, msg);
    }

    @RequestMapping
    public void ReqRevive(UserSession session) {
        GameContext.getReviveHandler().handle(session);
    }
}
