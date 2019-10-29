package com.sm.homeautomation.ui.home.adapters;

import android.app.LauncherActivity;
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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sm.homeautomation.R;
import com.sm.homeautomation.room.model.DbaseDevice;
import com.sm.homeautomation.room.model.PendingAddDevice;

import java.util.List;

import timber.log.Timber;

public class DevicesAdapter extends ListAdapter<DbaseDevice,DevicesAdapter.DeviceViewHolder> {

    private static final String TAG = "DevicesAdapter";
    private final OnDevicesAdapterInteractionListener mListener;

    private static final DiffUtil.ItemCallback<DbaseDevice> DIFF_CALLBACK = new DiffUtil.ItemCallback<DbaseDevice>() {
        @Override
        public boolean areItemsTheSame(DbaseDevice oldItem, DbaseDevice newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(DbaseDevice oldItem, DbaseDevice newItem) {
            return oldItem.getDev_id().equals(newItem.getDev_id()) &&
                    oldItem.getMac().equals(newItem.getMac()) &&
                    oldItem.getFirmware_version().equals(newItem.getFirmware_version());
        }
    };



    public DevicesAdapter(OnDevicesAdapterInteractionListener mListener) {
        super(DIFF_CALLBACK);
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
        holder.deviceText.setText(getItem(position).getType());

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
