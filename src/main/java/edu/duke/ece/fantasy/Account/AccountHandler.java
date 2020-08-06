package edu.duke.ece.fantasy.Account;

import edu.duke.ece.fantasy.Account.Message.LoginRequestMessage;
import edu.duke.ece.fantasy.Account.Message.LoginResultMessage;
import edu.duke.ece.fantasy.Account.Message.SignUpRequestMessage;
import edu.duke.ece.fantasy.Account.Message.SignUpResultMessage;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.net.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AccountHandler {
    private final PlayerDAO playerDAO;
    Logger logger = LoggerFactory.getLogger(getClass());
    // track online player
    private ConcurrentMap<Integer, UserSession> player2sessions = new ConcurrentHashMap<>();

    public AccountHandler() {
        this.playerDAO = MetaDAO.getPlayerDAO();
    }

    public void handleLogin(UserSession userSession, LoginRequestMessage input) {
        LoginResultMessage result = new LoginResultMessage();
        String username = input.getUsername();
        String password = input.getPassword();

        Player player = playerDAO.getPlayer(username, password);

        if (player != null) {
            result.setStatus("success");
            result.setId(player.getId());
            System.out.println("[DEBUG] Login success");
            // login success, make sharedData hold the login-player's info
            userSession.setPlayer(player);
            player2sessions.put(player.getId(), userSession);
        } else {
            result.setStatus("fail");
            result.setError_msg("LogIn failed, wrong password/username");
            System.out.println("[DEBUG] Login failed, wrong password/username");
        }
        userSession.sendMsg(result);
    }

    public void handleSignup(UserSession session, SignUpRequestMessage input) {
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

        session.sendMsg(result);
    }
}
