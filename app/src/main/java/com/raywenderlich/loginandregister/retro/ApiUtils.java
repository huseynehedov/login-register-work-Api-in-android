package com.raywenderlich.loginandregister.retro;


import com.raywenderlich.loginandregister.dao.UsersDaoInterface;

public class ApiUtils {

    public static final String baseUrl = "";

    public static UsersDaoInterface getUsersDaoInterface(){
        return RetrofitClient.getClient(baseUrl).create(UsersDaoInterface.class);
    }

}
