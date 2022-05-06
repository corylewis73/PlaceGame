package com.example.placegame;

public class Tile {

    // Determines tile color. -1 = not in play, 0 = gray
    public int colorID;

    Tile(int colorID_) {
        colorID = colorID_;
        Tile left, right, up, down;
    }

}
