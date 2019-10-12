package com.example.saarthi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.orhanobut.hawk.Hawk;

public class Slider extends AppCompatActivity {

    public Button button1;
    public  void init(){
        checkIfUserloggedin();
        button1 = (Button)findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent next = new Intent(Slider.this,Slider2.class);
                startActivity(next);
            }
        });
      Button button2 = (Button)findViewById(R.id.skipbutton3);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent next = new Intent(Slider.this,MainActivity.class);
                startActivity(next);
            }
        });

    }


    private void checkIfUserloggedin(){
        Hawk.init(getApplicationContext()).build();
        if(Hawk.contains("token")){
            Intent intent = new Intent(this, home.class);
            startActivity(intent);
            finish();

        }


    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.slider_image1);

        init();
    }
}
