package com.sm.homeautomation.room.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPendingAddDevice(PendingAddDevice pendingAddDevice);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePendingAddDevice(PendingAddDevice pendingAddDevice);

    @Query("SELECT * FROM pending_devices")
    LiveData<List<PendingAddDevice>> getAllPendingDevices();


}
