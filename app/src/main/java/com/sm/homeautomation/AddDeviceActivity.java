package com.sm.homeautomation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.sm.homeautomation.activityviewmodels.AddDeviceActivityViewModel;
import com.sm.homeautomation.di.ViewModelFactory;
import com.sm.homeautomation.room.model.DeviceResponse;
import com.sm.homeautomation.room.utils.Resource;
import com.sm.homeautomation.utils.Misc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

public class AddDeviceActivity extends AppCompatActivity {

   // @Inject
 //   DeviceApi deviceApi;
  //  @Inject
   // SMRepository smRepository;
    private TextView openWifiSettingsBtn;
    private Button proceedBtn;
    private Button backBtn;
    private ViewFlipper vf;
    private TextView connectedSSID;
    private ProgressBar progressBar;
    private BroadcastReceiver broadcastReceiver;
    private Button addDeviceBtn;

    @Inject
    ViewModelFactory providerFactory;
    private AddDeviceActivityViewModel addDeviceActivityViewModel;
    private TextView devId;
    private TextView devModel;
    private TextView devType;
    private TextView ssidInput;
    private TextView passwordWifi;
    private ProgressBar deviceConfiguringProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        AndroidInjection.inject(this);
        addDeviceActivityViewModel = ViewModelProviders.of(this, providerFactory).get(AddDeviceActivityViewModel.class);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.progressBar_connection);
        progressBar.setVisibility(View.INVISIBLE);
        vf = findViewById( R.id.viewFlipper);
        connectedSSID = findViewById(R.id.ssid_name);
        openWifiSettingsBtn = findViewById(R.id.open_wifi_settings);

        devId = findViewById(R.id.dev_id_v);
        devType = findViewById(R.id.dev_type_v);
        devModel = findViewById(R.id.dev_model_v);
        ssidInput = findViewById(R.id.ssid_v);
        passwordWifi = findViewById(R.id.password_v);

        deviceConfiguringProgressBar = findViewById(R.id.progressBar_connection);
        deviceConfiguringProgressBar.setVisibility(View.INVISIBLE);

        openWifiSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWifiSettings();
            }
        });

        proceedBtn = findViewById(R.id.proceed);
        //proceedBtn.setEnabled(false);
        //proceedBtn.setActivated(false);
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vf.setInAnimation(AddDeviceActivity.this, R.anim.in_from_right);
                vf.setOutAnimation(AddDeviceActivity.this, R.anim.out_to_left);
                vf.showNext();
                addDeviceActivityViewModel.setFlipperPosition(vf.getDisplayedChild());
                callGetInfo();
            }
        });


        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vf.setInAnimation(AddDeviceActivity.this, R.anim.in_from_left);
                vf.setOutAnimation(AddDeviceActivity.this, R.anim.out_to_right);
                vf.showPrevious();
                addDeviceActivityViewModel.setFlipperPosition(vf.getDisplayedChild());
            }
        });

        addDeviceBtn = findViewById(R.id.add_device_btn);
        addDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timber.i("onClick");
                if(configure()) {
                    vf.setInAnimation(AddDeviceActivity.this, R.anim.in_from_right);
                    vf.setOutAnimation(AddDeviceActivity.this, R.anim.out_to_left);
                    vf.showNext();
                    addDeviceActivityViewModel.setFlipperPosition(vf.getDisplayedChild());
                    deviceConfiguringProgressBar.setVisibility(View.VISIBLE);
                }

            }
        });


        initVflipper();

        initWifiBroadCastListener();

    }

    private void initVflipper() {
        vf.setDisplayedChild(addDeviceActivityViewModel.getFlipperPosition());
    }

    private void initWifiBroadCastListener() {
        Timber.i("initWifiBroadCastListener");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION) || action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                    if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
                        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                        Timber.i("info : %s", info.isConnected());
                        if(info.isConnected()){
                            if(Misc.getConnectedWifiSSID(AddDeviceActivity.this).contains(getString(R.string.softap_key))){
                                connectedSSID.setText(Misc.getConnectedWifiSSID(AddDeviceActivity.this));
                                progressBar.setVisibility(View.INVISIBLE);
                                proceedBtn.setEnabled(true);
                                proceedBtn.setActivated(true);
                            }
                            else{
                                connectedSSID.setText("");
                                progressBar.setVisibility(View.VISIBLE);
                                proceedBtn.setEnabled(false);
                                proceedBtn.setActivated(false);
                            }
                        }else{
                            connectedSSID.setText("");
                            progressBar.setVisibility(View.VISIBLE);
                            proceedBtn.setEnabled(false);
                            proceedBtn.setActivated(false);
                        }
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.EXTRA_NETWORK_INFO);
        registerReceiver(broadcastReceiver, intentFilter);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void setActivityResult(String result){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",result);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }


    private boolean configure() {
        String ssid = ssidInput.getText().toString();
        String passwd = passwordWifi.getText().toString();
        if(ssid.equals("")
                ||passwd.equals("")){
            Toast.makeText(this, "Please provide ssid password", Toast.LENGTH_SHORT).show();
            return false;
        }

        Map<String, String> data = new HashMap<>();
        data.put("ssid", ssid);
        data.put("pwd", passwd);
        data.put("ipmode", "0");

        addDeviceActivityViewModel.configure(data).removeObservers(this);
        addDeviceActivityViewModel.configure(data).observe(this, new Observer<Resource<DeviceResponse>>() {
            @Override
            public void onChanged(Resource<DeviceResponse> deviceResponseResource) {
                if(deviceResponseResource!=null && deviceResponseResource.status == Resource.Status.SUCCESS){
                    Timber.i("getInfo: %s"
                            ,deviceResponseResource.data.toString());
                    callGetConnectStatus();
                }
            }
        });
        return true;
    }

    private void callGetConnectStatus(){
        addDeviceActivityViewModel.getConnectStatus().removeObservers(this);
        Timber.i("callGetConnectStatus: ");
        addDeviceActivityViewModel.getConnectStatus().observe(this, new Observer<Resource<DeviceResponse>>() {
            @Override
            public void onChanged(Resource<DeviceResponse> deviceResponseResource) {
                if(deviceResponseResource!=null && deviceResponseResource.status == Resource.Status.SUCCESS){
                    Timber.i("getConnectStatus: %s"
                            ,deviceResponseResource.data.toString());
                    if(deviceResponseResource.data!=null && deviceResponseResource.data.getMsg().equals("connected")){
                        closeDeviceHotspot();
                    }else{
                        callGetConnectStatus();
                    }

                }
            }
        });
    }

    private void closeDeviceHotspot(){
        addDeviceActivityViewModel.closeDeviceHotspot().removeObservers(this);
        Timber.i("callGetConnectStatus: ");
        addDeviceActivityViewModel.closeDeviceHotspot().observe(this, new Observer<Resource<DeviceResponse>>() {
            @Override
            public void onChanged(Resource<DeviceResponse> deviceResponseResource) {
                if(deviceResponseResource!=null && deviceResponseResource.status == Resource.Status.SUCCESS){
                    Timber.i("getConnectStatus: %s"
                            ,deviceResponseResource.data.toString());
                    if(deviceResponseResource.data!=null && deviceResponseResource.data.getMsg().equals("CONFIG_SUCCESS_SOFTAP_CLOSING")){
                        deviceConfiguringProgressBar.setVisibility(View.INVISIBLE);
                        setActivityResult("device configured success");
                    }

                }
            }
        });
    }

    private void callGetInfo(){
        addDeviceActivityViewModel.getDeviceInfo().removeObservers(this);
        Timber.i("callGetInfo: ");
        addDeviceActivityViewModel.getDeviceInfo().observe(this, new Observer<Resource<DeviceResponse>>() {
            @Override
            public void onChanged(Resource<DeviceResponse> deviceResponseResource) {
                if(deviceResponseResource!=null && deviceResponseResource.status == Resource.Status.SUCCESS){
                    Timber.i("getInfo: %s"
                    ,deviceResponseResource.data.toString());
                    devId.setText(deviceResponseResource.data.getResp().getId());
                    devType.setText(deviceResponseResource.data.getResp().getType());
                    devModel.setText(deviceResponseResource.data.getResp().getMdl());
                }
            }
        });
    }

    private void callApi(){
       /* if(smRepository!=null) {
            Timber.i("smrepo not null");

            smRepository.getConnectStatus().observe(this, new Observer<Resource<DeviceResponse>>() {
                @Override
                public void onChanged(Resource<DeviceResponse> deviceResponseResource) {
                    Timber.i("hello");
                }
            });
        }*/
        /*deviceApi.getDeviceInfo().enqueue(new Callback<DeviceResponse>() {
            @Override
            public void onResponse(Call<DeviceResponse> call, Response<DeviceResponse> response) {
                Timber.i("onResponse");

            }

            @Override
            public void onFailure(Call<DeviceResponse> call, Throwable t) {
                Timber.i("onFailure");
            }
        });*/
    }




    private void openWifiSettings(){
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
        intent.setComponent(cn);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity( intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
