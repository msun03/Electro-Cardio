package com.electrocardio.michaelsun.electrocardio;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends Activity {

    ArrayAdapter<String> listAdapter;
    Button connectNew;
    ListView listView;
    BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Restore any saved state
        super.onCreate(savedInstanceState);

        // Set the content/layout view
        setContentView(R.layout.activity_main);
        setupUI();

        if (btAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth not detected", 0).show();
            finish();
        }
        else {
            if (!btAdapter.isEnabled()){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 1);
            }
        }
    }

    private void setupUI() {
        connectNew = (Button)findViewById(R.id.connectBluetooth);
    }
}
