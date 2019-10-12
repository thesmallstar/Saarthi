package com.example.saarthi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    EditText phone,password;
    private String server;
    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        phone = (EditText)findViewById(R.id.logPhone);
        password = (EditText)findViewById(R.id.logPass);
        Button loginButton = (Button) findViewById(R.id.logButton);
        test = (TextView)findViewById(R.id.test);
        server =   getResources().getString(R.string.server);;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkAndLogin();
            }
        });

    }
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    void checkAndLogin() {

        if (isEmpty(phone)) {
            Toast t = Toast.makeText(this, "You must enter Phone register!", Toast.LENGTH_SHORT);
            t.show();
            return;
        }

        if (isEmpty(password)) {
            password.setError("You must enter password to register");
            return;
        }

        //fortestingonlyyy
       if(phone.getText().toString().equals("007")){
           Hawk.init(getApplicationContext()).build();
           Hawk.put("token","007");
           checkIfUserloggedin();
           return;
       }

        tryLogin();

    }

    private void checkIfUserloggedin(){
        Hawk.init(getApplicationContext()).build();
        if(Hawk.contains("token")){
            Intent intent = new Intent(this, home.class);

            startActivity(intent);
            finishAffinity();

        }


    }

    void tryLogin(){

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
     //   String rname = name.getText().toString();
        String rphone = phone.getText().toString();
        String rpass = password.getText().toString();
        String url = server + "auth/login";


        RequestBody body = new FormBody.Builder()
                .add("password", rpass)
                .add("phone", rphone)
                .build();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Toast.makeText(getApplicationContext(), "Trying to Login", Toast.LENGTH_LONG).show();
        //Call call = client.newCall(request);

        client.newCall(request).enqueue(new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                runOnUiThread(new Runnable(){


                    @Override
                    public void run() {
                         Login.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    carryLoginAhead(response.body().string());
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
private void carryLoginAhead(String r) throws JSONException {

    JSONObject ress = new JSONObject(r);

    final String tokenn = (String) ress.get("token").toString();
    final String auth = (String) ress.get("auth").toString();
    if(auth.equals("true")){


        Hawk.init(getApplicationContext()).build();
        Hawk.put("token",tokenn);
        checkIfUserloggedin();



    }else{
        test.setText("Incorrect Login Details");
    }


}

}
