package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Territory;

import java.util.*;

public class TileGenerator {
    int x_block_num;
    int y_block_num;
    Set<TerritoryBlock> function_block_set;
    TerritoryBlock[][] map;

    public TileGenerator(int x_block_num, int y_block_num) {
        this.x_block_num = x_block_num;
        this.y_block_num = y_block_num;
        map = new TerritoryBlock[y_block_num][x_block_num];
    }

    public TerritoryBlock[][] GenerateTileSet() {
        // randomly add function block
        function_block_set = GenerateBlocks("grass");
        // randomly add road block

        // connect road with function block

        // add obstacle block
        for(int i=0;i<y_block_num;i++){
            for(int j=0;j<x_block_num;j++){
                if(map[i][j]==null){
                    map[i][j] = new TerritoryBlock(j,i,"mountain",null);
                }
            }
        }
        return map;
    }

    private Set<TerritoryBlock> GenerateBlocks(String type) {
        // generate coordinate of blocks
        Random rand = new Random();
        int block_num = 1 + rand.nextInt(8);
        Set<TerritoryBlock> res = new HashSet<>();
        while (res.size() < block_num) { // generate random number of blocks
            int x = rand.nextInt(x_block_num);
            int y = rand.nextInt(y_block_num);
            TerritoryBlock new_block = new TerritoryBlock(x, y, type, null);
            if (map[y][x] == null) {
                map[y][x] = new_block;
                res.add(new_block);
            }
        }
        return res;
    }
}
