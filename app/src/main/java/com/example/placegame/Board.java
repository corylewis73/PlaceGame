package com.example.placegame;

import android.widget.Button;

public class Board extends MainActivity {

    public Tile[][] grid = new Tile[8][8];

    // Determines which map to load. -1 = empty, 0 =
    public int mapID;

    // Constructs board based off of map selected. Can add more cases for more maps later
    Board(int mapID_) {
        mapID = mapID_;
        switch (mapID) {
            case -1:
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        String buttonID_string = "button" + i + j;
                        int buttonID = getResources().getIdentifier(buttonID_string, "id", getPackageName());
                        Button button = (Button) findViewById(buttonID);
                        button.setOnClickListener(this);
                        grid[i][j] = new Tile(-1, button);
                    }
                }
                break;

            case 0:
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        String buttonID_string = "button" + i + j;
                        int buttonID = getResources().getIdentifier(buttonID_string, "id", getPackageName());
                        Button button = (Button) findViewById(buttonID);
                        button.setOnClickListener(this);
                        grid[i][j] = new Tile(0, button);
                    }
                }
                break;
        }
    }
}
