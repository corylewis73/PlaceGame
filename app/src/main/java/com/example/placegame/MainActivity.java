package com.example.placegame;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Game game;
    //Will be used and initialized later.
    // Determines which map to load. -1 = empty, 0 = full board
    public int mapID = -1;
    private TextView turnToMove;
    private TextView tilesLeft;
    private TextView score;
    private Handler myHandler;

    //myUpdateClass is used for updating the GUI
    private class myUpdateClass implements Runnable {
        //Constructor used to make, can run to do things later.
        private String color, turn = "Turn To Move: ", tiles = "Tiles Left: ";
        private Integer score_inner;
        private int i;
        private int j;

        public myUpdateClass(String playerColor, int turnToMove, int tilesLeft, int vali, int valj) {
            color = playerColor;
            turn = "Turn To Move: " + turnToMove;
            tiles = "Tiles Left: " + tilesLeft;
            i = vali;
            j = valj;
        }

        public myUpdateClass(String playerColor, int turnToMove, int tilesLeft) {
            Random rand = new Random();
            i = rand.nextInt((7-0)+1);
            j = rand.nextInt((7-0)+1);
            color = playerColor;
            turn = "Turn To Move: " + turnToMove;
            tiles = "Tiles Left: " + tilesLeft;
        }
        public int[] coordinatesOfComputer() { int arr[] = {i,j}; return arr;};
        public void updateTurn(String turn_) {
            turn = turn_;
        }

        public void updateTiles(String tiles_) {
            tiles = tiles_;
        }

        public void updateScore(Integer score_) {
            score_inner = score_;
        }

        @Override
        public void run() {
            game.board[i][j].button.setBackgroundColor(Color.parseColor(color));
            turnToMove.setText(turn);
            tilesLeft.setText(tiles);
            score.setText("Score: "+ score_inner);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // For now it will default to full map but later the user can select map
        game = new Game(1, 1);
        initBoard(game,0);
        score = (TextView) findViewById(R.id.textViewScore);
        turnToMove = (TextView) findViewById(R.id.textViewTurnToMove);
        tilesLeft = (TextView) findViewById(R.id.textViewTilesLeft);

        // Not sure if best way. This allows it to have tiles left before first click.
        tilesLeft.setText("Tiles Left: " +  game.getPlayerList().get(game.getTurnToMove()).tilesLeft());
        score.setText("Score: " + game.getPlayerList().get(game.getTurnToMove()).getScore());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {

        // Checks if game is over before allowing a click
        if (!game.isGameOver()) {
            myHandler = new Handler();

            game.getPlayerList().get(game.getTurnToMove()).subtractTile();

            int[] coordinates = game.getIJ(view.getId());

            //Calling using player constructor.
            game.setPlayerForTile(coordinates, game.getTurnToMove());

            myUpdateClass myCl = new myUpdateClass(game.getPlayerList().get(game.getTurnToMove()).playerColor,
                    game.getTurnToMove(), game.getPlayerList().get(game.getTurnToMove()).tilesLeft(), coordinates[0], coordinates[1]);
            System.out.println("score = " + game.getScoreOfPlayer(game.getTurnToMove()));
            int score = game.getScoreOfPlayer(game.getTurnToMove());
//            myCl.updateScore();
            myHandler.post(myCl);

            // Checks if game is over while changing turn
            if(!game.changeTurn()) {
                myCl.updateTurn("Game Over!");
                myCl.updateTiles("Click any square to return to menu.");
                myHandler.post(myCl);
            }

            game.getPlayerList().get(game.getTurnToMove()).subtractTile();

            //Calling using computer constructor
            myCl = new myUpdateClass(game.getPlayerList().get(game.getTurnToMove()).playerColor, game.getTurnToMove(),
                    game.getPlayerList().get(game.getTurnToMove()).tilesLeft());
            game.setPlayerForTile(myCl.coordinatesOfComputer(),game.getTurnToMove());
            myCl.updateScore(game.getScoreOfPlayer(game.getTurnToMove()));
            myHandler.post(myCl);
            myCl.updateScore(score);
            // Checks if game is over while changing turn
            if(!game.changeTurn()) {
                myCl.updateTurn("Game Over!");
                myCl.updateTiles("Click any square to return.");
                myHandler.post(myCl);
            }

        }
        // Returns to menu
        else {
            Intent menuIntent = new Intent(this, Launcher.class);
            startActivity(menuIntent);
        }
        //Need to also change the graphics here

    }

    // Constructs board based off of map selected. Can add more cases for more maps later
    private void initBoard(Game game, int mapID_) {
        mapID = mapID_;
        int i = 0, j= 0 ;
        switch (mapID) {
            case -1:
                while(j < game.board[0].length) {
                    if (i >= game.board.length) {
                        i = 0;
                        j++;
                    }
                    if (j < game.board[0].length) {
                        String buttonID_string = "button" + i + j;
                        int buttonID = getResources().getIdentifier(buttonID_string, "id", getPackageName());
                        Button button = (Button) findViewById(buttonID);
                        button.setOnClickListener(this);
                        game.board[i][j] = new Tile(-1, button);
                        i++;
                    }
                }
                break;

            case 0:
                while(j < game.board[0].length) {
                    if (i >= game.board.length) {
                        i = 0;
                        j++;
                    }
                    if (j < game.board[0].length) {
                        String buttonID_string = "button" + i + j;
                        int buttonID = getResources().getIdentifier(buttonID_string, "id", getPackageName());
                        Button button = (Button) findViewById(buttonID);
                        button.setOnClickListener(this);
                        game.board[i][j] = new Tile(0, button);
                        i++;
                    }
                }
                break;
        }
    }
}




