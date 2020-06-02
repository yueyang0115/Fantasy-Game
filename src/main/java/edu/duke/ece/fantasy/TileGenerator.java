package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.Territory;

import java.util.*;

public class TileGenerator {
    int x_block_num;
    int y_block_num;
    TerritoryBlock[][] map;

    public TileGenerator(int x_block_num, int y_block_num) {
        this.x_block_num = x_block_num;
        this.y_block_num = y_block_num;
        map = new TerritoryBlock[y_block_num][x_block_num];
    }

//    public TerritoryBlock[][] InitialMap() {
//
//    }

    public void printTileSet() {
        for (int i = 0; i < map.length; i++) {
            StringBuilder tmp = new StringBuilder();
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] != null) {
                    tmp.append((map[i][j].getType().equals("grass")) ? "o" : "x");
                } else {
                    tmp.append("?");
                }
            }
            System.out.println(tmp);
        }
    }

    public TerritoryBlock[][] GenerateTileSet() {
        // randomly add function block
        Set<TerritoryBlock> function_block_set = GenerateBlocks("grass", 20, 25);
        // randomly add road block
        Set<TerritoryBlock> road_block_set = GenerateBlocks("grass", function_block_set.size(), function_block_set.size());
        // connect road with function block
        connect_blocks(function_block_set, road_block_set);
        // add obstacle block
        for (int i = 0; i < y_block_num; i++) {
            for (int j = 0; j < x_block_num; j++) {
                if (map[i][j] == null) {
                    map[i][j] = new TerritoryBlock(j, i, "mountain", null);
                }
            }
        }
        return map;
    }

    private void connect_blocks(Set<TerritoryBlock> function_blocks, Set<TerritoryBlock> road_blocks) {
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        for (TerritoryBlock block : road_blocks) {
            // bfs
            Queue<TerritoryBlock> queue = new ArrayDeque<>();
            Set<TerritoryBlock> visited = new HashSet<>();
            queue.add(block);
            visited.add(block);
            while (!queue.isEmpty()) {
                TerritoryBlock top_block = queue.remove();
                if (function_blocks.contains(top_block)) {
                    // hit the end block, add path to map
                    TerritoryBlock parent_block = top_block.parent;
                    while (parent_block != block) {
                        map[parent_block.getY()][parent_block.getX()] = parent_block;
                        parent_block = parent_block.parent;
                    }
                    function_blocks.remove(top_block);
                    break;
                }
                for (int i = 0; i < 4; i++) {
                    int[] dir = dirs[i];
                    TerritoryBlock new_block = new TerritoryBlock(top_block.getX() + dir[0], top_block.getY() + dir[1], "grass", top_block);
                    if (0 < new_block.getX() && new_block.getX() < x_block_num && 0 < new_block.getY() && new_block.getY() < y_block_num && !visited.contains(new_block)) {
                        queue.add(new_block);
                        visited.add(new_block);
                    }
                }
            }
        }
    }

    private Set<TerritoryBlock> GenerateBlocks(String type, int min_num, int max_num) {
        // generate coordinate of blocks
        Random rand = new Random();
        int block_num = min_num + rand.nextInt((max_num - min_num) + 1);
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
