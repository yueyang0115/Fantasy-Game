package edu.duke.ece.fantacy;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryHandlerTest {
    TerritoryHandler th = new TerritoryHandler();

    @Test
    void getTerritories() {
        List<Territory> res = th.getTerritories(0, 0.0021, 0.0033);
        JSONArray territoryList_arr = new JSONArray();
        for (int i = 0; i < res.size(); i++) {
            Territory t = res.get(i);
            territoryList_arr.put(t.toJSON());
        }
        System.out.println(territoryList_arr.toString());
    }
}