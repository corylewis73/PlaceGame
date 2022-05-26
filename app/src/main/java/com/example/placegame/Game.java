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


	  public int getScoreOfPlayer(int playerVal) {
        int sum = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j].getPlayerOwned() == playerVal) {
                    sum++;
                }
            }
        }
        return sum;
    }

	 public void setPlayerForTile(int [] arr, int playerVal) {
        int i = arr[0];
        int j = arr[1];
        board[i][j].setPlayerOwned(playerVal);
    }

    //Changes the color of a tile.
    //Might be outdated at this point.
    public void editTile(int number)
    {
        //Fix this to make it more efficient
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (number == board[i][j].button.getId()) {
                    board[i][j].button.setBackgroundColor(Color.parseColor(playerList.get(turnToMove).playerColor));
                }
            }
        }
        //This will break at the end, fix this later.
        changeTurn();
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
