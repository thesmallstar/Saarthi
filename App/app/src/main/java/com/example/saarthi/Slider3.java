package com.example.saarthi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Slider3 extends AppCompatActivity {

    public Button button1;
    public  void init(){

        button1 = (Button)findViewById(R.id.button3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent next3 = new Intent(Slider3.this,MainActivity.class);
                startActivity(next3);
            }
        });

      Button button2 = (Button) findViewById(R.id.skipbutton4);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent next = new Intent(Slider3.this,MainActivity.class);
                startActivity(next);
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.slider_image3);
        init();
    }
}
