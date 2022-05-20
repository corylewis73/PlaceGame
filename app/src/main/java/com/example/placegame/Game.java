package com.example.placegame;

import android.widget.Button;

import java.util.ArrayList;

public class Game
{
    public Tile[][] board = new Tile[8][8];

    //Support for multiple players
    private ArrayList<Player> playerList = new ArrayList<>();
    private int turnToMove;

    //Constructor, default for 2 players
    public Game() {
        Player playerOne = new Player();
        Player playerTwo = new Player();
        playerList.add(playerOne);
        playerList.add(playerTwo);
        turnToMove = 0;
    }

    //Allows any number of players
    public Game(int players) {
        for (int i=0;i<players;i++) {
            Player newPlayer = new Player();
            playerList.add(newPlayer);
        }
        turnToMove = 0;
    }

    //Used to change the turn of the game. Checks to see if the game is over before the switch
    //returns true if successful, false otherwise.
    public boolean changeTurn() {
        if (!this.isGameOver()) {
            if (turnToMove + 1 >= playerList.size())
                turnToMove = 0;
            else
                turnToMove += 1;
            return true;
        }
        return false;
    }

    //Helper function to check if the game is over
    private boolean isGameOver() {
        for (int i=0;i<playerList.size();i++) {
            if (playerList.get(i).tilesLeft() != 0)
                return false;
        }
        return true;
    }
}
