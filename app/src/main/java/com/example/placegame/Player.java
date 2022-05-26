package com.example.placegame;

import java.util.Random;

public class Player {
    public String playerColor;
    // Number of uses for each type of tile in the deck
    public int regularUses;
    public int horizontalUses;
    public int verticalUses;
    private int totalTilesLeft;
    private String myName;

    // 0 = regular, 1 = horizontal, 2 = vertical
    public int tileType = 0;

    //default constructor
    //Adds basic tiles and variables and instantiates a random color.
    public Player()
    {
        regularUses = 10;
        horizontalUses = 3;
        verticalUses = 3;
        totalTilesLeft = 16;

        //Pick a random color for the player
        Random obj = new Random();
        int rand_num = obj.nextInt(0xffffff + 1);
        playerColor = String.format("#%06x", rand_num);
    }

    public Player(String name)
    {
        myName = name;
        regularUses = 5;
        horizontalUses = 1;
        verticalUses = 1;
        totalTilesLeft = 7;

        //Pick a random color for the player
        Random obj = new Random();
        int rand_num = obj.nextInt(0xffffff + 1);
        playerColor = String.format("#%06x", rand_num);
    }

    //This method is to see how many moves a player has left.
    public int tilesLeft()
    {
        return totalTilesLeft;
    }

    public void subtractTile()
    {
        totalTilesLeft--;
    }

    //Helper function
    public String getMyName(){return myName;}
}
