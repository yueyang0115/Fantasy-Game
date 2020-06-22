package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.building.*;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.BuildingRequestMessage;
import edu.duke.ece.fantasy.json.BuildingResultMessage;
import org.hibernate.Session;

import java.util.ArrayList;
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
        Shop shop = new BaseShop();
        Mine mine = new Mine();
        BaseBuildingMap.put(shop.getName(), shop);
        BaseBuildingMap.put(mine.getName(), mine);
    }

    public BuildingResultMessage handle(BuildingRequestMessage buildingRequestMessage, int playerId) {
        BuildingResultMessage buildingResultMessage = new BuildingResultMessage();
        buildingResultMessage.setAction(buildingRequestMessage.getAction());
        String action = buildingRequestMessage.getAction();

        WorldCoord coord = buildingRequestMessage.getCoord();

        try {
            if (action.equals("createList")) {
                // check territory status
                Territory t = territoryDAO.getTerritory(coord);
                if (t == null) {
                    throw new InvalidBuildingRequest("Selected territory doesn't exist");
                }
                if (t.getTame() > 10) {
                    throw new InvalidBuildingRequest("Selected territory's tame is too high");
                }
                buildingResultMessage.setBuildingList(new ArrayList<>(BaseBuildingMap.values()));
                buildingResultMessage.setResult("success");
            } else if (action.equals("create")) {
                // create different building
                Building building = Create(buildingRequestMessage, playerId);
                buildingResultMessage.setBuilding(building);
                buildingResultMessage.setResult("success");
            } else if (action.equals("updateList")) {
                // check if Building exist
                DBBuilding dbBuilding = DBBuildingDAO.getBuilding(coord);
                if (dbBuilding == null) {
                    throw new InvalidBuildingRequest("Selected building doesn't exist");
                }
                buildingResultMessage.setBuildingList(dbBuilding.toGameBuilding().getUpgradeList());
                buildingResultMessage.setResult("success");
            } else if (action.equals("update")) {
                Building building = Update(buildingRequestMessage, playerId);
                buildingResultMessage.setBuilding(building);
                buildingResultMessage.setResult("success");
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

        building.onCreate(session, coord);
        return building;
    }

    private Building Update(BuildingRequestMessage buildingRequestMessage, int playerId) throws InvalidBuildingRequest {
        WorldCoord coord = buildingRequestMessage.getCoord();
        String name = buildingRequestMessage.getBuildingName();

        // check if territory have building
        DBBuilding dbBuilding = DBBuildingDAO.getBuilding(coord);
        if (dbBuilding == null) {
            throw new InvalidBuildingRequest("Selected building doesn't exist");
        }
        Building UpgradeFrom = dbBuilding.toGameBuilding();

        // check if update building is in the list
        Building UpgradeTo = UpgradeFrom.getUpgradeTo().get("name");
        if (UpgradeTo == null) {
            throw new InvalidBuildingRequest("Building type doesn't exist");
        }

        Player player = playerDAO.getPlayer(playerId);

        // deduce money
        int left_money = player.getMoney() - UpgradeTo.getCost();
        if (left_money < 0) {
            throw new InvalidBuildingRequest("Don't have enough resource");
        }
        player.setMoney(left_money);

        UpgradeTo.onCreate(session, coord);
        return UpgradeTo;
    }


}
