package com.example.saarthi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class displayParkikng extends AppCompatActivity {


    String server;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_parkikng);
        String id = getIntent().getStringExtra("id");
//        Toast.makeText(this,
//                id,
//                Toast.LENGTH_SHORT).show();
        server = getResources().getString(R.string.server);
        addParkingSpotstoMap(id);
    }

    private void  addParkingSpotstoMap(String id){


        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        //   String rname = name.getText().toString();

        String url = server + "parking/"+id;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        Toast.makeText(getApplicationContext(), "Loading Parking Spots nearby", Toast.LENGTH_LONG).show();
        //Call call = client.newCall(request);

        client.newCall(request).enqueue(new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                runOnUiThread(new Runnable(){


                    @Override
                    public void run() {
                        displayParkikng.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    displayTheDetails(response.body().string());
                                } catch (IOException e) {

                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                e.printStackTrace();
            }
        });






    }

    void displayTheDetails(String s) throws JSONException {

        //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        JSONObject ress = new JSONObject(s);
        TextView name = (TextView)findViewById(R.id.name);
        TextView timing = (TextView)findViewById(R.id.timing);
        TextView price = (TextView)findViewById(R.id.price);
        TextView ins = (TextView)findViewById(R.id.ins);
        name.setText(ress.get("name").toString());
        timing.setText(ress.get("timing").toString());
        price.setText(ress.get("price").toString());
        ins.setText(ress.get("ins").toString());
        TextView heavy = (TextView)findViewById(R.id.heavyvehicle);
         if(ress.get("heavy").toString().equals("true")){
              heavy.setText("Allowed");
         }
        else{
             heavy.setText("Not Allowed");

         }

    }
}
