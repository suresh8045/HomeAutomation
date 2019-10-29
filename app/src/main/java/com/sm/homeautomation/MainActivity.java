package com.sm.homeautomation;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.sm.homeautomation.data.model.User;
import com.sm.homeautomation.mqtt.MqttManager;
import com.sm.homeautomation.mqtt.MqttManagerService;
import com.sm.homeautomation.ui.login.LoginActivity;
import com.sm.homeautomation.utils.SessionHandler;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

import java.util.Timer;

import dagger.android.AndroidInjection;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    static final int ADD_DEVICE_REQUEST = 1;  // The request code
    private Intent mqttServiceIntent;
    private boolean mServiceBound;
    private MqttManagerService mBoundService;
    private TextView navUsername;
    private TextView navEmailId;
    private SessionHandler sessionHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidInjection.inject(this);
        Timber.i("onCreate");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
                Intent addDeviceIntent = new Intent(MainActivity.this, AddDeviceActivity.class);
                startActivityForResult(addDeviceIntent, ADD_DEVICE_REQUEST);

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        navUsername = navigationView.getHeaderView(0).findViewById(R.id.nav_username);
        navEmailId = navigationView.getHeaderView(0).findViewById(R.id.nav_emailid);

        sessionHandler = new SessionHandler(this);

        User user = sessionHandler.getUserDetails();
        if(navUsername!=null) {
            Timber.i("navUsername not null");
            navUsername.setText(user.getUsername());
            navEmailId.setText(user.getEmail());
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //startForegroundService
      /*  mqttServiceIntent = new Intent(this, MqttManagerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(mqttServiceIntent);
        }else{
            startService(mqttServiceIntent);
        }*/

      //MQTT
        MqttManager mqttManager = new MqttManager(this);
        mqttManager.initConnection();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                doLogout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doLogout() {
        sessionHandler.logoutUser();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_DEVICE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Timber.i("Result OK, %s",data.getStringExtra("result"));
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }else{
                Timber.i("Result cancelled");
            }
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundService = null;
            mServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MqttManagerService.MqttServiceBinder myBinder = (MqttManagerService.MqttServiceBinder) service;
            mBoundService = myBinder.getService();
            mServiceBound = true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //bindService(mqttServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        Timber.i("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.i("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Timber.i("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.i("onStop");
        //unbindService(mServiceConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.i("onDestroy");
    }


}
