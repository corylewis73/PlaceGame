package com.example.placegame;

import java.util.Random;

public class Player {
    public String playerColor;

    //default constructor
    //Adds basic tiles and variables and instantiates a random color.
    public Player()
    {
        regularUses = 5;
        horizontalUses = 1;
        verticalUses = 1;
        totalTilesLeft = 7;

        Random obj = new Random();
        int rand_num = obj.nextInt(0xffffff + 1);
        playerColor = String.format("#%06x", rand_num);
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