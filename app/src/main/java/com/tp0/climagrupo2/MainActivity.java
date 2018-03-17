package com.tp0.climagrupo2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    int cityCode = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isOnline()){
            Context context = getApplicationContext();
            CharSequence text = "No fue posible conectarse al servidor, por favor reintente más tarde";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        } else {
            setContentView(R.layout.activity_main);
        }

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /** Called when the user taps the cities button */
    public void selectCity(View view) {
        Intent intent = new Intent(this, CityActivity.class);
        startActivityForResult(intent, cityCode);
    }

    public void refreshWeather(View view) {
        if (!isOnline()){
            Context context = getApplicationContext();
            CharSequence text = "No fue posible conectarse al servidor, por favor reintente más tarde";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if ((requestCode == cityCode) && (resultCode == RESULT_OK)){
            TextView tvCity = (TextView)findViewById(R.id.tvCity);
            if (CityActivity.MyStaticString != null && !CityActivity.MyStaticString.equals(""))
                tvCity.setText(CityActivity.MyStaticString);
            System.out.println("codigo: " + data.getDataString());
            String url = "http://api.openweathermap.org/data/2.5/forecast?id=3435910&APPID=a7cdafb6e20c8ea2915a3c5bc16da0a3";
            volleyJsonObjectRequest(url);
        }
    }

    public void volleyJsonObjectRequest(String url){

        String  REQUEST_TAG = "com.tp0.climagrupo2.volleyJsonObjectRequest";

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq,REQUEST_TAG);
    }
}
