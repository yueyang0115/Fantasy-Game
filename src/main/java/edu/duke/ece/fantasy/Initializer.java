package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.building.Shop;
import edu.duke.ece.fantasy.database.*;
import org.hibernate.Session;

public class Initializer {

    private Session session;

    public Initializer() {

    }

    public void initialize() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // initialize database
            session.beginTransaction();
            // initialize terrain
            // initialize item
//            ConsumableDAO consumableDAO = new ConsumableDAO(session);
//            consumableDAO.initial();
//            EquipmentDAO equipmentDAO = new EquipmentDAO(session);
//            equipmentDAO.initial();
            // initialize shop
//            List<Item> items = itemDAO.getAllItem();
//            ShopDAO shopDAO = new ShopDAO(session);
//            shopDAO.initialShop(items);
            session.getTransaction().commit();
        }
    }

    public void initialize_test_player(Session session) {
        PlayerDAO playerDAO = new PlayerDAO(session);
        Player player = playerDAO.getPlayer("test");
        if (player == null) { // if test player doesn't exist
            playerDAO.addPlayer("test", "test");
        }
    }

    public void initialize_test_shop(Session session) {
        Shop shop = new Shop();
        shop.onCreate(session, new WorldCoord());
    }

    public void test_initialize(Session session) {

        // initialize database
        session.beginTransaction();

        // initialize player
        initialize_test_player(session);
        // initialize shop
        initialize_test_shop(session);

//            DBBuildingDAO DBBuildingDao = new DBBuildingDAO(session);
//            DBBuildingDao.addBuilding(new ,new Mine());
        session.getTransaction().commit();

    }


    public static void main(String[] args) {
        (new Initializer()).initialize();
    }
}
