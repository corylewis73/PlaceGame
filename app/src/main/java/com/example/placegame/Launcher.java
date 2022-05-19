package com.example.placegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Launcher extends AppCompatActivity implements View.OnClickListener{

    private TextView titleText;
    private TextView instructionsText;

    private Button singlePlayer;
    private Button multiplayerHost;
    private Button multiplayerPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        singlePlayer = (Button) findViewById(R.id.buttonSP);
        multiplayerHost = (Button) findViewById(R.id.buttonMPH);
        multiplayerPlayer = (Button) findViewById(R.id.buttonMPP);
        singlePlayer.setOnClickListener(this);
        multiplayerHost.setOnClickListener(this);
        multiplayerPlayer.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.buttonSP):
                //implement here.
                Intent singlePlayerIntent = new Intent(this, MainActivity.class);
                startActivity(singlePlayerIntent);
            case (R.id.buttonMPH):
                break;
            case (R.id.buttonMPP):
                break;
        }
    }
}