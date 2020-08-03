package edu.duke.ece.fantasy.Building;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.TerritoryDAO;

import java.util.List;

public class Castle extends Building {


    public Castle() {
        super("castle", 1000);
    }

    @Override
    public void onCreate(WorldCoord coord) {
        super.onCreate(coord);
        TerritoryDAO territoryDAO = MetaDAO.getTerritoryDAO();
        List<Territory> territories = territoryDAO.getTerritories(coord, 5, 5);
        for (Territory territory : territories) {
            territory.setTame(0);
            HibernateUtil.update(territory);
        }
    }
}