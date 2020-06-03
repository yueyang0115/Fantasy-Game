package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.ItemDAO;
import edu.duke.ece.fantasy.database.ShopDAO;
import edu.duke.ece.fantasy.database.TerrainDAO;
import org.hibernate.Session;

public class Initializer {

    public void initialize(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            // initialize database
            session.beginTransaction();
            // initialize terrain
            TerrainDAO terrainDAO = new TerrainDAO(session);
            terrainDAO.initialTerrain();
            // initialize item
            ItemDAO itemDAO = new ItemDAO(session);
            itemDAO.initial();
            // initialize shop
            ShopDAO shopDao = new ShopDAO(session);

            session.getTransaction().commit();
        }
    }

    public static void main(String[] args) {
        (new Initializer()).initialize();
    }
}
