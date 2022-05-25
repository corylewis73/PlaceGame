package com.example.placegame;
//Originally worked with my own socket programming code, but it was inconsistent so went and used tutorial from Sarthi Technology on YouTube
//Because it used Android's built-in packages which I assumed were more stable than doing it manually.

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.InetAddresses;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultihostPage extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    //Necessary for P2P
    private final IntentFilter intentFilter = new IntentFilter();
    private Button wifiButton;
    public Button btnSend;
    public Button btnOnOff;
    public ListView listView;
    public TextView read_msg_box;
    public TextView connectionStatus;

    private Game game;
    public int mapID = -1;
    private TextView turnToMove;
    private TextView tilesLeft;
    private TextView score;
    private String whoAmI;

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    WifiManager wifiManager;
    BroadcastReceiver mReceiver;
    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;
    public Handler myHandler;
    public String identity;

    static final int MESSAGE_READ=1;
    public ServerClass serverClass;
    public ClientClass clientClass;
    public SendReceive sendReceive;


    //This runnable class sends a message upon being called to sendReceive.
    public class myUpdateClass implements Runnable {
        public String mymsg;
        public myUpdateClass(String msg)
        {
            mymsg = msg;
        }

        @Override
        public void run() {
            if (mymsg == null)
            {
                mymsg = " ";
            }
            sendReceive.write(mymsg.getBytes());
        }
    }

    //myUpdateClass is used for updating the GUI
    private class gameUpdateClass implements Runnable {
        //Constructor used to make, can run to do things later.
        private String color, turn = "Turn To Move: ", tiles = "Tiles Left: ";
        private int i;
        private int j;

        public gameUpdateClass(String playerColor, int turnToMove, int tilesLeft, int vali, int valj) {
            color = playerColor;
            turn = "Turn To Move: " + turnToMove;
            tiles = "Tiles Left: " + tilesLeft;
            i = vali;
            j = valj;
        }

        public gameUpdateClass(String playerColor, int turnToMove, int tilesLeft) {
            Random rand = new Random();
            i = rand.nextInt((7-0)+1);
            j = rand.nextInt((7-0)+1);
            color = playerColor;
            turn = "Turn To Move: " + turnToMove;
            tiles = "Tiles Left: " + tilesLeft;
        }

        public void updateTurn(String turn_) {
            turn = turn_;
        }

        public void updateTiles(String tiles_) {
            tiles = tiles_;
        }

        @Override
        public void run() {
            game.board[i][j].button.setBackgroundColor(Color.parseColor(color));
            turnToMove.setText(turn);
            tilesLeft.setText(tiles);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multihost_page);

        //Security Bypass. Must have SDK version greater than 9.
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btnSend = (Button) findViewById(R.id.sendButton);
        listView = (ListView) findViewById(R.id.peerListView);
        btnOnOff = (Button) findViewById(R.id.btnOnOff);
        connectionStatus = (TextView) findViewById(R.id.connectionStatus);

        //Wifi initializations
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiButton = findViewById(R.id.buttonDISCOVER);
        wifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MultihostPage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    ActivityCompat.requestPermissions(MultihostPage.this, new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE}, 1);
                    ActivityCompat.requestPermissions(MultihostPage.this, new String[]{Manifest.permission.INTERNET}, 1);
                    //This might not work???

                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        connectionStatus.setText("Discovery Started");
                    }

                    @Override
                    public void onFailure(int i) {
                        connectionStatus.setText("Failed to start Discovery");
                    }
                });
            }
        });
        btnOnOff = (Button) findViewById(R.id.btnOnOff);
        btnOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);
                    btnOnOff.setText("OFF");
                } else {
                    wifiManager.setWifiEnabled(true);
                    btnOnOff.setText("ON");
                }
            }
        });
        listView.setOnItemClickListener(this);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_multiplayer_page);
                game = new Game("wifi");
                initBoard(game,0);
                score = (TextView) findViewById(R.id.textViewScore);
                turnToMove = (TextView) findViewById(R.id.textViewTurnToMove);
                tilesLeft = (TextView) findViewById(R.id.textViewTilesLeft);
                tilesLeft.setText("Tiles Left: " +  game.getPlayerList().get(game.getTurnToMove()).tilesLeft());
            }
        });
        myHandler = new Handler();

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mChannel, mManager, this);
        // Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    //This displays text in the message field in the original app
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what)
            {
                case MESSAGE_READ:
                    byte[] readBuff= (byte[]) msg.obj;
                    String tempMsg = new String(readBuff, 0, msg.arg1);
                    read_msg_box.setText(tempMsg);
                    break;
            }
            return true;
        }
    });

    //This is for finding peers (we need this)
    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if (!peerList.getDeviceList().equals(peers)) {
                peers.clear();
                peers.addAll(peerList.getDeviceList());

                deviceNameArray = new String[peerList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[peerList.getDeviceList().size()];
                int index = 0;

                for (WifiP2pDevice device : peerList.getDeviceList()) {
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    index++;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, deviceNameArray);
                listView.setAdapter(adapter);
                if (peers.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Device Found", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    //When the group is formed, we use this class to make the client and server classes that will be used to communicate.
    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress groupOwnerAddress = wifiP2pInfo.groupOwnerAddress;

            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner)
            {
                identity = "Host";
                connectionStatus.setText("Host");
                serverClass = new ServerClass();
                serverClass.start();
            } else if (wifiP2pInfo.groupFormed)
            {
                identity = "Client";
                connectionStatus.setText("Client");
                clientClass = new ClientClass(groupOwnerAddress);
                clientClass.start();
            }
        }
    };

    //Dont touch these I think
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    //This class is for sending and receiving messages?
    public class SendReceive extends Thread implements Serializable{
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public SendReceive(Socket skt)
        {
            socket = skt;
            try {
                inputStream=socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024]; //Might need to be gigger
            int bytes;

            while (socket != null)
            {
                try{
                    bytes = inputStream.read(buffer);
                    if (bytes>0) {
                        handler.obtainMessage(MESSAGE_READ,bytes,-1,buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }


    //This opens a sendReceive class as a server
    public class ServerClass extends Thread implements Serializable{
        public Socket socket;
        public ServerSocket serverSocket;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8888);
                socket = serverSocket.accept();
                sendReceive = new SendReceive(socket);

                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //This starts a new socket and sendReceive class as a client
    public class ClientClass extends Thread implements Serializable{
        public Socket socket;
        public String hostAdd;

        public ClientClass(InetAddress hostAddress) {
            hostAdd = hostAddress.getHostAddress();
            socket = new Socket();
        }

        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(hostAdd,8888),500);
                sendReceive= new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //This is the onclick for the two buttons we have.
    @Override
    public void onClick(View view) {

        // Checks if game is over before allowing a click
        if (!game.isGameOver()) {
            myHandler = new Handler();

            game.getPlayerList().get(game.getTurnToMove()).subtractTile();


            int[] coordinates = game.getIJ(view.getId());
            game.editTile(coordinates, game.getTurnToMove());

            //Calling using player constructor.
            gameUpdateClass myCl = new gameUpdateClass(game.getPlayerList().get(game.getTurnToMove()).playerColor,
                    game.getTurnToMove(), game.getPlayerList().get(game.getTurnToMove()).tilesLeft(), coordinates[0], coordinates[1]);
            myHandler.post(myCl);

            //This might because of handler, or the class.

            // Checks if game is over while changing turn
            if(!game.changeTurn()) {
                myCl.updateTurn("Game Over!");
                myCl.updateTiles("Click any square to return to menu.");
                myHandler.post(myCl);
            }

            System.out.println(game.returnState());
        }
        // Returns to menu
        else {
            Intent menuIntent = new Intent(this, Launcher.class);
            startActivity(menuIntent);
        }
        //Need to also change the graphics here
    }

    //Used for listView
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final WifiP2pDevice device = deviceArray[i];
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;

        //Need this check
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Connected to "+device.deviceName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i) {
                Toast.makeText(getApplicationContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
            }
        });
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