package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Player;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class UserHandler {
    private Session session;

    public UserHandler(Session session) {
        this.session = session;
    }

    public void addUser(String username, String password) {
        Player player = new Player(username, password);
        session.save(player);
    }

    public Player getUser(String username, String password) {
        // select territory according to conditions
        Query q = session.createQuery("From Player U where U.username =:username and U.password =:password");
        q.setParameter("username", username);
        q.setParameter("password", password);
        Player res = (Player) q.uniqueResult();
        return res;
    }
}
