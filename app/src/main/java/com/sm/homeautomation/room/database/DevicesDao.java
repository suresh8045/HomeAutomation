package com.sm.homeautomation.room.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sm.homeautomation.room.model.DbaseDevice;
import com.sm.homeautomation.room.model.PendingAddDevice;

import java.util.List;

import javax.inject.Inject;

@Dao
public interface DevicesDao {

    @Query("SELECT * FROM devices")
    LiveData<List<DbaseDevice>> getAllDevices();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDbaseDevice(List<DbaseDevice> dbaseDevices);

    @Query("SELECT * FROM pending_devices")
    LiveData<List<PendingAddDevice>> getAllPendingDevices();


}
