package edu.duke.ece.fantacy;

import edu.duke.ece.fantacy.json.LoginRequestMessage;
import edu.duke.ece.fantacy.json.LoginResultMessage;
import org.json.JSONObject;

public class LoginHandler {
    private DBprocessor myDBprocessor;

    LoginHandler(DBprocessor dbp){
        this.myDBprocessor = dbp;
    }

    public LoginResultMessage handle(LoginRequestMessage input){
        LoginResultMessage result = new LoginResultMessage();
        String username = input.getUsername();
        String password = input.getPassword();

        //checkUser:  >0: return wid, -1 : username doesn't exist or wrong password / username
        int checkUser = myDBprocessor.checkUser(username, password);

        if(checkUser >= 0){
            result.setStatus("success");
            result.setWid(checkUser);
            System.out.println("[DEBUG] Login success");
        }
        else{
            result.setStatus("fail");
            result.setStatus("LogIn failed, wrong password/username");
            System.out.println("[DEBUG] Login failed, wrong password/username");
        }
        return result;
    }
}
