package com.raywenderlich.loginandregister.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.raywenderlich.loginandregister.R;
import com.raywenderlich.loginandregister.dao.UsersDaoInterface;
import com.raywenderlich.loginandregister.model.LoginRequest;
import com.raywenderlich.loginandregister.model.LoginResponse;
import com.raywenderlich.loginandregister.repo.NetworkRepo;
import com.raywenderlich.loginandregister.retro.ApiUtils;

public class MainActivity extends AppCompatActivity {
    private EditText username, password;
    private Button buttonLogin, buttonRegister;
    private UsersDaoInterface usersDaoInterface;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String checkUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.editTextName);
        password = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonRegisterLogin);
        buttonRegister = findViewById(R.id.buttonRegisterLogin);

        sharedPreferences = getSharedPreferences("UsersInfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        checkUser = sharedPreferences.getString("user",null);

        if(checkUser != null ){
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }

        usersDaoInterface = ApiUtils.getUsersDaoInterface();
    }

    public void buttonLoginClick(View view){
        String checkUsername = username.getText().toString();
        String checkPassword = password.getText().toString();
        LoginRequest request = new LoginRequest(checkUsername, checkPassword);
        NetworkRepo.getInstance().login(request).observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                System.out.println(loginResponse);
                System.out.println(loginResponse.getError());
                if(loginResponse.getError() == null){
                    Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                    System.out.println(loginResponse.getSuccess().getToken());
                    editor.putString("user",loginResponse.getSuccess().getToken());
                    editor.commit();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }else{
                    StringBuilder sb = new StringBuilder();

                    if(loginResponse.getError().getEmail() != null){
                        for(String s: loginResponse.getError().getEmail()){
                            sb.append(s + '\n');
                        }
                    }
                    if(loginResponse.getError().getPassword() != null){
                        for(String s: loginResponse.getError().getPassword()){
                            sb.append(s + '\n');
                        }
                    }
                    Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void buttonRegisterClick(View view){
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

}