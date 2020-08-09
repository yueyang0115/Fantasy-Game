package edu.duke.ece.fantasy.World;

import edu.duke.ece.fantasy.Annotation.Controller;
import edu.duke.ece.fantasy.Annotation.RequestMapping;
import edu.duke.ece.fantasy.GameContext;
import edu.duke.ece.fantasy.World.Message.PositionRequestMessage;
import edu.duke.ece.fantasy.database.WorldInfo;
import edu.duke.ece.fantasy.net.UserSession;

@Controller
public class WorldController {
    @RequestMapping
    public void ReqCoord(UserSession session, PositionRequestMessage msg){
        GameContext.getPositionUpdateHandler().handle(session,msg);
        session.getPlayer().setStatus(WorldInfo.MainWorld);
    }
}
