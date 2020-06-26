package edu.duke.ece.fantasy.building;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.TerritoryDAO;
import org.hibernate.Session;

import java.util.List;

public class Castle extends Building {


    public Castle() {
        super("castle", 1000);
    }

    @Override
    public void onCreate(Session session, WorldCoord coord) {
        super.onCreate(session, coord);
        TerritoryDAO territoryDAO = new TerritoryDAO(session);
        List<Territory> territories = territoryDAO.getTerritories(coord, 5, 5);
        for(Territory territory:territories){
            territory.setTame(0);
        }
    }
}