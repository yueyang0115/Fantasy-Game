package edu.duke.ece.fantasy.worldgen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.duke.ece.fantasy.database.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.duke.ece.fantasy.RandomGenerator;

public class TileGenerator {
    private static HashMap<String, TileGenerator> perWorldType = new HashMap<String, TileGenerator>();

    final int tileWidth;
    final int tileHeight;
    final HashMap<String, Square> squareMapping;
    String startTileName;
    String panicTile;
    HashMap<String, TileInfo> tilesByName;
    ArrayList<HashMap<String, ArrayList<TileInfo>>> tilesByEdgeTag;
    public static TileGenerator forWorldType (String wt) {
        synchronized(perWorldType) {
            TileGenerator tg = perWorldType.get(wt);
            if (tg == null) {
                try{
                    tg = new TileGenerator(wt);
                    perWorldType.put(wt,tg);
                }
                catch(Exception e) {
                    //ughhh any of these would be REALY BAD and mean we have invalid data.  we should catch these in testing.
                    //TODO: WTF do we do here?
                    e.printStackTrace();
                }

            }
            return tg;
        }
    }
    private TileGenerator(String worldType) throws IOException, JSONException, InvalidMapDataException{
        startTileName = null;
        String resName = "/world/" + worldType + ".json";
        InputStream res = getClass().getResourceAsStream(resName);
        if (res == null) {
            throw new IllegalArgumentException("Cannot get resource: "  + resName);
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(res))) {
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
                sb.append("\n"); //makes errors from JSON library meaningful
            }
        }
        squareMapping = new HashMap<String, Square>();
        tilesByName = new HashMap<String, TileInfo>();
        tilesByEdgeTag = new ArrayList<HashMap<String, ArrayList<TileInfo>>>();
        //one for each of NORTH, SOUTH, EAST, and WEST.
        for (int i = 0; i < 4; i++) {
            tilesByEdgeTag.add(new HashMap<String, ArrayList<TileInfo>>());
        }
        JSONObject js = new JSONObject(sb.toString());
        tileWidth = js.getInt("width");
        tileHeight = js.getInt("height");
        panicTile = "Id:" + js.getInt("panictile");
        readTerrain(js.getJSONArray("terrain"));
        readTiles(js.getJSONArray("tiles"));
    }
    private void readTerrain(JSONArray squareList) throws JSONException{
        for (int i = 0; i < squareList.length(); i++) {
            JSONObject jsSq = squareList.getJSONObject(i);
            Square s = new Square(jsSq);
            squareMapping.put(s.getId(), s);
        }
    }
    private void readTiles(JSONArray tileList) throws InvalidMapDataException, JSONException{
        for (int i = 0; i < tileList.length(); i++) {
            JSONObject jsTile = tileList.getJSONObject(i);
            TileInfo t = new TileInfo(jsTile, this);
            if (startTileName == null) {
                startTileName = t.getId();
            }
            addTile(t);
      /*for (int j = 0; j < 3; j++) {
        if (!t.canRotate()) {
          break;
        }
        t = t.rotate(this.getSquareMapping());
        addTile(t);
        }*/
        }
    }
    private void addTile(TileInfo t) {
        tilesByName.put(t.getId(), t);
        for (int i=0; i < 4; i++) {
            HashMap<String, ArrayList<TileInfo>> m = tilesByEdgeTag.get(i);
            ArrayList<TileInfo> al = m.get(t.getEdgeTag(i));
            if (al == null) {
                al = new ArrayList<TileInfo>();
                m.put(t.getEdgeTag(i), al);
            }
            //System.out.println("Putting " + t + " into " + t.getEdgeTag(i) + " for dir" + i);
            al.add(t);
        }
    }

    /**
     *  Decrease c until (startc-c) is a multiple of tilesize.
     * E.g., make it a multiple of tilesize units apart
     * example: c = 1, startc = 99, tilesize= 20.
     * difference between c and startc is 98.
     *  We need to return -1 (difference = 5*20)
     */
    static int alignToTileSize(int c, int startc, int tilesize) {
        //in our c=1, startc=99 example, offset would be -18
        int offset = (c - startc)%tilesize;
        //offset might be negative: get something positive, e.g., 2
        offset += tilesize;
        offset = offset % tilesize;
        //now offset is in [0,tilesize), so we can subtract it from c
        return c - offset;
    }
    private ArrayList<TileInfo> selectPossibleTiles(WorldCoord where, HashMap<WorldCoord, Tile> existingTiles){
        //these are organized N, S, E, W
        int deltaXs[] = {0, 0, tileWidth, -tileWidth};
        int deltaYs[] = {tileHeight, -tileHeight, 0, 0 };
        //System.out.println("selectPossible for " + where);
        HashSet<TileInfo> ans = null;
        //first, for each of N, S, E, W, find out our constraints.
        //put either null (no constraints) or a list of valid tiles into possible[i]
        for (int i =0 ; i < 4; i++) {
            ArrayList<TileInfo> curr = null;
            WorldCoord wc = new WorldCoord(where.getWid(), where.getX()+deltaXs[i], where.getY()+deltaYs[i]);
            Tile neighboor = existingTiles.get(wc);
            if (neighboor != null) {
                //some tile already next to this one in that direction
                TileInfo ti = tilesByName.get(neighboor.getName());
                //if ti is null then something has gone drastically wrong...  e.g., we've remove da tile type and don't know anything about it.
                //if that happens, we'll have no constraints on its neighboors (??!?)
                if (ti != null) {
                    //get the constraing on the edge facing (e.g., if neighboors is north, get constranit on south)
                    String constraint = ti.getEdgeTag(TileInfo.facingEdge(i));
                    //System.out.println("In direction " + i + " neighboor is " + neighboor.getName() + " with constraint " + constraint);
                    //look up the list of tiles that have "constraint" edge type on "i" direction
                    curr = tilesByEdgeTag.get(i).get(constraint);
                    //System.out.println("Possible tiles in that direction: " + curr);
                    if (curr == null) {
                        continue;
                    }
                    //now we need to intersect with whatever is in ans.  if we don't have
                    //ans yet, it is treated as universal set (so take curr).
                    if (ans == null) {
                        ans = new HashSet(curr);
                    }
                    else {
                        HashSet<TileInfo> temp = new HashSet<TileInfo>();
                        for (TileInfo t: curr){
                            if (ans.contains(t)) {
                                //System.out.println("  -> valid choice: " + t);
                                temp.add(t);
                            }
                        }
                        ans = temp;
                    }
                }
            }
            //otherwise do nothing.
        }
        if (ans == null) {
            //absolutely no constraints? I guess we return everything and pick randomly?
            return new ArrayList<TileInfo>(tilesByName.values());
        }
        return new ArrayList<TileInfo>(ans);
    }
    //I hate how tightly coupled this it to the database :(
    public void generate(TerritoryDAO terdao, MonsterManger monsterDAO, WorldCoord where, WorldInfo info) {
        TileDAO tdao = new TileDAO(terdao);
        //if the world doesn't have a start tile, lets put one in it.
        if (!tdao.doesWorldHaveStartTile(info)){
            //System.out.println("Putting start tile at " + info.getFirstTile());
            tdao.addTile(info.getFirstTile(), startTileName);
            putTerrain(terdao, monsterDAO, info.getFirstTile(), tilesByName.get(startTileName));
        }
        //align "where" to a tile start.
        //System.out
        //.println("Aligning " + where + " with " + info.getFirstTile() + " size " + tileWidth + "," + tileHeight);
        int alignX = alignToTileSize(where.getX(), info.getFirstTile().getX(), tileWidth);
        int alignY = alignToTileSize(where.getY(), info.getFirstTile().getY(), tileHeight);
        where = new WorldCoord(where.getWid(), alignX, alignY);
        //System.out.println("aligned is " + where);

        //we'll fill in a +/-1 by +/-1 tile region around "where", but we get a bit more since we can
        //be constrained by things near us
        HashMap<WorldCoord,Tile> existingTiles = tdao.getAllNearPoint(where, 3 * tileWidth);
        //TODO: do we have world edge wrap around??
        int startX = alignX - 1 * tileWidth;
        int endX = alignX + 1 * tileWidth;
        int startY = alignY - 1 * tileHeight;
        int endY = alignY + 1 * tileHeight;
        for (int y = startY; y <= endY; y+= tileHeight) {
            for (int x = startX; x <= endX; x+= tileWidth) {
                WorldCoord thisWc = new WorldCoord(where.getWid(), x, y);
                System.out.println("Considering tile at " + thisWc);
                if(tdao.getTileAt(thisWc) != null) {
                    //System.out.println("Already have " + tdao.getTileAt(thisWc));
                    continue;
                }
                //System.out.println("Select possible tiles (" + x + "," + y + ")" + System.currentTimeMillis());
                ArrayList<TileInfo> possible = selectPossibleTiles(thisWc, existingTiles);
                //System.out.println(possible + " tiles possible" + System.currentTimeMillis());
                TileInfo selected;
                if (possible.size() == 0) {
                    //ummm no tiles can fit here.... we have "panic tile" for that case.
                    System.out.println("Picking panic tile!");
                    selected = tilesByName.get(panicTile);
                }
                else {
                    selected = possible.get(RandomGenerator.random.nextInt(possible.size()));
                }
                Tile t =tdao.addTile(thisWc, selected.getId());
                System.out.println("tdao.addTile " + selected.getId());
                existingTiles.put(thisWc, t);
                putTerrain(terdao, monsterDAO, thisWc, selected);
            }
        }
    }

    private void putTerrain(TerritoryDAO terDAO, MonsterManger monsterDAO, WorldCoord where, TileInfo info) {
        //    System.out.println("Putting terrain on ["+where.getX()+","+(where.getX()+tileWidth)+") y:["+where.getY()+","+(where.getY()+tileHeight)+")"+ System.currentTimeMillis());

        for (int x = 0; x < tileWidth; x++) {
            for (int y = 0; y < tileHeight; y++) {
                Square s = info.getSquareAt(x,y);
                WorldCoord place = new WorldCoord(where.getWid(), where.getX()+x, where.getY()+y);
                //System.out.println("Territory at place: " + place);
                terDAO.addTerritory(place, "unexplored", s.getImageName(), new ArrayList<Monster>());

                //TODO: yy: add monstef, use s.getImage Name to add a random monster
                if(s.getImageName().equals("forestDense")){
                    System.out.println("addMonster in " + place.getX() + ","+place.getY());
                    Monster m = new Monster("BigWolf", 60, 6, 10);
                    monsterDAO.addMonster(m,place);
                }

//                if(s.getImageName().equals()){
//
//                }
            }
        }
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    HashMap<String, Square> getSquareMapping() {
        return squareMapping;
    }

}
