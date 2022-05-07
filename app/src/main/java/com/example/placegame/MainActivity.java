package com.example.placegame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // For now it will default to full map but later the user can select map
        board = new Board(0);
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
        for (int i = 0; i < board.grid.length; i++) {
            for (int j = 0; j < board.grid[i].length; j++) {
                if (view.getId() == board.grid[i][j].button.getId()) {
                    board.grid[i][j].button.setBackgroundColor(Color.parseColor("#ff0000"));
                }
            }
        }
    }
}

