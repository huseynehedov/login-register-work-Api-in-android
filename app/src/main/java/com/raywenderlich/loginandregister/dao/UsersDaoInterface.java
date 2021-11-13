package com.raywenderlich.loginandregister.dao;

import com.raywenderlich.loginandregister.model.MyPlacesResponse;
import com.raywenderlich.loginandregister.model.PlaceRequest;
import com.raywenderlich.loginandregister.model.PlaceResponse;
import com.raywenderlich.loginandregister.model.LoginRequest;
import com.raywenderlich.loginandregister.model.LoginResponse;
import com.raywenderlich.loginandregister.model.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsersDaoInterface {

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("register")
    Call<LoginResponse> register(@Body RegisterRequest request);

    @POST("add-location")
    Call<PlaceResponse> addLocation(@Header("Authorization") String token, @Body PlaceRequest request);

    @POST("get-user-location")
    Call<MyPlacesResponse> getUserLocation(@Header("Authorization") String token);
}
