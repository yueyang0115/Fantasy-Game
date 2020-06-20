package edu.duke.ece.fantasy;

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
        (new Initializer()).initialize_test_player(session);
        territoryDAO.addTerritory(new WorldCoord(), 0, "grass", null);
    }


    @Test
    public void handle(){
        session.beginTransaction();
        handle_create_list();
        handle_create();
    }


    public void handle_create_list() {
        BuildingRequestMessage requestMessage = new BuildingRequestMessage();
        requestMessage.setAction("createList");
        requestMessage.setCoord(new WorldCoord());
        Player player = playerDAO.getPlayer("test");
        BuildingResultMessage res = buildingHandler.handle(requestMessage, player.getId());
        try{
            System.out.println(ObjectMapperFactory.getObjectMapper().writeValueAsString(res));
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void handle_create(){
        BuildingRequestMessage requestMessage = new BuildingRequestMessage();
        requestMessage.setAction("create");
        requestMessage.setBuildingName((new Shop()).getName());
        requestMessage.setCoord(new WorldCoord());
        Player player = playerDAO.getPlayer("test");
        BuildingResultMessage res = buildingHandler.handle(requestMessage, player.getId());
        try{
            System.out.println(ObjectMapperFactory.getObjectMapper().writeValueAsString(res));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}