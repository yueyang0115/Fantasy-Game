package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.Account.LoginHandler;
import edu.duke.ece.fantasy.Account.SignUpHandler;

public class GameContext {
    private static LoginHandler loginHandler = new LoginHandler();
    private static SignUpHandler signUpHandler = new SignUpHandler();

    public static LoginHandler getLoginHandler() {
        return loginHandler;
    }

    public static SignUpHandler getSignUpHandler() {
        return signUpHandler;
    }
}
