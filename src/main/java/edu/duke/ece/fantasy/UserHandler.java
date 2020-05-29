package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Player;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jasypt.util.password.BasicPasswordEncryptor;

public class UserHandler {
    private Session session;
    BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();

    public UserHandler(Session session) {
        this.session = session;
    }

    public void addUser(String username, String password) {
        String encryptPassword = passwordEncryptor.encryptPassword(password);
        Player player = new Player(username, encryptPassword);
        session.save(player);
    }

    public Player getUser(String username) {
        Query q = session.createQuery("From Player U where U.username =:username");
        q.setParameter("username", username);
        return (Player) q.uniqueResult();
    }

    public Player getUser(String username, String password) {
        // select territory according to conditions
        Query q = session.createQuery("From Player U where U.username =:username");
        q.setParameter("username", username);
        Player res = (Player) q.uniqueResult();
        if (res == null) {
            return null;
        }
        if (passwordEncryptor.checkPassword(password, res.getPassword())) {
            return res;
        } else {
            return null;
        }
    }
}
