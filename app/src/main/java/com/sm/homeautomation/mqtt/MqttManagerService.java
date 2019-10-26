package com.sm.homeautomation.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import timber.log.Timber;

public class MqttManagerService extends Service {
    public MqttManagerService() {
    }

    @Override
    public void onCreate() {
        Timber.i("onCreate: ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.i("onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        Timber.i("onBind: ");
        return new MqttServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Timber.i("onUnBind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Timber.i("onTaskRemoved: ");
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        Timber.i("onDestroy");
        super.onDestroy();
    }

    public class MqttServiceBinder extends Binder {
        public MqttManagerService getService() {
            return MqttManagerService.this;
        }
    }

}
