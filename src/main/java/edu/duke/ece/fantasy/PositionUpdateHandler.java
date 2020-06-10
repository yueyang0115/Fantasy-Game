package edu.duke.ece.fantasy;

import java.util.ArrayList;
import java.util.List;

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

    public List<Territory> handle(int wid, int x, int y, int vision_x, int vision_y) {
        ArrayList<Territory> res = new ArrayList<Territory>();
        int x_block_num = 20; // how large tileSet should we generate
        int y_block_num = 20;
        int x_size = x_block_num * 10;
        int y_size = y_block_num * 10;
        int start_x = (x / x_size) * x_size + ((x > 0) ? 5 : -5); // used for converting the generated tileSet index to game map index
        int start_y = (y / y_size) * y_size + ((y > 0) ? 5 : -5);
        int dir_x = (x > 0) ? 10 : -10;
        int dir_y = (y > 0) ? 10 : -10;
        WorldCoord where = new WorldCoord(wid, x, y);
        RandomGenerator randomGenerator = new RandomGenerator();
        WorldInfo info = worldDAO.getInfo(wid);
        if (info == null) {
          info = worldDAO.initWorld(where, "fixmelater", 20);//TODO: real player names.  Fix hardcoding of tile size
        }
        for (int dx = -6; dx <= 6; dx++) {
          for (int dy = -8; dy <= 8; dy++) {
            //System.out.println("Doing dx=" + dx + ", dy = " + dy);
            where = new WorldCoord(wid, x + dx, y + dy);
            Territory t = territoryDAO.getTerritory(where);
            if (t == null) {
              //later we might add other world types.  Like you can teleport to fire or ice etc worlds.
              //for now, wtype will always be "mainworld" but can change later.
              String wtype = info.getWorldType();
              TileGenerator gen = TileGenerator.forWorldType(wtype);
              gen.generate(territoryDAO, where, info);
              t = territoryDAO.getTerritory(where);
            }
            if (Math.abs(dx) <= 3 && Math.abs(dy) <= 3) {
              if (t.getStatus().equals("unexplored")) {
                territoryDAO.updateTerritory(where, "explored");
              }
            }
            res.add(t);
          }
        }

        return res;
    }

}
