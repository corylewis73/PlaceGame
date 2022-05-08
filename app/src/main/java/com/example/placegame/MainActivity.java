package com.example.placegame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public Tile[][] board = new Tile[8][8];

    // Determines which map to load. -1 = empty, 0 = full board
    public int mapID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // For now it will default to full map but later the user can select map
        initBoard(0);
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
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (view.getId() == board[i][j].button.getId()) {
                    board[i][j].button.setBackgroundColor(Color.parseColor("#ff0000"));
                }
            }
        }
    }

    // Constructs board based off of map selected. Can add more cases for more maps later
    void initBoard(int mapID_) {
        mapID = mapID_;
        switch (mapID) {
            case -1:
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[i].length; j++) {
                        String buttonID_string = "button" + i + j;
                        int buttonID = getResources().getIdentifier(buttonID_string, "id", getPackageName());
                        Button button = (Button) findViewById(buttonID);
                        button.setOnClickListener(this);
                        board[i][j] = new Tile(-1, button);
                    }
                }
                break;

            case 0:
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[i].length; j++) {
                        String buttonID_string = "button" + i + j;
                        int buttonID = getResources().getIdentifier(buttonID_string, "id", getPackageName());
                        Button button = (Button) findViewById(buttonID);
                        button.setOnClickListener(this);
                        board[i][j] = new Tile(0, button);
                    }
                }
                break;
        }
    }
}




