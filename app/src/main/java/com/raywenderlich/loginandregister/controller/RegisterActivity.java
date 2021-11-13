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
import com.raywenderlich.loginandregister.model.LoginResponse;
import com.raywenderlich.loginandregister.model.RegisterRequest;
import com.raywenderlich.loginandregister.repo.NetworkRepo;

public class RegisterActivity extends AppCompatActivity {
    private EditText email, password, name;
    private Button buttonLogin, buttonRegister;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.editTextName);
        password = findViewById(R.id.editTextPassword);
        email = findViewById(R.id.editTextEmail);
        buttonLogin = findViewById(R.id.buttonRegisterLogin);
        buttonRegister = findViewById(R.id.buttonRegisterLogin);
        sharedPreferences = getSharedPreferences("UsersInfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void buttonLoginClick(View view){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void buttonRegisterClick(View view){
        String checkUsername = email.getText().toString();
        String checkName = name.getText().toString();
        String checkPassword = password.getText().toString();
        RegisterRequest request = new RegisterRequest();
        request.setName(checkName);
        request.setEmail(checkUsername);
        request.setPassword(checkPassword);

        NetworkRepo.getInstance().register(request).observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                System.out.println(loginResponse);
                System.out.println(loginResponse.getError());

                if (loginResponse.getError() == null){
                    Toast.makeText(RegisterActivity.this, "Registered...", Toast.LENGTH_SHORT).show();
                    System.out.println(loginResponse.getSuccess().getToken());
                    editor.putString("user",loginResponse.getSuccess().getToken());
                    editor.commit();
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(intent);
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
                    Toast.makeText(RegisterActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}