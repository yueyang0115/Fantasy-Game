package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import org.hibernate.Session;

import java.util.List;

public class Initializer {

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

    public void test_initialize() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // initialize database
            session.beginTransaction();
            // initialize terrain
            // initialize item
//            ConsumableDAO consumableDAO = new ConsumableDAO(session);
//            consumableDAO.initial();
//            EquipmentDAO equipmentDAO = new EquipmentDAO(session);
//            equipmentDAO.initial();

            // initialize player
            PlayerDAO playerDAO = new PlayerDAO(session);
            Player player = playerDAO.getPlayer("test");
            if (player == null) { // if test player doesn't exist
                playerDAO.addPlayer("test", "test");
            }
            // initialize shop
            ShopDAO shopDAO = new ShopDAO(session);
            Shop shop = shopDAO.getShop(1);
            if (shop == null) {
                shopDAO.createShop();
            }
            session.getTransaction().commit();
        }
    }


    public static void main(String[] args) {
        (new Initializer()).initialize();
    }
}
