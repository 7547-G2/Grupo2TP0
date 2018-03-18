package com.tp0.climagrupo2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int cityCode = 000;
    String BASE_URI = "http://api.openweathermap.org/data/2.5/forecast?";
    List<ResponseInfo> tempList = new ArrayList<>();

    String mocker = "{\"id\": 707860,\"name\": \"Hurzuf\",\"country\": \"UA\",\"info\": [{\"date\": \"2018-03-18\",\"dayTemp\": 15,\"nightTemp\": 10},{\"date\": \"2018-03-19\",\"dayTemp\": 16, \"nightTemp\": 11}" +
            ",{\"date\": \"2018-03-20\",\"dayTemp\": 17, \"nightTemp\": 12},{\"date\": \"2018-03-21\",\"dayTemp\": 18, \"nightTemp\": 13}" +
            ",{\"date\": \"2018-03-22\",\"dayTemp\": 19, \"nightTemp\": 14}]}";

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
            String url = BASE_URI + "id=3435910&APPID=a7cdafb6e20c8ea2915a3c5bc16da0a3";
            volleyJsonObjectRequest(url);
            loadTableDefault();
        }

    }

    private void loadTableDefault() {

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        TableLayout tblAddLayout = (TableLayout) findViewById(R.id.tableLayout);
        tblAddLayout.removeAllViews();
        TextView dayName, tempDay, tempNight;

        for (int i=0;i<5;i++){
            View tr = inflater.inflate(R.layout.table_row, null);
            dayName = (TextView) tr.findViewById(R.id.tvDayName);
            dayName.setText("NNNN");
            tempDay = (TextView) tr.findViewById(R.id.tvDayTemp);
            tempDay.setText("XX" + "°C");
            tempNight = (TextView) tr.findViewById(R.id.tvNightTemp);
            tempNight.setText("XX" + "°C");
            tblAddLayout.addView(tr);
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
        if ((requestCode == cityCode) && (resultCode == RESULT_OK)){
            TextView tvCity = (TextView)findViewById(R.id.tvCity);
            if (CityActivity.MyStaticString != null && !CityActivity.MyStaticString.equals(""))
                tvCity.setText(CityActivity.MyStaticString);
            System.out.println("codigo: " + data.getDataString());
            String url = BASE_URI + "id=3435910&APPID=a7cdafb6e20c8ea2915a3c5bc16da0a3";
            volleyJsonObjectRequest(url);
            loadTable();
        }
    }

    private void loadTable() {

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        TableLayout tblAddLayout = (TableLayout) findViewById(R.id.tableLayout);
        tblAddLayout.removeAllViews();
        TextView dayName, tempDay, tempNight;
        String day = "";
        for (int i=0;i<5;i++){
            View tr = inflater.inflate(R.layout.table_row, null);
            dayName = (TextView) tr.findViewById(R.id.tvDayName);
            switch (i) {
                case 0: day = "Hoy";
                        break;
                case 1: day = "Mañana";
                        break;
                default: day = tempList.get(i).getDay();
                         break;
            }
            dayName.setText(day);
            tempDay = (TextView) tr.findViewById(R.id.tvDayTemp);
            tempDay.setText(tempList.get(i).getDayTemp().toString() + "°C");
            tempNight = (TextView) tr.findViewById(R.id.tvNightTemp);
            tempNight.setText(tempList.get(i).getNightTemp().toString() + "°C");
            tblAddLayout.addView(tr);
        }
    }

    public void volleyJsonObjectRequest(String url){

        String  REQUEST_TAG = "com.tp0.climagrupo2.volleyJsonObjectRequest";
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseResponse(mocker);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error 3: " + error.toString());
            }
        });

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq,REQUEST_TAG);
    }

    private void parseResponse(String response2) {
        try {
            JSONObject response = new JSONObject(response2);
            String city = response.getString("name");
            String country = response.getString("country");
            TextView cityName = (TextView) findViewById(R.id.tvCity);
            cityName.setText(city + ", " + country);
            JSONArray jsonInfo = response.getJSONArray("info");
            JSONObject aux;
            tempList.clear();
            String tmp, day;
            for (int j=0;j<jsonInfo.length();j++){
                aux = jsonInfo.getJSONObject(j);
                tmp = aux.getString("date");
                day = getDay(tmp);
                day = day + ", " + tmp.substring(8, 10) + "/" + tmp.substring(5, 7);
                ResponseInfo elem = new ResponseInfo(day, aux.getInt("dayTemp"), aux.getInt("nightTemp"));
                tempList.add(j,elem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getDay(String input) {
        SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        String ret = "";
        try {
            date = dateformat.parse(input);
            DateFormat dayFormate=new SimpleDateFormat("EEEE");
            ret = dayFormate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
