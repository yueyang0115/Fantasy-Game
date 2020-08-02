package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.json.SignUpRequestMessage;
import edu.duke.ece.fantasy.json.SignUpResultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUpHandler {
//    private MetaDAO metaDAO;
    Logger logger = LoggerFactory.getLogger(SignUpHandler.class);

//    public SignUpHandler(MetaDAO metaDAO) {
//        this.metaDAO = metaDAO;
//    }

    public SignUpResultMessage handle(SignUpRequestMessage input) {
        SignUpResultMessage result = new SignUpResultMessage();
        String username = input.getUsername();
        String password = input.getPassword();

        Player player = MetaDAO.getPlayerDAO().getPlayer(username);
        if (player == null) {
            MetaDAO.getPlayerDAO().addPlayer(username, password);
            result.setStatus("success");
            System.out.println("[DEBUG] SignUp succeed");
            logger.debug("[DEBUG] SignUp succeed");
        } else {
            result.setStatus("fail");
            result.setError_msg("SignUp failed, username already exist");
            System.out.println("[DEBUG] SignUp failed, username already exist");
            logger.debug("[DEBUG] SignUp failed, username already exist");
        }

        return result;
    }
}
