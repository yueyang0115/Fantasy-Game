package edu.duke.ece.fantasy.database;

public class Mine extends DBBuilding {
    int GeneratedGold;
    public Mine(WorldCoord coord) {
        super("mine", coord);
    }

    public Mine(){
        GeneratedGold = 100;

    }
}
