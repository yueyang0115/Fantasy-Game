package edu.duke.ece.fantasy.Building;

import edu.duke.ece.fantasy.Building.Prototype.BaseShop;
import edu.duke.ece.fantasy.Building.Prototype.Building;
import edu.duke.ece.fantasy.Building.Prototype.Mine;
import edu.duke.ece.fantasy.Building.Prototype.Shop;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.DBBuildingDAO;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.DAO.TerritoryDAO;
import edu.duke.ece.fantasy.Building.Message.BuildingRequestMessage;
import edu.duke.ece.fantasy.Building.Message.BuildingResultMessage;
import edu.duke.ece.fantasy.net.UserSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuildingHandler {
    Map<String, Building> BaseBuildingMap = new HashMap<>();
    //("shop",shop);

    public BuildingHandler() {
        Shop shop = new BaseShop();
        Mine mine = new Mine();
        BaseBuildingMap.put(shop.getName(), shop);
        BaseBuildingMap.put(mine.getName(), mine);
    }

    public void handle(UserSession session, BuildingRequestMessage buildingRequestMessage) {
        BuildingResultMessage buildingResultMessage = new BuildingResultMessage();
        buildingResultMessage.setAction(buildingRequestMessage.getAction());
        String action = buildingRequestMessage.getAction();

        WorldCoord coord = buildingRequestMessage.getCoord();
        Player curPlayer = session.getPlayer();
        TerritoryDAO territoryDAO = session.getMetaDAO().getTerritoryDAO();
        DBBuildingDAO dbBuildingDAO = session.getMetaDAO().getDbBuildingDAO();
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
                Building building = Create(buildingRequestMessage, curPlayer, dbBuildingDAO);
                buildingResultMessage.setBuilding(building);
                buildingResultMessage.setResult("success");
            } else if (action.equals("upgradeList")) {
                // check if Building exist
                DBBuilding dbBuilding = dbBuildingDAO.getBuilding(coord);
                if (dbBuilding == null) {
                    throw new InvalidBuildingRequest("Selected building doesn't exist");
                }
                buildingResultMessage.setBuildingList(dbBuilding.toGameBuilding().getUpgradeList());
                buildingResultMessage.setResult("success");
            } else if (action.equals("upgrade")) {
                Building building = Update(buildingRequestMessage, curPlayer, dbBuildingDAO);
                buildingResultMessage.setBuilding(building);
                buildingResultMessage.setResult("success");
            }
        } catch (InvalidBuildingRequest e) {
            buildingResultMessage.setResult("Fail-" + e.getMessage());
        }

        HibernateUtil.update(curPlayer);
        session.sendMsg(buildingResultMessage);
    }

    private Building Create(BuildingRequestMessage buildingRequestMessage, Player player, DBBuildingDAO dbBuildingDAO) throws InvalidBuildingRequest {
        WorldCoord coord = buildingRequestMessage.getCoord();
        String name = buildingRequestMessage.getBuildingName();
        // check if territory have building
        if (dbBuildingDAO.getBuilding(coord) != null) {
            throw new InvalidBuildingRequest("Selected territory already have a building");
        }

        // check if building is BaseBuilding
        Building building = BaseBuildingMap.get(name);
        if (building == null) {
            throw new InvalidBuildingRequest("Building type doesn't exist");
        }


        // deduce money
        if (!player.checkMoney(building.getCost())) {
            throw new InvalidBuildingRequest("Don't have enough resource");
        }
        player.subtractMoney(building.getCost());

        building.onCreate(coord);
        return building;
    }

    private Building Update(BuildingRequestMessage buildingRequestMessage, Player player, DBBuildingDAO dbBuildingDAO) throws InvalidBuildingRequest {
        WorldCoord coord = buildingRequestMessage.getCoord();
        String name = buildingRequestMessage.getBuildingName();

        // check if territory have building
        DBBuilding dbBuilding = dbBuildingDAO.getBuilding(coord);
        if (dbBuilding == null) {
            throw new InvalidBuildingRequest("Selected building doesn't exist");
        }
        Building UpgradeFrom = dbBuilding.toGameBuilding();

        // check if update building is in the list
        Building UpgradeTo = UpgradeFrom.getUpgradeTo().get(name);
        if (UpgradeTo == null) {
            throw new InvalidBuildingRequest("Building type doesn't exist");
        }

        // deduce money
        if (!player.checkMoney(UpgradeTo.getCost())) {
            throw new InvalidBuildingRequest("Don't have enough resource");
        }
        player.subtractMoney(UpgradeTo.getCost());

        UpgradeTo.onCreate(coord);
        return UpgradeTo;
    }


}
