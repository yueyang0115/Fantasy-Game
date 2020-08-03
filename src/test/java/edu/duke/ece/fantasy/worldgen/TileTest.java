package edu.duke.ece.fantasy.worldgen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import edu.duke.ece.fantasy.World.worldgen.Square;
import edu.duke.ece.fantasy.World.worldgen.TileInfo;
import org.junit.jupiter.api.Test;

public class TileTest {
  @Test
  public void test_facingEdge() {
    assertEquals(TileInfo.NORTH, TileInfo.facingEdge(TileInfo.SOUTH));
    assertEquals(TileInfo.SOUTH, TileInfo.facingEdge(TileInfo.NORTH));
    assertEquals(TileInfo.EAST, TileInfo.facingEdge(TileInfo.WEST));
    assertEquals(TileInfo.WEST, TileInfo.facingEdge(TileInfo.EAST));
  }
  @Test
  public void test_rotate(){
    //we want to test rotate of
    // a b c      G D A
    // d e f  ->  H E B with lower->upper rotate tile mapping
    // g h i      I F C  
    HashMap<String, Square> mapping = new HashMap<String, Square>();
    Square[] nineSquares = new Square[9];
    Square[] capSquares = new Square[9];
    for (int i =0; i < 9; i++){
      char letter=(char)('a'+i);
      String str=""+letter;
      char capLetter = (char) ('A' + i);
      String capStr = "" + capLetter;
      nineSquares[i] = new Square(str, str, str, capStr);
      mapping.put(str, nineSquares[i]);
      capSquares[i] = new Square(capStr, capStr, capStr, str);
      mapping.put(capStr, capSquares[i]);
    }
    Square[][] inp = new Square[3][3];
    int index = 0;
    for (int y = 0; y < 3; y++){
      for (int x = 0; x < 3; x++){
        inp[y][x] = nineSquares[index];
        index++;
      }
    }
    String[] edgeTags = new String[] { "north", "south", "east", "west" };
    TileInfo test = new TileInfo("test", inp, true, edgeTags);
    TileInfo t2 = test.rotate(mapping);
    //check that edges came out right.
    assertEquals("north:r", t2.getEdgeTag(TileInfo.EAST));
    assertEquals("east:r",t2.getEdgeTag(TileInfo.SOUTH));
    assertEquals("south:r",t2.getEdgeTag(TileInfo.WEST));
    assertEquals("west:r",t2.getEdgeTag(TileInfo.NORTH));
    
    assertEquals("A", t2.getSquareAt(2,0).getId());
    assertEquals("B", t2.getSquareAt(2,1).getId());
    assertEquals("C", t2.getSquareAt(2,2).getId());
    assertEquals("D", t2.getSquareAt(1,0).getId());
    assertEquals("E", t2.getSquareAt(1,1).getId());
    assertEquals("F", t2.getSquareAt(1,2).getId());
    assertEquals("G", t2.getSquareAt(0,0).getId());
    assertEquals("H", t2.getSquareAt(0,1).getId());
    assertEquals("I", t2.getSquareAt(0,2).getId());
  }
}
