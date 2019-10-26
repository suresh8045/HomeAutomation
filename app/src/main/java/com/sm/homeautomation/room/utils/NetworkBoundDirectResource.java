package com.sm.homeautomation.room.utils;


import android.util.Log;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;


// CacheObject: Type for the Resource data. (database cache)
// RequestObject: Type for the API response. (network request)
public abstract class NetworkBoundDirectResource<RequestObject> {

    private static final String TAG = "NetworkBoundResource";

    private AppExecutors appExecutors;
    private MediatorLiveData<Resource<RequestObject>> results = new MediatorLiveData<>();

    public NetworkBoundDirectResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        init();
    }

    private void init() {
        Log.d(TAG, "fetchFromNetwork:init called.");
        // update LiveData for loading status
        results.setValue((Resource<RequestObject>) Resource.loading(null));
        fetchFromNetwork();

    }

    /**
     * 1) observe local db
     * 2) if <condition/> query the network
     * 3) stop observing the local db
     * 4) insert new data into local db
     * 5) begin observing local db again to see the refreshed data from network
     */
    private void fetchFromNetwork() {

        Log.d(TAG, "fetchFromNetwork: called.");


        final LiveData<ApiResponse<RequestObject>> apiResponse = createCall();

        results.addSource(apiResponse, new Observer<ApiResponse<RequestObject>>() {
            @Override
            public void onChanged(@Nullable final ApiResponse<RequestObject> requestObjectApiResponse) {
                results.removeSource(apiResponse);

                /*
                    3 cases:
                       1) ApiSuccessResponse
                       2) ApiErrorResponse
                       3) ApiEmptyResponse
                 */

                if (requestObjectApiResponse instanceof ApiResponse.ApiSuccessResponse) {
                    Log.d(TAG, "onChanged: ApiSuccessResponse.");

                    results.setValue(Resource.success(processResponse((ApiResponse.ApiSuccessResponse) requestObjectApiResponse)));
                    appExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            // save the response to the local db
                            saveCallResult((RequestObject) processResponse((ApiResponse.ApiSuccessResponse) requestObjectApiResponse));
                        }
                    });
                } else if (requestObjectApiResponse instanceof ApiResponse.ApiEmptyResponse) {
                    Log.d(TAG, "onChanged: ApiEmptyResponse");
                    results.setValue(Resource.success(processResponse((ApiResponse.ApiSuccessResponse) requestObjectApiResponse)));
                } else if (requestObjectApiResponse instanceof ApiResponse.ApiErrorResponse) {
                    Log.d(TAG, "onChanged: ApiErrorResponse.");
                    Log.i(TAG, "onChanged: ssd"+ requestObjectApiResponse.toString());

                    results.setValue(
                            Resource.error(
                                    ((ApiResponse.ApiErrorResponse) requestObjectApiResponse).getErrorMessage(),
                                    (RequestObject) requestObjectApiResponse
                            )
                    );


                }
            }
        });
    }

    private RequestObject processResponse(ApiResponse.ApiSuccessResponse response) {
        return (RequestObject) response.getBody();
    }

    private void setValue(Resource<RequestObject> newValue) {
        if (results.getValue() != newValue) {
            results.setValue(newValue);
        }
    }

    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject item);


    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestObject>> createCall();

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    public final LiveData<Resource<RequestObject>> getAsLiveData() {
        return results;
    }

    ;
}