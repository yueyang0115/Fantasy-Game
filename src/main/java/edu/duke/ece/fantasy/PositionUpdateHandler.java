package edu.duke.ece.fantasy;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.PositionRequestMessage;
import edu.duke.ece.fantasy.json.PositionResultMessage;
import org.hibernate.Session;

import edu.duke.ece.fantasy.worldgen.TileGenerator;

public class PositionUpdateHandler {
    TerritoryDAO territoryDAO;
    BuildingDAO buildingDAO;
    ShopDAO shopDAO;
    WorldDAO worldDAO;
    MonsterManger monsterDAO;
    //    ItemDAO itemDAO;

//    int x_block_num;
//    int y_block_num;

    public PositionUpdateHandler(Session session) {
        territoryDAO = new TerritoryDAO(session);
        buildingDAO = new BuildingDAO(session);
        shopDAO = new ShopDAO(session);
        worldDAO = new WorldDAO(session);
        monsterDAO = new MonsterManger(session);
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
        for (WorldCoord where : worldCoords) {
            where.setWid(wid);
            if (info == null) {
                info = worldDAO.initWorld(where, "fixmelater", 20);//TODO: real player names.  Fix hardcoding of tile size
            }
            Territory t = territoryDAO.getTerritory(where);
            if (t == null) {
                //later we might add other world types.  Like you can teleport to fire or ice etc worlds.
                //for now, wtype will always be "mainworld" but can change later.
                String wtype = info.getWorldType();
                TileGenerator gen = TileGenerator.forWorldType(wtype);
                gen.generate(territoryDAO, monsterDAO, buildingDAO, where, info);
                t = territoryDAO.getTerritory(where);
            }
//            if (Math.abs(dx) <= 3 && Math.abs(dy) <= 3) {
//                if (t.getStatus().equals("unexplored")) {
//                    territoryDAO.updateTerritory(where, "explored");
//                }
//            }
            territoryList.add(t);

//            if (t.getTerrainType().equals("forest_dense")) {
//                System.out.println("find forest_dense, should have a monster here");
//            }
            List<Monster> monsters = monsterDAO.getMonsters(where);
            if (monsters != null){
                for(Monster m: monsters) monsterList.add(m);
            }

            Building building = buildingDAO.getBuilding(where);

            if (building != null) {
                buildingList.add(building);
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
