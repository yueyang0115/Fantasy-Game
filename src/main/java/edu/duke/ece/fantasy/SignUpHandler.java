package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.json.SignUpRequestMessage;
import edu.duke.ece.fantasy.json.SignUpResultMessage;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUpHandler {
    private UserHandler userHandler;
    Logger logger = LoggerFactory.getLogger(SignUpHandler.class);

    public SignUpHandler(Session session){
        this.userHandler = new UserHandler(session);
    }

    public SignUpResultMessage handle(SignUpRequestMessage input){
        SignUpResultMessage result = new SignUpResultMessage();
        String username = input.getUsername();
        String password = input.getPassword();

        //checkUser:  >0: return wid, -1 : username doesn't exist or wrong password / username
//        int checkUser = myDBprocessor.checkUser(username, password);
        Player player = userHandler.getUser(username,password);
        if(player == null){
            userHandler.addUser(username,password);
            result.setStatus("success");
            logger.debug("[DEBUG] SignUp succeed");
        } else {
            result.setStatus("fail");
            result.setError_msg("SignUp failed, username already exist");
            logger.debug("[DEBUG] SignUp failed, username already exist");
        }

        return result;
    }
}
