package com.example.placegame;

import android.widget.Button;

public class Tile {

    // Determines tile color. -1 = not in play, 0 = gray
    public int colorID;
    // -1 if not owned, # of player otherwise
    private int playerOwned;

    Button button;

    Tile(int colorID_, Button button_) {
        colorID = colorID_;
        Tile left, right, up, down;
        button = button_;
        playerOwned = -1;
    }

    //Returns as a string representation
    public String toString()
    {
        String myString = ""+playerOwned;
        return myString;
    }


    public int getPlayerOwned() {return playerOwned;}
    public void setPlayerOwned(int val) {playerOwned = val;}

}
