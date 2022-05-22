package com.example.placegame;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Thread.sleep;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Game game;
    //Will be used and initialized later.
    // Determines which map to load. -1 = empty, 0 = full board
    public int mapID = -1;
    private TextView turnToMove;
    private TextView score;
    private Handler myHandler;

    private class myUpdateClass implements Runnable {
        //Constructor used to make, can run to do things later.
        private String color;
        private int i;
        private int j;

        public myUpdateClass(String playerColor, int vali, int valj) {
            color = playerColor;
            i = vali;
            j = valj;
        }

        public myUpdateClass(String playerColor)
        {
            Random rand = new Random();
            i = rand.nextInt((7-0)+1);
            j = rand.nextInt((7-0)+1);
            //Does this work?
            color = playerColor;
        }

        @Override
        public void run() {
            game.board[i][j].button.setBackgroundColor(Color.parseColor(color));
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
        myHandler = new Handler();
        int[] coordinates = game.getIJ(view.getId());
        myUpdateClass myCl = new myUpdateClass(game.getPlayerList().get(game.getTurnToMove()).playerColor,
                coordinates[0], coordinates[1]);
        myHandler.post(myCl);

        game.changeTurn();
        myCl = new myUpdateClass(game.getPlayerList().get(game.getTurnToMove()).playerColor);
        myHandler.post(myCl);
        //Need to also change the graphics here

    }

    // Constructs board based off of map selected. Can add more cases for more maps later
    void initBoard(Game game, int mapID_) {
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




