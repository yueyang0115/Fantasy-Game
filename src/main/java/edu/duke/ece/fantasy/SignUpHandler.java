package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.json.SignUpRequestMessage;
import edu.duke.ece.fantasy.json.SignUpResultMessage;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUpHandler {
    private PlayerDAO playerDAO;
    Logger logger = LoggerFactory.getLogger(SignUpHandler.class);

    public SignUpHandler(Session session) {
        this.playerDAO = new PlayerDAO(session);
    }

    public SignUpResultMessage handle(SignUpRequestMessage input) {
        SignUpResultMessage result = new SignUpResultMessage();
        String username = input.getUsername();
        String password = input.getPassword();

        Player player = playerDAO.getPlayer(username);
        if (player == null) {
            playerDAO.addPlayer(username, password);
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
