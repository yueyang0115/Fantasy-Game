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


    public void printTileSet() {
        for (int i = 0; i < map.length; i++) {
            StringBuilder tmp = new StringBuilder();
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] != null) {
                    if (map[i][j].getType().equals("grass")) {
                        tmp.append("g");
                    } else if (map[i][j].getType().equals("river")) {
                        tmp.append("i");
                    } else {
                        tmp.append("m");
                    }
                } else {
                    tmp.append("?");
                }
            }
            System.out.println(tmp);
        }
        System.out.println("-----------");
    }

    public TerritoryBlock[][] GenerateTileSet() {
        // add river
        add_terrain_path("river",10,15);
        // add mountain
        add_terrain_path("mountain",10,15);
        // add grass
        for (int i = 0; i < y_block_num; i++) {
            for (int j = 0; j < x_block_num; j++) {
                if (map[i][j] == null) {
                    map[i][j] = new TerritoryBlock(j, i, "grass", null);
                }
            }
        }
        return map;
    }

    private void add_terrain_path(String type,int min_size,int max_size){
        // add start block
        Set<TerritoryBlock> set_1 = GenerateBlocks(type, min_size, max_size);
        // add end block
        Set<TerritoryBlock> set_2 = GenerateBlocks(type, set_1.size(), set_1.size());
        // connect blocks
        connect_blocks(set_1, set_2,5);
        // delete unreachable block
        delete_blocks(set_1);
    }

    private void delete_blocks(Set<TerritoryBlock> blocks) {
        for (TerritoryBlock block : blocks) {
            map[block.getY()][block.getX()] = null;
        }
    }

    private void connect_blocks(Set<TerritoryBlock> end_blocks, Set<TerritoryBlock> road_blocks,int path_len) {
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        String road_type = "grass";
        for (TerritoryBlock block : road_blocks) { // get road block type
            road_type = block.getType();
            break;
        }
        Set<TerritoryBlock> tmp = new HashSet<>(road_blocks);
        for (TerritoryBlock start_block : road_blocks) {
            // bfs
            Queue<TerritoryBlock> queue = new ArrayDeque<>();
            Set<TerritoryBlock> visited = new HashSet<>();
            queue.add(start_block);
            visited.add(start_block);
            int level = 0;
            while (!queue.isEmpty()) {
                TerritoryBlock top_block = queue.remove();
                if (end_blocks.contains(top_block) && level > path_len) {
                    // hit the end block, add path to map; make sure the path length is greater than given number
                    TerritoryBlock parent_block = top_block.parent;
                    while (parent_block != start_block) { // traverse from the end block to the start block
                        map[parent_block.getY()][parent_block.getX()] = parent_block;
                        parent_block = parent_block.parent;
                    }
                    end_blocks.remove(top_block);
                    tmp.remove(start_block);
                    break;
                }
                for (int i = 0; i < 4; i++) {
                    // try 4 directions
                    int[] dir = dirs[i];
                    int new_x = top_block.getX() + dir[0];
                    int new_y = top_block.getY() + dir[1];
                    TerritoryBlock new_block = new TerritoryBlock(new_x, new_y, road_type, top_block);
                    if (0 < new_x && new_x < x_block_num && 0 < new_y && new_y < y_block_num && !visited.contains(new_block) && (map[new_y][new_x] == null || map[new_y][new_x].getType().equals(road_type) || end_blocks.contains(new_block))) {
//                    if (0 < new_x && new_x < x_block_num && 0 < new_y && new_y < y_block_num && map[new_y][new_x] == null) {
                        queue.add(new_block);
                        visited.add(new_block);
                    }
                }
                level += 1;
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
