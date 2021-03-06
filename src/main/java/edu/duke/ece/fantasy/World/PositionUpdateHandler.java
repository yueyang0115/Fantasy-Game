package edu.duke.ece.fantasy.World;

import java.util.ArrayList;
import java.util.List;

import edu.duke.ece.fantasy.Building.Prototype.Building;
import edu.duke.ece.fantasy.Building.Prototype.Castle;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.*;
import edu.duke.ece.fantasy.World.Message.PositionRequestMessage;
import edu.duke.ece.fantasy.World.Message.PositionResultMessage;

import edu.duke.ece.fantasy.World.worldgen.TileGenerator;
import edu.duke.ece.fantasy.net.UserSession;

public class PositionUpdateHandler {
    TerritoryDAO territoryDAO;
    DBBuildingDAO DBBuildingDAO;
    WorldDAO worldDAO;
    MonsterDAO monsterDAO;
    PlayerDAO playerDAO;
    ArrayList<Territory> territoryList = new ArrayList<>();
    ArrayList<Monster> monsterList = new ArrayList<>();
    ArrayList<Building> buildingList = new ArrayList<>();

    //    ItemDAO itemDAO;

//    int x_block_num;
//    int y_block_num;

    public PositionUpdateHandler() {
        //    itemDAO = new ItemDAO(session);
    }

    private void GenerateTerritory(List<WorldCoord> worldCoords, WorldInfo info, MetaDAO metaDAO) {
        for (WorldCoord where : worldCoords) {
            where.setWid(info.getWid());
            Territory t = territoryDAO.getTerritory(where);
            if (t == null) {
                //later we might add other world types.  Like you can teleport to fire or ice etc worlds.
                //for now, wtype will always be "mainworld" but can change later.
                String wtype = info.getWorldType();
                TileGenerator gen = TileGenerator.forWorldType(wtype);
                gen.generate(metaDAO, where, info);
            }
        }
    }

    private void GetCoordInfo(List<WorldCoord> worldCoords) {
        for (WorldCoord where : worldCoords) {
            Territory t = territoryDAO.getTerritory(where);
            territoryList.add(t);
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
    }

    public void handle(UserSession session, PositionRequestMessage positionMsg) {
        territoryDAO = session.getMetaDAO().getTerritoryDAO();
        DBBuildingDAO = session.getMetaDAO().getDbBuildingDAO();
        worldDAO = session.getMetaDAO().getWorldDAO();
        monsterDAO = session.getMetaDAO().getMonsterDAO();
        playerDAO = session.getMetaDAO().getPlayerDAO();

        //cachedMap = new HashMap<>();
        session.beginTransaction();
        PositionResultMessage positionResultMessage = new PositionResultMessage();
        Player player = session.getPlayer();
        //WorldInfo info = worldDAO.getInfo(wid);
//        WorldInfo info = player.getWorlds().get(WorldInfo.MainWorld);
        System.out.println();
        WorldInfo info = worldDAO.getInfo(player.getCurWorldId());

        List<WorldCoord> worldCoords = positionMsg.getCoords();
        WorldCoord currentCoord = positionMsg.getCurrentCoord();
        player.setCurrentCoord(currentCoord);
        boolean isNewWorld = false;
        if (info == null) { // generate info
            info = worldDAO.initWorld(currentCoord, player.getUsername(), 20);//TODO: Fix hardcoding of tile size
            isNewWorld = true;
        }
        player.setCurWorldId(info.getWid());
        player.getCurrentCoord().setWid(info.getWid());

        GenerateTerritory(worldCoords, info, session.getMetaDAO());

        if (isNewWorld && player.getStatus().equals(WorldInfo.MainWorld)) {
            /* add castle between GenerateTerritory and GetCoordInfo since it need territory exist and will change
              the territory's tame*/
            (new Castle()).onCreate(info.getStartCoords(),session.getMetaDAO());
        }

        GetCoordInfo(worldCoords);

        positionResultMessage.setTerritoryArray(territoryList);
        positionResultMessage.setBuildingArray(buildingList);
        positionResultMessage.setMonsterArray(monsterList);
        session.commitTransaction();
        session.sendMsg(positionResultMessage);
    }

}
