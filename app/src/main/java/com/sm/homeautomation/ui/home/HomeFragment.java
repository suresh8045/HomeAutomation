package com.sm.homeautomation.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sm.homeautomation.R;
import com.sm.homeautomation.room.model.DbaseDevice;
import com.sm.homeautomation.di.ViewModelFactory;
import com.sm.homeautomation.room.utils.Resource;
import com.sm.homeautomation.ui.home.adapters.DevicesAdapter;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

import static com.sm.homeautomation.room.utils.Resource.Status.LOADING;
import static com.sm.homeautomation.room.utils.Resource.Status.SUCCESS;

public class HomeFragment extends Fragment implements DevicesAdapter.OnDevicesAdapterInteractionListener {

    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private RecyclerView deviceRecyclerView;
    private LinearLayoutManager layoutManager;

    @Inject
    ViewModelFactory providerFactory;
    private DevicesAdapter devicesAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        homeViewModel = ViewModelProviders.of(this, providerFactory).get(HomeViewModel.class);
        Timber.i("onCreateView");
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
          //      textView.setText(s);
            }
        });

        deviceRecyclerView = root.findViewById(R.id.device_recycler_view);
        deviceRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        deviceRecyclerView.setLayoutManager(layoutManager);
        devicesAdapter = new DevicesAdapter(this);
        deviceRecyclerView.setAdapter(devicesAdapter);

        initObservers();
        return root;
    }

    private void initObservers() {
        homeViewModel.getDevices().removeObservers(getViewLifecycleOwner());
        homeViewModel.getDevices().observe(getViewLifecycleOwner(), new Observer<Resource<List<DbaseDevice>>>() {
            @Override
            public void onChanged(Resource<List<DbaseDevice>> listResource) {
                Timber.i("onChanged:");
                if(listResource.status == LOADING){

                }
                if(listResource.status == SUCCESS){
                    if(listResource.data!=null){
                        initDeviceAdapter(listResource.data);
                    }
                }
            }
        });
    }

    public void initDeviceAdapter(List<DbaseDevice> dbaseDevices){
        devicesAdapter.submitList(dbaseDevices);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDevicesAdapterItemClick(int position) {

    }
}