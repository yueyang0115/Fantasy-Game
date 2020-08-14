package edu.duke.ece.fantasy.Friend;

import edu.duke.ece.fantasy.Annotation.Controller;
import edu.duke.ece.fantasy.Annotation.RequestMapping;
import edu.duke.ece.fantasy.Friend.Message.FriendRequestMessage;
import edu.duke.ece.fantasy.GameContext;
import edu.duke.ece.fantasy.net.UserSession;

@Controller
public class FriendController {
    @RequestMapping
    public void handleFriendReq(UserSession session, FriendRequestMessage friendRequestMessage) {
        session.getPlayer().setStatus("IN_FRIEND");
        GameContext.getFriendHandler().handle(session, friendRequestMessage);
    }
}
