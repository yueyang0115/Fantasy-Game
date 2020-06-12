package edu.duke.ece.fantasy.database;

import org.hibernate.Session;

public class playerInventoryDAO {
    Session session;

    public playerInventoryDAO(Session session) {
        this.session = session;
    }

}
