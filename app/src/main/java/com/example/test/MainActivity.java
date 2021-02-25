package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button Authenticate;
    TextView Username;
    TextView Password;
    TextView Result;

    private String readStream(InputStream is) {
        try {
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                int i = is.read();
                while(i != -1) {
                    bo.write(i);
                    i = is.read();
                }
                return bo.toString();
            }
            catch (IOException e) {
                return "";
            }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Authenticate = findViewById(R.id.Authenticate);
        Username = findViewById(R.id.editTextTextLogin);
        Password = findViewById(R.id.editTextTextPassword);
        Result = findViewById(R.id.textViewResult);

        Authenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable myRunnable =
                        new Runnable(){
                            public void run(){
                                URL url = null;
                                try {
                                    url = new URL("https://httpbin.org/basic-auth/bob/sympa");
                                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                                    try {

                                        String basicAuth = "Basic " + Base64.encodeToString((Username.getText()+":"+Password.getText()).getBytes(),
                                                Base64.NO_WRAP);
                                        urlConnection.setRequestProperty ("Authorization", basicAuth);

                                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                                        String s = readStream(in);
                                        Log.i("JFL", s);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Result.setText("My Result Here");
                                            }
                                        });

                                    } finally {
                                        urlConnection.disconnect();
                                    }
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                Thread thread = new Thread(myRunnable);
                thread.start();
            }
        });


    }

}

