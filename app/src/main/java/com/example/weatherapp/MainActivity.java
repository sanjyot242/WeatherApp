package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    EditText city;
    Button weather;
    TextView main,description;

    public void getDetails(View view) {
        if(city.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter City name", Toast.LENGTH_SHORT).show();
        }else {
            getJsonData task = new getJsonData();
            try {
                task.execute("https://api.openweathermap.org/data/2.5/weather?q="+city.getText().toString()+"&APPID=fa9a26ce1c2a969df0bfed7ba7ca9860").get();

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class getJsonData extends AsyncTask<String, Void, String>{

        String result="";
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                StringBuilder inputLine = new StringBuilder() ;
                while(data != -1){
                    char currrent = (char) data;
                    inputLine.append(currrent);
                    data=reader.read();

                }

                result=inputLine.toString();
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String message="";
            try {
                JSONObject jsonString = new JSONObject(s);
                String weatherInfo = jsonString.getString("weather");

                JSONArray arr = new JSONArray(weatherInfo);
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String mains=jsonPart.getString("main");
                    String descriptions=jsonPart.getString("description");

                    if(!mains.equals("") && !descriptions.equals("")){
                       message += mains+":"+descriptions+"\r\n";
                    }
//                    Log.i("main", ""+jsonPart.getString("main"));
//                    Log.i("description", ""+jsonPart.getString("description"))

                }

                if(!message.equals("")){
                    main.setText(message);

                }


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Name not found", Toast.LENGTH_SHORT).show();

            }




        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        city=findViewById(R.id.editText);
        weather=findViewById(R.id.button);
        main=findViewById(R.id.main);


    }
}
