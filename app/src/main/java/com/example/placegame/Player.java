package com.example.placegame;

public class Player {
    public String playerColor;

    public Player()
    {

    }


    // Number of uses for each type of tile in the deck
    public int regularUses = 10;
    public int horizontalUses = 1;
    public int verticalUses = 1;
    private int totalTilesLeft;

    //This method is to see how many moves a player has left.
    public int tilesLeft()
    {
        return totalTilesLeft;
    }

}
