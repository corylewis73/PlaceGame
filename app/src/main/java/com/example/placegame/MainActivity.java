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

import org.w3c.dom.Text;

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
    private Button regularButton, horizontalButton, verticalButton;

    //myUpdateClass is used for updating the GUI
    private class myUpdateClass implements Runnable {
        //Constructor used to make, can run to do things later.
        private String color, turn = "Turn To Move: ";
        private String tiles = "Tiles Left: ";
        private String scoreString = "";
        private String regular = "Regular\nx ";
        private String horizontal = "Horizontal\nx ";
        private String vertical = "Vertical\nx ";
        private int i = -1;
        private int j = -1;

        public myUpdateClass(){}

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

        public void updateUses(String  regUses, String horUses, String vertUses) {
            regular = regUses;
            horizontal = horUses;
            vertical = vertUses;
        }

        public void updateTurn(String turn_) {
            turn = turn_;
        }

        public void updateTiles(String tiles_) {
            tiles = tiles_;
        }

        public void updateScore(String s) {
            scoreString = s;
        }

        @Override
        public void run() {
            if (i != -1 || j != -1)
                game.board[i][j].button.setBackgroundColor(Color.parseColor(color));
            turnToMove.setText(turn);
            tilesLeft.setText(tiles);
            regularButton.setText(regular);
            horizontalButton.setText(horizontal);
            verticalButton.setText(vertical);
            score.setText(scoreString);

            // Sets the tile selecting buttons' colors
            switch (game.getPlayerList().get(game.getTurnToMove()).tileType) {
                case 0:
                    regularButton.setBackgroundColor(Color.parseColor("#ffffff"));
                    horizontalButton.setBackgroundColor(Color.parseColor("#ff6600"));
                    verticalButton.setBackgroundColor(Color.parseColor("#ff6600"));
                    break;

                case 1:
                    regularButton.setBackgroundColor(Color.parseColor("#ff6600"));
                    horizontalButton.setBackgroundColor(Color.parseColor("#ffffff"));
                    verticalButton.setBackgroundColor(Color.parseColor("#ff6600"));
                    break;

                case 2:
                    regularButton.setBackgroundColor(Color.parseColor("#ff6600"));
                    horizontalButton.setBackgroundColor(Color.parseColor("#ff6600"));
                    verticalButton.setBackgroundColor(Color.parseColor("#ffffff"));
                    break;
            }
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

        regularButton = (Button) findViewById(R.id.buttonRegular);
        horizontalButton = (Button) findViewById(R.id.buttonHorizontal);
        verticalButton = (Button) findViewById(R.id.buttonVertical);

        regularButton.setOnClickListener(this);
        horizontalButton.setOnClickListener(this);
        verticalButton.setOnClickListener(this);
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

            myUpdateClass myCl = new myUpdateClass();

            myCl.updateUses("Regular\nx " + game.getPlayerList().get(game.getTurnToMove()).regularUses,
                    "Horizontal\nx " + game.getPlayerList().get(game.getTurnToMove()).horizontalUses,
                    "Vertical\nx " + game.getPlayerList().get(game.getTurnToMove()).verticalUses);

            // Select tile type
            switch (view.getId()) {
                case R.id.buttonRegular:
                    game.getPlayerList().get(game.getTurnToMove()).tileType = 0;
                    myHandler.post(myCl);
                    break;

                case R.id.buttonHorizontal:
                    game.getPlayerList().get(game.getTurnToMove()).tileType = 1;
                    myHandler.post(myCl);
                    break;

                case R.id.buttonVertical:
                    game.getPlayerList().get(game.getTurnToMove()).tileType = 2;
                    myHandler.post(myCl);
                    break;

                default:
                    // Checks if currently selected tile has uses left
                    if (usesLeft(game.getPlayerList().get(game.getTurnToMove()).tileType)) {
                        int[] coordinates = game.getIJ(view.getId());

                        myCl = new myUpdateClass(game.getPlayerList().get(game.getTurnToMove()).playerColor,
                                game.getTurnToMove(), game.getPlayerList().get(game.getTurnToMove()).tilesLeft(), coordinates[0], coordinates[1]);

                        clickBoard(myCl, coordinates);

                        game.getPlayerList().get(game.getTurnToMove()).subtractTile();
                        myCl.updateUses("Regular\nx " + game.getPlayerList().get(game.getTurnToMove()).regularUses,
                                "Horizontal\nx " + game.getPlayerList().get(game.getTurnToMove()).horizontalUses,
                                "Vertical\nx " + game.getPlayerList().get(game.getTurnToMove()).verticalUses);
                        myHandler.post(myCl);

                        // Checks if game is over while changing turn
                        if(!game.changeTurn()) {
                            myCl.updateTurn("Game Over!");
                            myCl.updateTiles("Tap any square to return to menu.");
                            myCl.updateScore("Score: " + getFinalScore());
                            myHandler.post(myCl);
                        }


                        Random rand = new Random();

                        int a = 0;
                        boolean isGood = false;
                        while(!isGood) {
                            game.getPlayerList().get(game.getTurnToMove()).tileType = rand.nextInt(3);
                            isGood = usesLeft(game.getPlayerList().get(game.getTurnToMove()).tileType);
                            a++;

                            // Check all of them is RNG is taking too long
                            if (a == 10) {
                                for (int b = 0; !isGood; b++) {
                                    game.getPlayerList().get(game.getTurnToMove()).tileType = b;
                                    isGood = usesLeft(game.getPlayerList().get(game.getTurnToMove()).tileType);
                                }
                            }
                        }

                        coordinates[0] = rand.nextInt((7-0)+1);
                        coordinates[1] = rand.nextInt((7-0)+1);
                        myCl = new myUpdateClass(game.getPlayerList().get(game.getTurnToMove()).playerColor, game.getTurnToMove(),
                                game.getPlayerList().get(game.getTurnToMove()).tilesLeft(), coordinates[0], coordinates[1]);

                        clickBoard(myCl, coordinates);

                        game.getPlayerList().get(game.getTurnToMove()).subtractTile();

                        // Checks if game is over while changing turn
                        if(!game.changeTurn()) {
                            myCl.updateTurn("Game Over!");
                            myCl.updateTiles("Click any square to return.");
                            myCl.updateScore("Score: " + getFinalScore());
                            myHandler.post(myCl);
                        }
                        myCl.updateUses("Regular\nx " + game.getPlayerList().get(game.getTurnToMove()).regularUses,
                                "Horizontal\nx " + game.getPlayerList().get(game.getTurnToMove()).horizontalUses,
                                "Vertical\nx " + game.getPlayerList().get(game.getTurnToMove()).verticalUses);
                        myHandler.post(myCl);
                    }
                    break;
            }
        }
        // Returns to menu
        else {
            Intent menuIntent = new Intent(this, Launcher.class);
            startActivity(menuIntent);
        }
    }

    private int getFinalScore() {
        int scoreCount = 0;
        for (int i = 0; i < game.board.length; i++) {
            for (int j = 0; j < game.board[i].length; j++) {
                if (game.board[i][j].getPlayerOwned() == 0)
                    scoreCount++;
                else if (game.board[i][j].getPlayerOwned() == 1)
                    scoreCount--;
            }
        }
        return scoreCount;
    }

    // Helper function that checks if the tile type selected has uses left
    private boolean usesLeft(int type) {
        switch (type) {
            case 0:
                if (game.getPlayerList().get(game.getTurnToMove()).regularUses == 0)
                    return false;
                break;

            case 1:
                if (game.getPlayerList().get(game.getTurnToMove()).horizontalUses == 0)
                    return false;
                break;

            case 2:
                if (game.getPlayerList().get(game.getTurnToMove()).verticalUses == 0)
                    return false;
                break;
        }
        return true;
    }

    // Helper function for board clicking logic
    private void clickBoard(myUpdateClass myCl, int[] coordinates) {
        // Click logic depending on which tile type selected
        switch (game.getPlayerList().get(game.getTurnToMove()).tileType) {
            case 0:
                game.getPlayerList().get(game.getTurnToMove()).regularUses--;
                myHandler.post(myCl);
                game.board[coordinates[0]][coordinates[1]].setPlayerOwned(game.getTurnToMove());
                break;

            case 1:
                game.getPlayerList().get(game.getTurnToMove()).horizontalUses--;
                for (int i = 0; i < game.board[coordinates[0]].length; i++) {
                    myCl = new myUpdateClass(game.getPlayerList().get(game.getTurnToMove()).playerColor,
                            game.getTurnToMove(), game.getPlayerList().get(game.getTurnToMove()).tilesLeft(), coordinates[0], i);
                    myHandler.post(myCl);
                    game.board[coordinates[0]][i].setPlayerOwned(game.getTurnToMove());
                }
                break;

            case 2:
                game.getPlayerList().get(game.getTurnToMove()).verticalUses--;
                for (int i = 0; i < game.board.length; i++) {
                    myCl = new myUpdateClass(game.getPlayerList().get(game.getTurnToMove()).playerColor,
                            game.getTurnToMove(), game.getPlayerList().get(game.getTurnToMove()).tilesLeft(), i, coordinates[1]);
                    myHandler.post(myCl);
                    game.board[i][coordinates[1]].setPlayerOwned(game.getTurnToMove());
                }
                break;
        }
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




