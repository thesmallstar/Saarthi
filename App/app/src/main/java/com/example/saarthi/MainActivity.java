package com.example.saarthi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;





public class MainActivity extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        checkIfUserloggedin();





    }



    private void checkIfUserloggedin(){
        Hawk.init(getApplicationContext()).build();
        if(Hawk.contains("token")){
            Intent intent = new Intent(this, home.class);
            startActivity(intent);
            finish();

        }


    }

    public void sendReg(View v) {

        Intent intent = new Intent(this, Register.class);
        startActivity(intent);

    }
    public void sendLogin(View v) {

        Intent intent = new Intent(this, Login.class);

        startActivity(intent);

    }

}

