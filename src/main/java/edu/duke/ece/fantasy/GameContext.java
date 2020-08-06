package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.Account.LoginHandler;
import edu.duke.ece.fantasy.Account.SignUpHandler;
import edu.duke.ece.fantasy.World.PositionUpdateHandler;

public class GameContext {
    private static LoginHandler loginHandler = new LoginHandler();
    private static SignUpHandler signUpHandler = new SignUpHandler();
    private static PositionUpdateHandler positionUpdateHandler = new PositionUpdateHandler();

    public static LoginHandler getLoginHandler() {
        return loginHandler;
    }

    public static SignUpHandler getSignUpHandler() {
        return signUpHandler;
    }

    public static PositionUpdateHandler getPositionUpdateHandler() {
        return positionUpdateHandler;
    }
}
