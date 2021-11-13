package com.raywenderlich.loginandregister.retro;


import com.raywenderlich.loginandregister.dao.UsersDaoInterface;

public class ApiUtils {

    public static final String baseUrl = "http://94.20.38.107:8003/api/";

    public static UsersDaoInterface getUsersDaoInterface(){
        return RetrofitClient.getClient(baseUrl).create(UsersDaoInterface.class);
    }

}