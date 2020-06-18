package edu.duke.ece.fantasy.database;

public class Mine extends Building {
    int GeneratedGold;
    public Mine(WorldCoord coord) {
        super("mine", coord);
    }

    public Mine(){
        GeneratedGold = 100;

    }
}
