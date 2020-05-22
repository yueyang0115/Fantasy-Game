package edu.duke.ece.fantacy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TerritoryHandler {
    private DBprocessor db;
    private double width_unit = 0.0002;
    private double height_unit = 0.0002;
    private static double[][] offset = {{0, 0.0002}};
    private static ArrayList<Double> x_offset = new ArrayList<>(Arrays.asList(0.0, 0.0002, -0.0002));
    private static ArrayList<Double> y_offset = new ArrayList<>(Arrays.asList(0.0, 0.0002, -0.0002));

    public TerritoryHandler(DBprocessor db) {
        this.db = db;
    }

    // given coordination, return list of territory
    List<Territory> getTerritories(int wid, double x, double y) {
        List<Territory> res = new ArrayList<>();
        // find the center of block
        double center_x = (int) (x / width_unit) * width_unit + width_unit / 2;
        double center_y = (int) (y / width_unit) * height_unit + height_unit / 2;
        // add neighbor block to database, update status of center block
        for (Double x_off : x_offset) {
            for (Double y_off : y_offset) {
                double target_x = center_x + x_off;
                double target_y = center_y + y_off;
                if (x > 180 || x < -180 || y > 90 || y < -90) continue;
                if (!db.checkTerritory(wid, target_x, target_y)) {
                    // if territory haven't been tracked, add it to database
                    db.addTerritory(wid, target_x, target_y, "unexplored");
                }
                if (x_off == 0 && y_off == 0) {
                    // change the status of center territory
//                    db.updateTerritory(wid, target_x, target_y, "explored");
                }
//                res.add(db.getTerritory(wid, x, y));
            }
        }
        return res;
    }
}
