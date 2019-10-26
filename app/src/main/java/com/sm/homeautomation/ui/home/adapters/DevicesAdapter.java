package com.sm.homeautomation.ui.home.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sm.homeautomation.R;
import com.sm.homeautomation.room.model.DbaseDevice;
import com.sm.homeautomation.room.model.PendingAddDevice;

import java.util.List;

import timber.log.Timber;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder> {

    private static final String TAG = "DevicesAdapter";
    private final OnDevicesAdapterInteractionListener mListener;
    private List<DbaseDevice> mValues;

    public DevicesAdapter(OnDevicesAdapterInteractionListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_view, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, final int position) {
        Timber.i("onBindViewHolder: ");
        holder.deviceText.setText(mValues.get(position).getType());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDevicesAdapterItemClick(position);
            }
        });

        holder.toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Timber.i("onCheckedChange b: %s", b);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setDevices(List<DbaseDevice> dbaseDevices){
        this.mValues = dbaseDevices;
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {
        Switch toggleSwitch;
        // each data item is just a string in this case
        TextView deviceText;
        ImageView deviceImg;

        DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceImg = itemView.findViewById(R.id.device_icon);
            deviceText = itemView.findViewById(R.id.device_name);
            toggleSwitch = itemView.findViewById(R.id.switch_toggle);
        }
    }

    public interface OnDevicesAdapterInteractionListener{
        void onDevicesAdapterItemClick(int position);
    }

}
