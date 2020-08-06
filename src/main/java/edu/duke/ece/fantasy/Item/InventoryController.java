package edu.duke.ece.fantasy.Item;

import edu.duke.ece.fantasy.Annotation.Controller;
import edu.duke.ece.fantasy.Annotation.RequestMapping;
import edu.duke.ece.fantasy.GameContext;
import edu.duke.ece.fantasy.Item.Message.InventoryRequestMessage;
import edu.duke.ece.fantasy.net.UserSession;

@Controller
public class InventoryController {
    @RequestMapping
    public void ReqInventory(UserSession session, InventoryRequestMessage msg) {
        GameContext.getInventoryHandler().handle(session, msg);
    }
}
