package com.example.placegame;

import android.graphics.Color;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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


    public Game(String name)
    {
        Player playerOne = playerOne = new Player("Host");
        Player playerTwo = playerTwo = new Player("Client");
        playerList.add(playerOne);
        playerList.add(playerTwo);
    }

    //Allows any number of players
    public Game(int players) {
        for (int i=0;i<players;i++) {
            Player newPlayer = new Player();
            playerList.add(newPlayer);
        }
        turnToMove = 0;
    }

    //General maker function for later.
    public Game(int players, int computers)
    {
        //This might not be allowed in this lang but maybe?
        for (int i=0;i<players;i++) {
            Player newPlayer = new Player();
            playerList.add(newPlayer);
        }
        for (int i=0;i<computers;i++) {
            Player newPlayer = new Player();
            playerList.add(newPlayer);
        }
    }

    //Gets the state of the game
    public String returnState()
    {
        String state = ""; //Might need ? here.
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                state = state +"l"+ board[i][j].toString();
            }
        }
        return state;
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
    public boolean isGameOver() {
        for (int i=0;i<playerList.size();i++) {
            if (playerList.get(i).tilesLeft() != 0)
                return false;
        }
        return true;
    }

    //Gets the score of the board at the time.
    public ArrayList<Integer> getScore()
    {
        return null;
    }

    //Changes the ID of a tile of a tile.
    //Might be outdated at this point.
    public void editTile(int [] coordinates, int value)
    {
        board[coordinates[0]][coordinates[1]].setPlayerOwned(value);
    }

    public int getTurnToMove() {
        return turnToMove;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    //returns i and j position of a view.getID() call
    public int[] getIJ(int number)
    {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (number == board[i][j].button.getId()) {
                    return new int[]{i,j};
                }
            }
        }
        return null; //should not reach here
    }
}
