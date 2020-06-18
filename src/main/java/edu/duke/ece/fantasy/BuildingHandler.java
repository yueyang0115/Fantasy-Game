package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.BuildingRequestMessage;
import edu.duke.ece.fantasy.json.BuildingResultMessage;
import org.hibernate.Session;

public class BuildingHandler {
    PlayerDAO playerDAO;
    BuildingDAO buildingDAO;
    ShopDAO shopDAO;
    TerritoryDAO territoryDAO;


    public BuildingHandler(Session session) {
        playerDAO = new PlayerDAO(session);
        buildingDAO = new BuildingDAO(session);
        shopDAO = new ShopDAO(session);
        territoryDAO = new TerritoryDAO(session);
    }

    public BuildingResultMessage handle(BuildingRequestMessage buildingRequestMessage, int playerId) {
        BuildingResultMessage buildingResultMessage = new BuildingResultMessage();
        String action = buildingRequestMessage.getAction();
        WorldCoord coord = buildingRequestMessage.getCoord();

        if (action.equals("createList")) {
            // check territory status
            Territory t = territoryDAO.getTerritory(coord);
            if (t.getTame() <= 10) {
                buildingResultMessage.addBuilding("Shop");
                buildingResultMessage.setResult("success");
            } else {
                buildingResultMessage.setResult("Selected territory's tame is too high");
            }
        } else if (action.equals("create")) {
            // create different building
            Shop shop = shopDAO.createShop(); // add to shopInventory table
            buildingResultMessage.setBuilding(buildingDAO.addBuilding(coord, shop)); // add to building table
            buildingResultMessage.setResult("success");
        }

        return buildingResultMessage;
    }

}
