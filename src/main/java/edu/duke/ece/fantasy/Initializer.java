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
            ItemDAO itemDAO = new ItemDAO(session);
            itemDAO.initial();
            // initialize shop
            List<Item> items = itemDAO.getAllItem();
            ShopDAO shopDAO = new ShopDAO(session);
            shopDAO.initialShop(items);
            session.getTransaction().commit();
        }
    }

    public static void main(String[] args) {
        (new Initializer()).initialize();
    }
}
