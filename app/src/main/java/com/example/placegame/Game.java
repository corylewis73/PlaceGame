package com.example.placegame;

import java.util.ArrayList;

public class Game
{
    //Support for multiple players
    private ArrayList<Player> playerList = new ArrayList<>();
    private int turnToMove;

    //Constructor, default for 2 players
    public Game()
    {
        Player playerOne = new Player();
        Player playerTwo = new Player();
        playerList.add(playerOne);
        playerList.add(playerTwo);
        turnToMove = 0;
    }

    //Allows any number of players
    public Game(int players)
    {
        for (int i=0;i<players;i++)
        {
            Player newPlayer = new Player();
            playerList.add(newPlayer);
        }
        turnToMove = 0;
    }

    public changeTurn()
    {

    }

    private isGameOver()
    {

    }

}
