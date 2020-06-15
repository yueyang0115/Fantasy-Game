package edu.duke.ece.fantasy;

import java.util.ArrayList;
import java.util.List;

import edu.duke.ece.fantasy.json.PositionRequestMessage;
import org.hibernate.Session;

import edu.duke.ece.fantasy.database.BuildingDAO;
import edu.duke.ece.fantasy.database.ShopDAO;
import edu.duke.ece.fantasy.database.Territory;
import edu.duke.ece.fantasy.database.TerritoryDAO;
import edu.duke.ece.fantasy.database.WorldCoord;
import edu.duke.ece.fantasy.database.WorldDAO;
import edu.duke.ece.fantasy.database.WorldInfo;
import edu.duke.ece.fantasy.worldgen.TileGenerator;

public class PositionUpdateHandler {
    TerritoryDAO territoryDAO;
    BuildingDAO buildingDAO;
    ShopDAO shopDAO;
    WorldDAO worldDAO;
    //    ItemDAO itemDAO;
//    int x_block_num;
//    int y_block_num;

    public PositionUpdateHandler(Session session) {
        territoryDAO = new TerritoryDAO(session);
        buildingDAO = new BuildingDAO(session);
        shopDAO = new ShopDAO(session);
        worldDAO = new WorldDAO(session);
        //    itemDAO = new ItemDAO(session);
    }

    public List<Territory> handle(int wid, PositionRequestMessage positionMsg) {
        ArrayList<Territory> res = new ArrayList<Territory>();
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
                gen.generate(territoryDAO, where, info);
                t = territoryDAO.getTerritory(where);
            }
//            if (Math.abs(dx) <= 3 && Math.abs(dy) <= 3) {
//                if (t.getStatus().equals("unexplored")) {
//                    territoryDAO.updateTerritory(where, "explored");
//                }
//            }
            res.add(t);
        }
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

        return res;
    }

}
