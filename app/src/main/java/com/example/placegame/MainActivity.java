package com.example.placegame;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Thread.sleep;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Game game;
    //Will be used and initialized later.
    // Determines which map to load. -1 = empty, 0 = full board
    public int mapID = -1;
    private TextView turnToMove;
    private TextView score;

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
        game.editTile(view.getId());
        try {
            sleep(100); //Find out how to do this
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        game.computerTurn();
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




