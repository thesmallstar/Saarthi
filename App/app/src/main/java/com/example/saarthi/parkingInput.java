package com.example.saarthi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class parkingInput extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
        {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_input);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Button b =(Button) findViewById(R.id.parkingSubmit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerparking();
            }
        });
        googleApiClient.connect();
        super.onStart();
    }
    EditText  name;
    EditText  price;
    EditText  phone;
    EditText  timing;
    EditText  ins ;
    Switch loc;
    CheckBox heavy;
 String server;
    public void registerparking(){
          name = (EditText)findViewById(R.id.inputparkingname);
          price = (EditText)findViewById(R.id.inputparkingprice);
          phone = (EditText)findViewById(R.id.inputparkingnumber);
          timing = (EditText)findViewById(R.id.inputparrkingtime);
          ins = (EditText)findViewById(R.id.inputparkingins);
          loc = (Switch) findViewById(R.id.inputparkingswitch);
          heavy = (CheckBox) findViewById(R.id.inputparklingheavy);
        server =  getResources().getString(R.string.server);;
          registerRequestToServer();





    }
    private GoogleApiClient googleApiClient;
    private Double longitude;
    private Double latitude;
    public void registerRequestToServer(){

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        longitude=0.0;
        latitude=0.0;
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        String url = server + "parking";
        String check="false";
        if(heavy.isChecked()){
            check="true";
        }
        RequestBody body = new FormBody.Builder()
                .add("name", name.getText().toString())
                .add("price", price.getText().toString())
                .add("phone", phone.getText().toString())
                .add("timing",timing.getText().toString())
                .add("ins",ins.getText().toString())
                .add("heavy",check)
                .add("lat",latitude.toString())
                .add("lang",longitude.toString())
                .build();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("x-access-token",  Hawk.get("token").toString())
                .build();

        //Call call = client.newCall(request);

        client.newCall(request).enqueue(new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                runOnUiThread(new Runnable(){


                    @Override
                    public void run() {
                        if (response.isSuccessful()) {

                            parkingInput.this.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(getApplicationContext(), response.body().string(), Toast.LENGTH_LONG).show();

                                        finish();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });





    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
