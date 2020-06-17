package edu.duke.ece.fantasy.worldgen;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TileInfo {
  final String id;
  final Square[][] squares;
  final boolean isRotateAllowed; // can this tile be rotated? I.e. do all squares in it support rotation.
  // each edge (N, S, E, W) as a "tag" e.g. "g" or "R12". Tiles can go next to
  // each other iff facing tags match
  // i.e. if a tile has north tag X the the tile above it needs south tag X
  final String[] edgeTags;
  public static final int NORTH = 0;
  public static final int SOUTH = 1;
  public static final int EAST = 2;
  public static final int WEST = 3;

  // this method converts NORTH <-> SOUTH and EAST <-> WEST
  // we can do this by flipping the low bit.
  public static int facingEdge(int edge) {
    int lowBit = edge & 1; // for N or E this is 0, for S or W this is 1
    int flippedLowBit = ~lowBit; // so turn it into 1 for N/E or 0 for S/W
    int rest = edge & ~1; // for N or S this is 0, for S or W this is 2
    return rest | (flippedLowBit&1); // Or them back together to get the right answer
  }

  public TileInfo(JSONObject js, TileGenerator myGenerator) throws InvalidMapDataException, JSONException {
    this.id = "Id:" + js.getInt("id");
    this.squares = new Square[myGenerator.getTileHeight()][myGenerator.getTileWidth()];
    this.isRotateAllowed = readSquares(js.getJSONArray("rows"), myGenerator.getSquareMapping());
    this.edgeTags = new String[4];
    this.edgeTags[NORTH] = js.getString("north");
    this.edgeTags[SOUTH] = js.getString("south");
    this.edgeTags[EAST] = js.getString("east");
    this.edgeTags[WEST] = js.getString("west");
  }

  TileInfo(String id, Square[][] squares, boolean isRotateAllowed, String[] edgeTags) {
    this.id = id;
    this.squares = squares;
    this.isRotateAllowed = isRotateAllowed;
    this.edgeTags = edgeTags;
  }

  private boolean readSquares(JSONArray rows, HashMap<String, Square> mapping)
      throws InvalidMapDataException, JSONException {
    boolean answer = squares.length == squares[0].length; // whether or not all squares can be rotated + is square
                                                          // (w==h)
    for (int row = 0; row < squares.length; row++) {
      String s = rows.getString(squares.length-row-1); //we need to flip this upside down since y goes up in world coords
      for (int col = 0; col < squares[row].length; col++) {
        char c = s.charAt(col); // one letter = one square
        String sqname = "" + c; // but we do the mapping a string
        Square sq = mapping.get(sqname);
        if (sq == null) {
          throw new InvalidMapDataException(
              sqname + " is not a valid square name (row = " + row + " , col = " + col + ")");
        }
        squares[row][col] = sq;
        // if this square does not have a rotation scheme
        if (sq.getRotId() == null) {
          // then we can't rotate this tile
          answer = false;
        }
      }
    }
    return answer;
  }

  public String getId() {
    return id;
  }

  public boolean canRotate() {
    return isRotateAllowed;
  }
  public Square getSquareAt(int x, int y) {
    return squares[y][x];
  }
  // rotate tile clockwise
  // so
  // a b c g d a
  // d e f -> h e b
  // g h i i f c
  TileInfo rotate(HashMap<String, Square> mapping) {
    String newId = id + ":rot";
    Square[][] newSquares = new Square[squares.length][squares.length];
    boolean ansCanRot = true;
    for (int i = 0; i < newSquares.length; i++) {
      for (int j = 0; j < newSquares.length; j++) {
        Square oldSq = squares[i][j]; // find right square
        // find rotation of that square
        Square newSq = mapping.get(oldSq.getRotId());
        if (newSq.getRotId() == null) {
          ansCanRot = false; // see if it can be rotated further
        }
        // put it in the answer
        newSquares[j][newSquares.length - 1 - i] = newSq;
      }
    }
    String[] newEdgeTags = new String[4];
    newEdgeTags[NORTH] = rotationNameForEdge(edgeTags[WEST]);
    newEdgeTags[EAST] = rotationNameForEdge(edgeTags[NORTH]);
    newEdgeTags[SOUTH] = rotationNameForEdge(edgeTags[EAST]);
    newEdgeTags[WEST] = rotationNameForEdge(edgeTags[SOUTH]);
    return new TileInfo(newId, newSquares, ansCanRot, newEdgeTags);
  }
  private String rotationNameForEdge(String orig) {
    if (orig.length() <= 1){
      return orig;
    }
    return orig + ":r";
  }
  public String getEdgeTag(int which) {
    return edgeTags[which];
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TileInfo other = (TileInfo) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
  @Override
  public String toString() {
    return "TileInfo" + id;
  }
}
