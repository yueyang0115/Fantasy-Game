package edu.duke.ece.fantasy.Building;

import edu.duke.ece.fantasy.Annotation.Controller;
import edu.duke.ece.fantasy.Annotation.RequestMapping;
import edu.duke.ece.fantasy.Building.Message.BuildingRequestMessage;
import edu.duke.ece.fantasy.Building.Message.ShopRequestMessage;
import edu.duke.ece.fantasy.GameContext;
import edu.duke.ece.fantasy.net.UserSession;

@Controller
public class BuildingController {
    @RequestMapping
    public void handleBuildingReq(UserSession session, BuildingRequestMessage msg){
        session.getPlayer().setStatus("IN_BUILDING");
        GameContext.getBuildingHandler().handle(session,msg);
    }

    @RequestMapping
    public void handleShopReq(UserSession session, ShopRequestMessage msg){
        session.getPlayer().setStatus("IN_BUILDING");
        GameContext.getShopHandler().handle(session,msg);
    }
}
