package com.example.saarthi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {


    //used to test only :)
    private TextView test;
    private String server;
   private EditText name,phone,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();
        test = (TextView) findViewById(R.id.test);
        server =   getResources().getString(R.string.server);;
        name = findViewById(R.id.regName);
        phone = findViewById(R.id.regPhone);
        password = findViewById(R.id.regPass);
        Button register = (Button) findViewById(R.id.regButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRegister();
            }
        });
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    void checkAndRegister() {
        if (isEmpty(name)) {
            Toast t = Toast.makeText(this, "You must enter first name to register!", Toast.LENGTH_SHORT);
            t.show();
            return;
        }

        if (isEmpty(phone)) {
            Toast t = Toast.makeText(this, "You must enter Phone register!", Toast.LENGTH_SHORT);
            t.show();
            return;
        }

        if (isEmpty(password)) {
            password.setError("You must enter password to register");
            return;
        }


        registerVerified();





    }


    private void testApi() throws IOException {


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
               // .header("'x-access-token", "fgfgfg")
                .url(server)
                .build();


        client.newCall(request).enqueue(new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if (response.isSuccessful()) {

                    final String myres =  response.body().string();
                    Register.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                test.setText(myres);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
           e.printStackTrace();
            }
        });

    }



    void registerVerified(){

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String rname = name.getText().toString();
        String rphone = phone.getText().toString();
        String rpass = password.getText().toString();
        String url = server + "auth/register";
        RequestBody body = new FormBody.Builder()
                .add("firstName", rname)
                .add("password", rpass)
                .add("phone", rphone)
                .build();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        //Call call = client.newCall(request);

        client.newCall(request).enqueue(new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                  runOnUiThread(new Runnable(){


                      @Override
                      public void run() {
                          if (response.isSuccessful()) {

                              Register.this.runOnUiThread(new Runnable() {

                                  @Override
                                  public void run() {
                                      try {
                                          handleRegister(response.body().string());
                                      } catch (IOException e) {
                                          e.printStackTrace();
                                      } catch (JSONException e) {
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




void handleRegister(String r) throws JSONException {
    JSONObject ress = new JSONObject(r);

    final String tokenn = (String) ress.get("token").toString();
    final String auth = (String) ress.get("auth").toString();
    if(auth.equals("true")){


        Hawk.init(getApplicationContext()).build();
        Hawk.put("token",tokenn);
        //String s = Hawk.get("token");
        test.setText("Sucessfully Registered");
        checkIfUserloggedin();



    }else{
        test.setText("Could not register");
    }



}

    private void checkIfUserloggedin(){
        Hawk.init(getApplicationContext()).build();
        if(Hawk.contains("token")){
            Intent intent = new Intent(this, home.class);

            startActivity(intent);
            finishAffinity();

        }


    }

  private void runInBackround(){
        server =  getString(R.string.server)+"auth/register";
        Toast.makeText(getApplicationContext(), server, Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //method containing process logic.
                makeNetworkRequest_depricated(server + "auth/register");
            }
        }).start();
    }

    private void makeNetworkRequest_depricated(String reqUrl) {

        OkHttpClient httpClient = new OkHttpClient();
        String responseString = "";
        String rname = name.getText().toString();
        String rphone = phone.getText().toString();
        String rpass = password.getText().toString();

        try{
            RequestBody body = new FormBody.Builder()
                    .add("firstName", rname)
                    .add("password", rpass)
                    .add("phone", rphone)
                    .build();

            Request request = new Request.Builder()
                    .url(reqUrl)
                    .post(body)
                    .build();

            Response response = httpClient
                    .newCall(request)
                    .execute();
            Log.d("0", "makeNetworkRequest: ");
            responseString =  response.body().string();
            response.body().close();


            // Response node is JSON Object
            JSONObject ress = new JSONObject(responseString);
           final String tokenn = (String) ress.get("token");
           final String auth = (String) ress.get("auth");
            //final String auth = res.getJSONArray("added").getJSONObject(0).getString("response");


            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    if(auth.equals("true")){
                        //display in short period of time
                        test.setText("herse");
                      //  Toast.makeText(getApplicationContext(), "Booking Successful", Toast.LENGTH_LONG).show();
                    }else{
                        //display in short period of time
                        test.setText("here");
                       // Toast.makeText(getApplicationContext(), "Booking Not Successful", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (MalformedURLException e) {
            //Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            //Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            //Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            //Log.e(TAG, "Exception: " + e.getMessage());
        }

    }

}
