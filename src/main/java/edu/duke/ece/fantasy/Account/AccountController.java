package edu.duke.ece.fantasy.Account;

import edu.duke.ece.fantasy.Account.Message.LoginRequestMessage;
import edu.duke.ece.fantasy.Account.Message.SignUpRequestMessage;
import edu.duke.ece.fantasy.Annotation.Controller;
import edu.duke.ece.fantasy.Annotation.RequestMapping;
import edu.duke.ece.fantasy.GameContext;
import edu.duke.ece.fantasy.net.UserSession;

@Controller
public class AccountController {

    @RequestMapping
    public void ReqLogin(UserSession session, LoginRequestMessage msg){
        GameContext.getLoginHandler().handle(session,msg);
    }

    @RequestMapping
    public void ReqSignUp(UserSession session, SignUpRequestMessage msg){
        GameContext.getSignUpHandler().handle(session,msg);
    }
}
