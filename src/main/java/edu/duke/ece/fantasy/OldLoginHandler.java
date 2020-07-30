package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.json.LoginRequestMessage;
import edu.duke.ece.fantasy.json.LoginResultMessage;

public class OldLoginHandler {
    private PlayerDAO playerDAO;
    private SharedData sharedData;

    public OldLoginHandler(MetaDAO metaDAO, SharedData sharedData){
        this.playerDAO = metaDAO.getPlayerDAO();
        this.sharedData = sharedData;
    }

    public LoginResultMessage handle(LoginRequestMessage input){
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
            sharedData.setPlayer(player);
        }
        else{
            result.setStatus("fail");
            result.setError_msg("LogIn failed, wrong password/username");
            System.out.println("[DEBUG] Login failed, wrong password/username");
        }
        return result;
    }


}


