package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.building.BaseShop;
import edu.duke.ece.fantasy.building.Shop;
import edu.duke.ece.fantasy.building.SuperShop;
import edu.duke.ece.fantasy.database.*;
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
        PlayerDAO playerDAO = new PlayerDAO(session);
        Player player = playerDAO.getPlayer("test");
        if (player == null) { // if test player doesn't exist
            playerDAO.addPlayer("test", "test");
        }
//        session.getTransaction().commit();
    }

    public WorldCoord initialize_test_shop() {
        // create shop in WorldCoord
        WorldCoord shopCoord = new WorldCoord(-1, 100, 100);
//        session.beginTransaction();
        DBBuildingDAO dbBuildingDAO = new DBBuildingDAO(session);
        DBBuilding building = dbBuildingDAO.getBuilding(shopCoord);
        if (building == null) {
            Shop shop = new SuperShop();
            shop.onCreate(session, shopCoord);
        }
//        session.getTransaction().commit();
        return shopCoord;
    }

    public void test_initialize() {
        // initialize player
        initialize_test_player();
        // initialize shop
        initialize_test_shop();
    }


    public static void main(String[] args) {
//        (new Initializer()).initialize();
    }
}
