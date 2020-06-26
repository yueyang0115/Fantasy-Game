package edu.duke.ece.fantasy.database;

import org.hibernate.Session;

public class MetaDAO {
    Session session;

    public MetaDAO(Session session){
        this.session = session;
    }

    public PlayerDAO generatePlayerDAO(){
        return new PlayerDAO(session);
    }

}
