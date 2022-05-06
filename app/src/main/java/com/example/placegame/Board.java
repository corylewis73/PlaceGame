package com.example.placegame;

public class Board {

    public Tile[][] grid;

    // Determines which map to load. -1 = empty, 0 =
    public int mapID;

    // Constructs board based off of map selected
    Board(int mapID_) {
        mapID = mapID_;
        switch (mapID) {
            case -1:
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        grid[i][j] = new Tile(-1);
                    }
                }
                break;

            case 0:
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        grid[i][j] = new Tile(0);
                    }
                }
                break;
        }
    }

}
