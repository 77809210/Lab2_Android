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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button btnok;
    EditText login;
    EditText password;
    TextView textview;
    // the clone it is ok
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnok = findViewById(R.id.button);
        login = findViewById(R.id.login); //there i obtain the value of input
        password = findViewById(R.id.password); // there in obtain the value of input
        textview = findViewById(R.id.textView);
        /*Android doesn't allow to make network calls on UI thread; Also il doesn't allow to change anything on the UI if you are on the
        background thread to resolve this problem i create a new create to make a network call
        */
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadSolution ts = new ThreadSolution("https://httpbin.org/basic-auth/duclaire/samantha");//there i call my thread to do a long task
                ts.start();// there i start the thread to do the job
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
        //the goal of this class it's to resolve a problem of the main thread Exception
        public class ThreadSolution extends Thread
        {
            //" https://httpbin.org/basic-auth/duclaire/samantha"
            String URL;
            ThreadSolution(String url) //the construction of the thread
            {
               this.URL = url;
            }
            @Override
            public void run() {
                String basicAuth = "Basic " + Base64.encodeToString("duclaire:samantha".getBytes(),
                        Base64.NO_WRAP);
                URL url = null;
                try {
                    url = new URL(URL); //i added the S of http
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("Authorization", basicAuth); //i allows a request to sent credentiels
                    try {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        String s = readStream(in);
                        Log.i("JFL", s);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textview.setText(s);  //there i set my textview by using my thread to avoid CalledFromThreadException
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
        }
    }
