package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class PositionUpdateHandler {
    TerritoryDAO territoryDAO;
    TerrainDAO terrainDAO;
    BuildingDAO buildingDAO;
    ShopDAO shopDAO;
    ItemDAO itemDAO;
//    int x_block_num;
//    int y_block_num;

    public PositionUpdateHandler(Session session) {
        territoryDAO = new TerritoryDAO(session);
        terrainDAO = new TerrainDAO(session);
        buildingDAO = new BuildingDAO(session);
        shopDAO = new ShopDAO(session);
        itemDAO = new ItemDAO(session);
    }

    public List<Territory> handle(int wid, int x, int y, int vision_x, int vision_y) {
        List<Territory> res = new ArrayList<>();
        int x_block_num = 20; // how large tileSet should we generate
        int y_block_num = 20;
        int x_size = x_block_num * 10;
        int y_size = y_block_num * 10;
        int start_x = (x / x_size) * x_size + ((x > 0) ? 5 : -5); // used for converting the generated tileSet index to game map index
        int start_y = (y / y_size) * y_size + ((y > 0) ? 5 : -5);
        int dir_x = (x > 0) ? 10 : -10;
        int dir_y = (y > 0) ? 10 : -10;
        RandomGenerator randomGenerator = new RandomGenerator();
        if (territoryDAO.getTerritory(wid, x, y) == null) {
            // check if need to generate new tile set
            TileGenerator tileGenerator = new TileGenerator(x_block_num, y_block_num);  // generate tile set
            TerritoryBlock[][] new_map = tileGenerator.GenerateTileSet();
            for (int i = 0; i < y_block_num; i++) {
                for (int j = 0; j < x_block_num; j++) {
                    int new_x = new_map[i][j].getX() * dir_x + start_x;
                    int new_y = new_map[i][j].getY() * dir_y + start_y;
                    // add terrain
                    Terrain terrain = terrainDAO.getTerrain(new_map[i][j].getType());
                    // add monster
                    List<Monster> monsters = new ArrayList<>();
                    if (terrain.getType().equals("mountain") && !(new_x == x && new_y == y)) {
                        monsters.add(new Monster("wolf", 10, 10, 10));
                    }
                    Territory territory = territoryDAO.addTerritory(wid, new_x, new_y, "unexplored", terrain, monsters);
                    // add building
                    if ((territory.getTerrain().getType().equals("grass") && randomGenerator.getRandomResult(30)) || (new_x == x && new_y == y)) {
                        // create shop
                        Shop shop = shopDAO.createShop();
                        territoryDAO.addBuildingToTerritory(territory, shop);
                    }
                }
            }
        }

        if (territoryDAO.getTerritory(wid, x, y).getStatus().equals("unexplored")) {
            territoryDAO.updateTerritory(wid, x, y, "explored");
        }
        // get x_block_num*y_block_num blocks
        res = territoryDAO.getTerritories(wid, x, y, vision_x, vision_y);
        return res;
    }

}
