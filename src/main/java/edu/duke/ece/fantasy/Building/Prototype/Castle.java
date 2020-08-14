package edu.duke.ece.fantasy.Building.Prototype;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.TerritoryDAO;

import java.util.List;

public class Castle extends Building {


    public Castle() {
        super("castle", 1000);
    }

    @Override
    public void onCreate(WorldCoord coord,MetaDAO metaDAO) {
        super.onCreate(coord, metaDAO);
        TerritoryDAO territoryDAO = metaDAO.getTerritoryDAO();
        List<Territory> territories = territoryDAO.getTerritories(coord, 5, 5);
        for (Territory territory : territories) {
            territory.setTame(0);
            metaDAO.getDbBuildingDAO().getSession().update(territory);
        }
    }
}