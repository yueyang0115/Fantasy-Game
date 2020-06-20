package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.building.Building;
import edu.duke.ece.fantasy.building.InvalidBuildingRequest;
import edu.duke.ece.fantasy.building.Shop;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.BuildingRequestMessage;
import edu.duke.ece.fantasy.json.BuildingResultMessage;
import org.hibernate.Session;

import java.util.HashMap;
import java.util.Map;

public class BuildingHandler {
    PlayerDAO playerDAO;
    DBBuildingDAO DBBuildingDAO;
    TerritoryDAO territoryDAO;
    Session session;
    Map<String, Building> BaseBuildingMap = new HashMap<>();
    //("shop",shop);

    public BuildingHandler(Session session) {
        playerDAO = new PlayerDAO(session);
        DBBuildingDAO = new DBBuildingDAO(session);
        territoryDAO = new TerritoryDAO(session);
        this.session = session;
        BaseBuildingMap.put("Shop", new Shop());
    }

    public BuildingResultMessage handle(BuildingRequestMessage buildingRequestMessage, int playerId) {
        BuildingResultMessage buildingResultMessage = new BuildingResultMessage();
        String action = buildingRequestMessage.getAction();

        WorldCoord coord = buildingRequestMessage.getCoord();

        try {
            if (action.equals("createList")) {
                // check territory status
                Territory t = territoryDAO.getTerritory(coord);
                if (t == null) {
                    throw new InvalidBuildingRequest("Selected territory doesn't exist");
                }
                if (t.getTame() <= 10) {
                    buildingResultMessage.addBuilding(new Shop());
                    buildingResultMessage.setResult("success");
                } else {
                    throw new InvalidBuildingRequest("Selected territory's tame is too high");
//                    buildingResultMessage.setResult("Selected territory's tame is too high");
                }
            } else if (action.equals("create")) {
                // create different building
                Building building = Create(buildingRequestMessage, playerId);
                buildingResultMessage.setBuilding(building);
                buildingResultMessage.setResult("success");
            } else if (action.equals("update")) {

            }
        } catch (InvalidBuildingRequest e) {
            buildingResultMessage.setResult("Fail-" + e.getMessage());
        }

        return buildingResultMessage;
    }

    private Building Create(BuildingRequestMessage buildingRequestMessage, int playerId) throws InvalidBuildingRequest {
        WorldCoord coord = buildingRequestMessage.getCoord();
        String name = buildingRequestMessage.getBuildingName();
        // check if territory have building
        if (DBBuildingDAO.getBuilding(coord) != null) {
            throw new InvalidBuildingRequest("Selected territory already have a building");
        }

        // check if building is BaseBuilding
        Building building = BaseBuildingMap.get(name);
        if (building == null) {
            throw new InvalidBuildingRequest("Building type doesn't exist");
        }

        Player player = playerDAO.getPlayer(playerId);

        // deduce money
        int left_money = player.getMoney() - building.getCost();
        if (left_money < 0) {
            throw new InvalidBuildingRequest("Don't have enough resource");
        }
        player.setMoney(left_money);
        session.update(player);

        building.onCreate(session, coord);
        return building;
    }

}
