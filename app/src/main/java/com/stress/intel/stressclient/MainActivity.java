package com.stress.intel.stressclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

public class MainActivity extends AppCompatActivity {

    private String ipAddr;
    private Button btn_connect;
    private EditText ipDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_connect = (Button)findViewById(R.id.btn_connect);
        ipDisplay = (EditText) findViewById(R.id.ipAddress);
        btn_connect.setOnClickListener(new View.OnClickListener(){

            public void onClick (View v){

                ipAddr = ipDisplay.getText().toString().trim();
                Log.d("Check", ipAddr);
                if(ipAddr!=null && !ipAddr.equals("") && isValid(ipAddr)){

                    // Try to Connect to the server on the given ipaddress
                    new NetworkConnection().execute();

                }
                else
                {
                    Log.d("Error", "Addr: "+ipAddr);
                    Toast.makeText(getApplicationContext(),"IP Address Provided is either null or not valid", Toast.LENGTH_LONG).show();
                }
            }



        });
    }


    private class NetworkConnection extends AsyncTask <Void, Void, Void> {

        public NetworkConnection(){
        }

        @Override
        protected Void doInBackground(Void... params) {

            Log.d("Check", ""+ipAddr);

            if(isConnectedToNetwork()){
                try {
                    URL url = new URL("http://"+ipAddr);
                    try {
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        String response = "",data="";
                        while((data = reader.readLine())!= null){
                            response += data+"\n";
                        }

                        Log.d("Connected", response);

                    }
                    catch(IOException e){
                        Log.d("Error", "IOException Occured, Cannot Open the Connection");
                        e.printStackTrace();
                    }
                }
                catch (MalformedURLException e) {
                    Log.d("Error","URL string is not correct");
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Connected to "+ipAddr, Toast.LENGTH_LONG).show();

        }

        private boolean isConnected(String ipAddr) {

            Log.d("Check", "isConnected function");

            if(isConnectedToNetwork()){
                try {
                    URL url = new URL("http://"+ipAddr);
                    try {
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        String response = "",data="";
                        while((data = reader.readLine())!= null){
                            response += data+"\n";
                        }

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        Log.d("Connected", response);

                        return true;
                    }
                    catch(IOException e){
                        Log.d("Error", "IOException Occured, Cannot Open the Connection");
                        e.printStackTrace();
                    }
                }
                catch (MalformedURLException e) {
                    Log.d("Error","URL string is not correct");
                    e.printStackTrace();
                }
            }


            return false;
        }

        private boolean isConnectedToNetwork() {
            // Defatult functionality given for now
            return true;
        }
    }


    private boolean isValid(String ipAddr) {
        // Defatult functionality given for now

        return true;
    }



}
