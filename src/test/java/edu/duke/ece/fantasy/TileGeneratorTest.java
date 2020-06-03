package edu.duke.ece.fantasy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileGeneratorTest {

    @Test
    void generateTileSet() {
        TileGenerator tileGenerator = new TileGenerator(20,20);
        tileGenerator.GenerateTileSet();
        tileGenerator.printTileSet();
    }
}