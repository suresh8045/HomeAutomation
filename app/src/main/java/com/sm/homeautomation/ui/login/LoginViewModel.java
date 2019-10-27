package com.sm.homeautomation.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.sm.homeautomation.data.LoginRepository;
import com.sm.homeautomation.data.Result;
import com.sm.homeautomation.data.model.LoggedInUser;
import com.sm.homeautomation.R;
import com.sm.homeautomation.room.cloud.LoginResponse;
import com.sm.homeautomation.room.model.DeviceResponse;
import com.sm.homeautomation.room.repository.SMRepository;
import com.sm.homeautomation.room.utils.Resource;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import timber.log.Timber;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MediatorLiveData<LoginResponse> loginResult = new MediatorLiveData<>();
    private LoginRepository loginRepository;
    private LiveData<Resource<LoginResponse>> ld;

    @Inject
    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

//    LiveData<LoginResult> getLoginResult() {
    LiveData<LoginResponse> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Map<String,String> requestMap = new HashMap<>();
        requestMap.put("username", username);
        requestMap.put("password", password);

        ld = loginRepository.login(requestMap);

        loginResult.addSource(ld, new Observer<Resource<LoginResponse>>() {
            @Override
            public void onChanged(Resource<LoginResponse> deviceResponseResource) {
                Timber.i("onChanged:");
                loginResult.setValue(deviceResponseResource.data);
            }
        });



     /*   Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }*/
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
