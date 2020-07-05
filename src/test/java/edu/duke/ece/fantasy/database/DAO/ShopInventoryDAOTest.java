package edu.duke.ece.fantasy.database.DAO;

import edu.duke.ece.fantasy.building.BaseShop;
import edu.duke.ece.fantasy.building.Shop;
import edu.duke.ece.fantasy.database.HibernateUtil;
import edu.duke.ece.fantasy.database.Inventory;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.database.shopInventory;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.util.List;

class ShopInventoryDAOTest {
    private static Session session;
    private MetaDAO metaDAO;

    @BeforeAll
    static void setUp() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    @BeforeEach
    void SetUpEach() {
        session.beginTransaction();
        metaDAO = new MetaDAO(session);
    }

    @AfterEach
    void shutDownEach() {
        session.getTransaction().rollback();
    }

    @AfterAll
    static void shutDownAll() {
        session.close();
    }

    @Test
    void getInventories() {
        ShopInventoryDAO shopInventoryDAO = new ShopInventoryDAO(session);
        Shop baseShop = new BaseShop();
        baseShop.onCreate(metaDAO, new WorldCoord());
        List<Inventory> res = shopInventoryDAO.getInventories(new WorldCoord());

    }
}