package com.electrocardio.michaelsun.electrocardio;
/* Author: Michael Sun */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.widget.ArrayAdapter;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter myBluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;
    ArrayAdapter<String> BTArrayAdapter;
    Button onButton;
    Button offButton;
    Button pairedDevicesButton;
    Button scanButton;
    ListView devicesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        if(myBluetoothAdapter == null){
            Toast.makeText(getApplicationContext(),"Your device does not support Bluetooth",
                    Toast.LENGTH_LONG).show();
            finish();
        } else {
            onButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    turnOnBT();
                }
            });

            offButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v){
                    turnOffBT();
                }
            });

            pairedDevicesButton.setOnClickListener(new OnClickListener(){
                public void onClick(View v){
                    list(v);
                }
            });

            scanButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v){
                    scan(v);
                }
            });

            // create the arrayAdapter that contains the BTDevices, and set it to the ListView
            BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            devicesList.setAdapter(BTArrayAdapter);
        }
    }

    private void initialize(){
        onButton = (Button)findViewById(R.id.onButton);
        offButton = (Button)findViewById(R.id.offButton);
        pairedDevicesButton = (Button)findViewById(R.id.pairedDevices);
        scanButton = (Button)findViewById(R.id.scan);
        devicesList = (ListView)findViewById(R.id.devicesList);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private void turnOnBT() {
        if(!myBluetoothAdapter.isEnabled()){
            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
        } else {
            Toast.makeText(getApplicationContext(),"Bluetooth is already on",
                    Toast.LENGTH_LONG).show();
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

    public void list(View view){
        pairedDevices = myBluetoothAdapter.getBondedDevices();
        BTArrayAdapter.clear();

        for (BluetoothDevice device : pairedDevices)
            BTArrayAdapter.add(device.getName()+ " (Paired)" + "\n" + device.getAddress());

        Toast.makeText(getApplicationContext(), "Showing paired devices",
                Toast.LENGTH_SHORT).show();

    }

    final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name and the MAC address of the object to the arrayAdapter
                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                BTArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    public void scan(View view){

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

    private void turnOffBT(){
        myBluetoothAdapter.disable();
        Toast.makeText(getApplicationContext(),"Bluetooth turned off!",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bReceiver);
    }
}