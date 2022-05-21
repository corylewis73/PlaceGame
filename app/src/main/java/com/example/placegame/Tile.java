package com.example.placegame;

import android.widget.Button;

public class Tile {

    // Determines tile color. -1 = not in play, 0 = gray
    public int colorID;


    Button button;

    Tile(int colorID_, Button button_) {
        colorID = colorID_;
        Tile left, right, up, down;
        button = button_;
    }

}
