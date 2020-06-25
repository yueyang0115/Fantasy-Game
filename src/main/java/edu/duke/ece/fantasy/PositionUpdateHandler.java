package edu.duke.ece.fantasy;

import java.util.ArrayList;
import java.util.List;

import edu.duke.ece.fantasy.building.Building;
import edu.duke.ece.fantasy.building.Castle;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.PositionRequestMessage;
import edu.duke.ece.fantasy.json.PositionResultMessage;
import org.hibernate.Session;

import edu.duke.ece.fantasy.worldgen.TileGenerator;

public class PositionUpdateHandler {
    TerritoryDAO territoryDAO;
    DBBuildingDAO DBBuildingDAO;
    WorldDAO worldDAO;
    MonsterManger monsterDAO;
    PlayerDAO playerDAO;
    Session session;
    //    ItemDAO itemDAO;

//    int x_block_num;
//    int y_block_num;

    public PositionUpdateHandler(Session session) {
        territoryDAO = new TerritoryDAO(session);
        DBBuildingDAO = new DBBuildingDAO(session);
        worldDAO = new WorldDAO(session);
        monsterDAO = new MonsterManger(session);
        playerDAO = new PlayerDAO(session);
        this.session = session;
        //    itemDAO = new ItemDAO(session);
    }

    public PositionResultMessage handle(int wid, PositionRequestMessage positionMsg) {
        //cachedMap = new HashMap<>();
        PositionResultMessage positionResultMessage = new PositionResultMessage();
        ArrayList<Territory> territoryList = new ArrayList<Territory>();
        ArrayList<Monster> monsterList = new ArrayList<Monster>();
        ArrayList<Building> buildingList = new ArrayList<>();
        WorldInfo info = worldDAO.getInfo(wid);

        List<WorldCoord> worldCoords = positionMsg.getCoords();
        WorldCoord currentCoord = positionMsg.getCurrentCoord();
        currentCoord.setWid(wid);
        if (info == null) { // generate info
            info = worldDAO.initWorld(currentCoord, playerDAO.getPlayerByWid(wid).getUsername(), 20);//TODO: Fix hardcoding of tile size
        }

        for (WorldCoord where : worldCoords) {
            where.setWid(wid);
            Territory t = territoryDAO.getTerritory(where);
            if (t == null) {
                //later we might add other world types.  Like you can teleport to fire or ice etc worlds.
                //for now, wtype will always be "mainworld" but can change later.
                String wtype = info.getWorldType();
                TileGenerator gen = TileGenerator.forWorldType(wtype);
                gen.generate(territoryDAO, monsterDAO, DBBuildingDAO, where, info);

                if (currentCoord.equals(info.getStartCoords())) { // add castle to start point
                    (new Castle()).onCreate(session, currentCoord);
                }
                t = territoryDAO.getTerritory(where);
            }

            territoryList.add(t);
//            if (t.getTerrainType().equals("forest_dense")) {
//                System.out.println("find forest_dense, should have a monster here");
//            }
            List<Monster> monsters = monsterDAO.getMonsters(where);
            if (monsters != null) {
                for (Monster m : monsters) {
                    monsterDAO.setMonsterStatus(m.getId(), false);
                    monsterList.add(m);
                }
            }

            DBBuilding building = DBBuildingDAO.getBuilding(where);

            if (building != null) {
                buildingList.add(building.toGameBuilding());
            }
        }





        positionResultMessage.setTerritoryArray(territoryList);
        positionResultMessage.setBuildingArray(buildingList);
        positionResultMessage.setMonsterArray(monsterList);
        return positionResultMessage;
//        WorldCoord where = new WorldCoord(wid, x, y);


//        for (int dx = -6; dx <= 6; dx++) {
//          for (int dy = -8; dy <= 8; dy++) {
//            //System.out.println("Doing dx=" + dx + ", dy = " + dy);
//            where = new WorldCoord(wid, x + dx, y + dy);
//            Territory t = territoryDAO.getTerritory(where);
//            if (t == null) {
//              //later we might add other world types.  Like you can teleport to fire or ice etc worlds.
//              //for now, wtype will always be "mainworld" but can change later.
//              String wtype = info.getWorldType();
//              TileGenerator gen = TileGenerator.forWorldType(wtype);
//              gen.generate(territoryDAO, where, info);
//              t = territoryDAO.getTerritory(where);
//            }
//            if (Math.abs(dx) <= 3 && Math.abs(dy) <= 3) {
//              if (t.getStatus().equals("unexplored")) {
//                territoryDAO.updateTerritory(where, "explored");
//              }
//            }
//            res.add(t);
//          }
//        }
    }

}
