package edu.duke.ece.fantasy.Account;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.Account.Message.LoginRequestMessage;
import edu.duke.ece.fantasy.Account.Message.LoginResultMessage;
import edu.duke.ece.fantasy.net.UserSession;

public class LoginHandler {
    private PlayerDAO playerDAO;

    public LoginHandler(){
        this.playerDAO = MetaDAO.getPlayerDAO();
    }

    public LoginResultMessage handle(UserSession userSession,LoginRequestMessage input){
        LoginResultMessage result = new LoginResultMessage();
        String username = input.getUsername();
        String password = input.getPassword();

        Player player = playerDAO.getPlayer(username,password);

        if(player != null){
            result.setStatus("success");
//            result.setWid(player.getWid());
            result.setId(player.getId());
            System.out.println("[DEBUG] Login success");
            // login success, make sharedData hold the login-player's info
            userSession.setPlayer(player);
        }
        else{
            result.setStatus("fail");
            result.setError_msg("LogIn failed, wrong password/username");
            System.out.println("[DEBUG] Login failed, wrong password/username");
        }
        userSession.sendMsg(result);
        return result;
    }
}
