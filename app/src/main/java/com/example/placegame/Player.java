package com.example.placegame;

import java.util.Random;

public class Player {
    public String playerColor;
    // Number of uses for each type of tile in the deck
    public int regularUses;
    public int horizontalUses;
    public int verticalUses;
    private int totalTilesLeft;
    private int score;

    //default constructor
    //Adds basic tiles and variables and instantiates a random color.
    public Player()
    {
        regularUses = 5;
        horizontalUses = 1;
        verticalUses = 1;
        totalTilesLeft = 7;

        //Pick a random color for the player
        Random obj = new Random();
        int rand_num = obj.nextInt(0xffffff + 1);
        playerColor = String.format("#%06x", rand_num);
    }
 	public void setScore(Integer score) {
        this.score = score;
    }

    public int getScore(){
        return score;
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
}
