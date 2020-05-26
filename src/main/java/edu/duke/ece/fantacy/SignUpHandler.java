package edu.duke.ece.fantacy;

import edu.duke.ece.fantacy.json.SignUpRequestMessage;
import edu.duke.ece.fantacy.json.SignUpResultMessage;

public class SignUpHandler {
    private DBprocessor myDBprocessor;
    private int wid;

    public SignUpHandler(DBprocessor dbp, int id){
        this.myDBprocessor = dbp;
        this.wid = id;
    }

    public SignUpResultMessage handle(SignUpRequestMessage input){
        SignUpResultMessage result = new SignUpResultMessage();
        String username = input.getUsername();
        String password = input.getPassword();

        //checkUser:  >0: return wid, -1 : username doesn't exist or wrong password / username
        int checkUser = myDBprocessor.checkUser(username, password);
        if(checkUser == -1) {
            //username doesn't exit, can add to database
            myDBprocessor.addUser(username, password, wid);
            result.setStatus("success");
            System.out.println("[DEBUG] SignUp succeed");
        }
        else{
            result.setStatus("fail");
            result.setError_msg("SignUp failed, username already exist");
            System.out.println("[DEBUG] SignUp failed, username already exist");
        }
        return result;
    }
}
