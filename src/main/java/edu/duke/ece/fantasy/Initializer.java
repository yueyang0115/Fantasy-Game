package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.building.Shop;
import edu.duke.ece.fantasy.building.SuperShop;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.DBBuildingDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import org.hibernate.Session;

public class Initializer {

    private Session session;

    public Initializer(Session session) {
        this.session = session;
    }

    public void initialize() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.getTransaction().commit();
        }
    }

    public void initialize_test_player() {
//        session.beginTransaction();
//        PlayerDAO playerDAO = new PlayerDAO(session);
//        Player player = playerDAO.getPlayer("test");
//        if (player == null) { // if test player doesn't exist
//            playerDAO.addPlayer("test", "test");
//        }
//        session.getTransaction().commit();
    }


    public void test_initialize() {
        // initialize player
        initialize_test_player();
        // initialize shop
    }


    public static void main(String[] args) {
//        (new Initializer()).initialize();
    }
}
