package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.building.BaseShop;
import edu.duke.ece.fantasy.building.Shop;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.BuildingRequestMessage;
import edu.duke.ece.fantasy.json.BuildingResultMessage;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingHandlerTest {
    BuildingHandler buildingHandler;
    Session session;
    PlayerDAO playerDAO;
    TerritoryDAO territoryDAO;

//    Session createSession() {
//        Session session =
//
//        return session;
//    }

    public BuildingHandlerTest() {
        session = HibernateUtil.getSessionFactory().openSession();
        buildingHandler = new BuildingHandler(session);
        playerDAO = new PlayerDAO(session);
        territoryDAO = new TerritoryDAO(session);
        (new Initializer(session)).initialize_test_player();
        territoryDAO.addTerritory(new WorldCoord(), 0, "grass", null);
    }


    @Test
    public void handle() {
        session.beginTransaction();
        handle_create_list();
        handle_create();
        handle_update();
    }


    public void handle_create_list() {
        BuildingRequestMessage requestMessage = new BuildingRequestMessage();
        requestMessage.setAction("createList");
        requestMessage.setCoord(new WorldCoord());
        Player player = playerDAO.getPlayer("test");
        BuildingResultMessage res = buildingHandler.handle(requestMessage, player.getId());
        try {
            System.out.println(ObjectMapperFactory.getObjectMapper().writeValueAsString(res));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handle_create() {
        BuildingRequestMessage requestMessage = new BuildingRequestMessage();
        requestMessage.setAction("create");
        String building_name = (new BaseShop()).getName();
        requestMessage.setBuildingName(building_name);
        requestMessage.setCoord(new WorldCoord());
        Player player = playerDAO.getPlayer("test");
        BuildingResultMessage res = buildingHandler.handle(requestMessage, player.getId());
        assertEquals(building_name, res.getBuilding().getName());
        // rebuild
        res = buildingHandler.handle(requestMessage, player.getId());
        assertNotEquals("success", res.getResult());
        try {
            System.out.println(ObjectMapperFactory.getObjectMapper().writeValueAsString(res));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handle_update() {

    }
}