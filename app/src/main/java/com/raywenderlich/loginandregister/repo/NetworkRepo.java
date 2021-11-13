package com.raywenderlich.loginandregister.repo;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.raywenderlich.loginandregister.model.MyPlacesResponse;
import com.raywenderlich.loginandregister.model.PlaceRequest;
import com.raywenderlich.loginandregister.model.PlaceResponse;
import com.raywenderlich.loginandregister.model.RegisterRequest;
import com.raywenderlich.loginandregister.retro.ApiUtils;
import com.raywenderlich.loginandregister.model.LoginRequest;
import com.raywenderlich.loginandregister.model.LoginResponse;
import com.raywenderlich.loginandregister.dao.UsersDaoInterface;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkRepo {
    private static NetworkRepo networkRepo;

    public static NetworkRepo getInstance(){
        if(networkRepo == null){
            networkRepo = new NetworkRepo();
        }
        return networkRepo;
    }

    private UsersDaoInterface usersDaoInterface;

    public NetworkRepo(){
        usersDaoInterface = ApiUtils.getUsersDaoInterface();
    }

    public LiveData<LoginResponse> login(LoginRequest request ){
        MutableLiveData<LoginResponse> loginData = new MutableLiveData<>();
        usersDaoInterface.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.body() == null){
                    try {
                        loginData.postValue(new Gson().fromJson(response.errorBody().string(), LoginResponse.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    loginData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
                loginData.postValue(null);
            }
        });
        return loginData;
    }

    public LiveData<LoginResponse> register(RegisterRequest request){
        MutableLiveData<LoginResponse> loginData = new MutableLiveData<>();
        usersDaoInterface.register(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.body() == null){
                    try {
                        loginData.postValue(new Gson().fromJson(response.errorBody().string(),LoginResponse.class));
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }else{
                    loginData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
                loginData.postValue(null);
            }
        });
        return loginData;
    }

    public  LiveData<PlaceResponse> addLocation(String token, PlaceRequest request){
        MutableLiveData<PlaceResponse> locationData = new MutableLiveData<>();
        usersDaoInterface.addLocation(token, request).enqueue(new Callback<PlaceResponse>() {
            @Override
            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {

                if(response.body() == null){
                    try {
                        locationData.postValue(new Gson().fromJson(response.errorBody().string(), PlaceResponse.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    locationData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<PlaceResponse> call, Throwable t) {
                t.printStackTrace();
                locationData.postValue(null);
            }
        });
        return locationData;
    }

    public LiveData<MyPlacesResponse> getUserLocation(String token){
        MutableLiveData<MyPlacesResponse> placeData = new MutableLiveData<>();
        usersDaoInterface.getUserLocation(token).enqueue(new Callback<MyPlacesResponse>() {
            @Override
            public void onResponse(Call<MyPlacesResponse> call, Response<MyPlacesResponse> response) {
                if(response.body() == null){
                    try {
                        placeData.postValue(new Gson().fromJson(response.errorBody().string(), MyPlacesResponse.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    placeData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MyPlacesResponse> call, Throwable t) {
                t.printStackTrace();
                placeData.postValue(null);
            }
        });
        return placeData;
    }

}
