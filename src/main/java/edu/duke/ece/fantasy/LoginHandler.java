package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.json.LoginRequestMessage;
import edu.duke.ece.fantasy.json.LoginResultMessage;
import org.hibernate.Session;

public class LoginHandler {
    private UserHandler userHandler;

    LoginHandler(Session session){
        this.userHandler = new UserHandler(session);
    }

    public LoginResultMessage handle(LoginRequestMessage input){
        LoginResultMessage result = new LoginResultMessage();
        String username = input.getUsername();
        String password = input.getPassword();

        Player player = userHandler.getUser(username,password);

        if(player != null){
            result.setStatus("success");
            result.setWid(player.getWid());
            System.out.println("[DEBUG] Login success");
        }
        else{
            result.setStatus("fail");
            result.setError_msg("LogIn failed, wrong password/username");
            System.out.println("[DEBUG] Login failed, wrong password/username");
        }
        return result;
    }
}
