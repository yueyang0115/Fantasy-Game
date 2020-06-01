package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.TerrainDAO;
import edu.duke.ece.fantasy.database.Territory;
import edu.duke.ece.fantasy.database.TerritoryDAO;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class PositionUpdateHandler {
    TerritoryDAO territoryDAO;
    TerrainDAO terrainDAO;
    int tile_set_width = 300;
    int tile_set_height = 300;
//    int x_block_num;
//    int y_block_num;

    public PositionUpdateHandler(Session session) {
        territoryDAO = new TerritoryDAO(session);
        terrainDAO = new TerrainDAO(session);
    }

    public List<Territory> handle(int wid, int x, int y, int vision_x, int vision_y) {
        List<Territory> res = new ArrayList<>();
        int x_block_num = 30;
        int y_block_num = 30;
//        // check if need to generate new tile set
//        if (territoryDAO.getTerritory(wid, x, y) == null) {
//            TileGenerator tileGenerator = new TileGenerator(x_block_num, y_block_num);
//            TerritoryBlock[][] new_map = tileGenerator.GenerateTileSet();
//            for (int i=0;i<y_block_num;i++) {
//                for(int j=0;j<x_block_num;j++){
//                    territoryDAO.addTerritory(wid,new_map[i][j].getX(),new_map[i][j].getY(),"unexplored");
//                }
//            }
//
//        }

        territoryDAO.addTerritories(wid, x, y, x_block_num, y_block_num);
        if (territoryDAO.getTerritory(wid, x, y).getStatus().equals("unexplored")) {
            territoryDAO.updateTerritory(wid, x, y, "explored");
        }
        // generate tile set
        res = territoryDAO.getTerritories(wid, x, y, vision_x, vision_y);
        // get x_block_num*y_block_num blocks
        return res;
    }


    private void GenerateTileSet() {

    }
}
