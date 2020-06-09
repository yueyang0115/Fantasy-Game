package edu.duke.ece.fantasy.worldgen;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TileGeneratorTest {

  
    @Test
    void readTileTest() {
      TileGenerator tg = TileGenerator.forWorldType("mainworld");
      assertEquals(20, tg.tileWidth);
      assertEquals(20, tg.tileHeight);
      assertEquals("Id:0", tg.startTileName);
      assertEquals("Id:2", tg.panicTile);
      Square river = tg.squareMapping.get("|"); //get the river NS tile
      assertNotNull(river);
      assertEquals("riverNS", river.getImageName());
      assertEquals("|", river.getId());
      assertEquals("river", river.getType());
      assertEquals("-", river.getRotId());
      //TODO: more here
    }
  @Test
  void alignToTileSizeTest(){
    assertEquals(-1, TileGenerator.alignToTileSize(1,99,20));
    assertEquals(1, TileGenerator.alignToTileSize(1,99,2));
    assertEquals(83, TileGenerator.alignToTileSize(85,3,10));
  }

  
}
