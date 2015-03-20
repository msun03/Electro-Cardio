package com.electrocardio.michaelsun.electrocardio;
/* Author: Michael Sun */

import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter myBluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;
    ArrayAdapter<String> BTArrayAdapter;
    BluetoothDevice myDevice;
    Button onButton;
    Button offButton;
    Button pairedDevicesButton;
    Button scanButton;
    ListView devicesList;
    Button heartRateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the User Interface view
        initializeUI();

        //Checks if Bluetooth is supported on the device
        if(myBluetoothAdapter == null){
            Toast.makeText(getApplicationContext(),"Your device does not support Bluetooth",
                    Toast.LENGTH_LONG).show();
            finish();
        } else {
            // Create the arrayAdapter that contains the Bluetooth Devices, and set it to the ListView
            BTArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            devicesList.setAdapter(BTArrayAdapter);

            devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    /*Thread myConnectThread = new ConnectThread(myDevice);
                    myConnectThread.start();*/
                }
            });
        }
    }

    /** Method for initializing the User Interface.*/
    private void initializeUI(){
        onButton = (Button)findViewById(R.id.onButton);
        offButton = (Button)findViewById(R.id.offButton);
        pairedDevicesButton = (Button)findViewById(R.id.pairedDevices);
        scanButton = (Button)findViewById(R.id.scanForDevices);
        devicesList = (ListView)findViewById(R.id.devicesList);
        heartRateButton = (Button)findViewById(R.id.heartRate);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /** Method for turning on Bluetooth. */
    public void turnOnBT(View v) {
        if(!myBluetoothAdapter.isEnabled()){
            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
        } else {
            Toast.makeText(getApplicationContext(),"Bluetooth is already on",
                    Toast.LENGTH_LONG).show();
        }
    }

    /** Method for turning off Bluetooth. */
    public void turnOffBT(View v){
        myBluetoothAdapter.disable();
        Toast.makeText(getApplicationContext(),"Bluetooth turned off!",
                Toast.LENGTH_LONG).show();
    }

    /** Method for Scanning nearby Bluetooth devices. */
    public void scanForDevices(View v){

        if(!myBluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(),"Bluetooth needs to be enabled to Scan!",
                    Toast.LENGTH_LONG).show();
        }
        if (myBluetoothAdapter.isDiscovering()){
            myBluetoothAdapter.cancelDiscovery();
        } else {
            Toast.makeText(getApplicationContext(),"Scanning...",
                    Toast.LENGTH_SHORT).show();

            BTArrayAdapter.clear();
            myBluetoothAdapter.startDiscovery();

            registerReceiver(bReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        }
    }

    /** Method for showing currently paired devices in the list. */
    public void showPairedDevices(View v){
        pairedDevices = myBluetoothAdapter.getBondedDevices();
        BTArrayAdapter.clear();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                BTArrayAdapter.add(device.getName() + " (Paired)" + "\n" + device.getAddress());
                myDevice = device;
            }

            Toast.makeText(getApplicationContext(), "Showing paired devices",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(requestCode == REQUEST_ENABLE_BT){
            if(myBluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(),"Bluetooth turned on",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),"Bluetooth turned off",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and the MAC address of the object to the arrayAdapter
                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                BTArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    /*private class ConnectThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }
        public void run() {
            myBluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }*/

    /** Method for switching to the Heart Rate Activity. */
    public void toHeartRateActivity(View v) {
        Intent heartRateIntent = new Intent(this, HeartRateActivity.class);
        startActivity(heartRateIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bReceiver);
    }
}