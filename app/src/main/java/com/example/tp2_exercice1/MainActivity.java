package com.example.tp2_exercice1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button btnAnth;
    EditText login;
    EditText password;
    TextView textview;
    // the clone it is ok
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAnth = findViewById(R.id.button2);
        login = findViewById(R.id.login); //there i obtain the value of input
        password = findViewById(R.id.password); // there in obtain the value of input
        textview = findViewById(R.id.textView);
        btnAnth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        String basicAuth = "Basic " + Base64.encodeToString("duclaire:samantha".getBytes(),
                                Base64.NO_WRAP);
                        URL url = null;
                        try {
                            url = new URL(" https://httpbin.org/basic-auth/duclaire/samantha"); //i added the S of http
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestProperty("Authorization", basicAuth); //i allows a request to sent credentiels
                            try {
                                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                                String s = readStream(in);
                                Log.i("JFL", s);
                                 textview.setText(s);
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
            }
        });
    }
        //i create a new Thread to resolve the issues of Android that said "you are doing a long task in the main thread "

        public static String readStream (InputStream is) throws IOException {
            StringBuilder sb = new StringBuilder();
            BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
            for (String line = r.readLine(); line != null; line = r.readLine()) {
                sb.append(line);
            }
            is.close();
            return sb.toString();
        }
    }
