package edu.duke.ece.fantacy;

import org.json.JSONObject;

public class LoginHandler {
    private JSONObject login_obj;
    private String type;
    private String username;
    private String password;
    private JSONObject result_obj;
    private boolean loginStatus;
    //private MockDBprocessor myMockDBprocessor;
    private DBprocessor myDBprocessor;
    private int wid;

    LoginHandler(String login_msg, DBprocessor processor){
        this.login_obj = new JSONObject(login_msg);
        this.type = login_obj.optString("type");
        this.username = login_obj.optString("username");
        this.password = login_obj.optString("password");
        this.result_obj = new JSONObject();
        this.loginStatus = false;
        //this.myMockDBprocessor = processor;
        this.myDBprocessor = processor;
        handle();
    }

    private void handle(){
        result_obj.put("type", type);
        if(type!=null && username !=null && password != null){
            if(type.equals("signup")){
                handleSignUp();
            }
            if(type.equals("login")){
                handleLogin();
            }
        }
        else{
            handleException();
        }
    }

    private void handleSignUp(){
        //checkUser:  >0: return wid, -1 : username doesn't exist, -2 : wrong password / username
        //int checkUser = myMockDBprocessor.checkUser(username, password);
        int checkUser = myDBprocessor.checkUser(username, password);

        if(checkUser==-1) {
            //username doesn't exit, can add to database
            //myMockDBprocessor.addUser(username, password, wid);
            myDBprocessor.addUser(username, password, wid);
            result_obj.put("status", "success");
            result_obj.put("error_msg","null");
            System.out.println("Signup succeed");
        }
        else{
            result_obj.put("status", "fail");
            result_obj.put("error_msg", "SignUp failed, username already exist");
            System.out.println("Signup failed, username already exist");
        }
    }

    private void handleLogin(){
        //checkUser:  >0: return wid, -1 : username doesn't exist, -2 : wrong password / username
        //int checkUser = myMockDBprocessor.checkUser(username, password);
        int checkUser = myDBprocessor.checkUser(username, password);

        if(checkUser >= 0){
            result_obj.put("status", "success");
            result_obj.put("wid",checkUser);
            result_obj.put("error_msg","null");
            loginStatus = true;
            System.out.println("login success");
        }
        else if(checkUser == -1){
            result_obj.put("status", "fail");
            result_obj.put("error_msg","LogIn failed, username doesn't exist");
            System.out.println("login failed, username doesn't exist");
        }
        else if(checkUser == -2){
            result_obj.put("status", "fail");
            result_obj.put("error_msg","LogIn failed, wrong password/username");
            System.out.println("login failed, wrong password/username");
        }
    }

    private void handleException(){
        result_obj.put("status","fail");
        result_obj.put("error_msg","LogIn/SignUp failed, invalid operation");
        System.out.println("failed, invalid operation");
    }

    public boolean getLoginStatus(){
        return loginStatus;
    }

    public String getLoginResult(){
        return result_obj.toString();
    }

    public int getWid(){
        return myDBprocessor.checkUser(username,password);
    }
}
