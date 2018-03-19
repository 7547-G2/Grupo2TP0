package com.tp0.climagrupo2;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int cityCode = 000;
    String cityId = "3433955";
    String BASE_URI = "https://grupo2-api-backend.herokuapp.com/weather/";
    List<ResponseInfo> tempList = new ArrayList<>();

    String mocker = "{\"id\": 707860,\"name\": \"Hurzuf\",\"country\": \"UA\",\"info\": [{\"date\": \"2018-03-18\",\"dayTemp\": 15,\"nightTemp\": 10,\"dayIcon\":\"01d\",\"nightIcon\":\"01n\"},{\"date\": \"2018-03-19\",\"dayTemp\": 16, \"nightTemp\": 11,\"dayIcon\":\"01d\",\"nightIcon\":\"01n\"}" +
            ",{\"date\": \"2018-03-20\",\"dayTemp\": 17, \"nightTemp\": 12,\"dayIcon\":\"01d\",\"nightIcon\":\"01n\"},{\"date\": \"2018-03-21\",\"dayTemp\": 18, \"nightTemp\": 13,\"dayIcon\":\"01d\",\"nightIcon\":\"01n\"}" +
            ",{\"date\": \"2018-03-22\",\"dayTemp\": 19, \"nightTemp\": 14,\"dayIcon\":\"01d\",\"nightIcon\":\"01n\"}]}";

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
            String cityId = readFromFile(this);
            String url = BASE_URI + cityId;
            volleyJsonObjectRequest(url);
            //loadTableDefault();
        }

    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "6559994";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }catch (Exception e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
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
        } else {
            String url = BASE_URI + cityId;
            volleyJsonObjectRequest(url);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == cityCode) && (resultCode == RESULT_OK)){
            TextView tvCity = (TextView)findViewById(R.id.tvCity);
            System.out.println("ciudad: " + CityActivity.selectedCity);
            if (CityActivity.selectedCity != null && !CityActivity.selectedCity.equals(""))
                tvCity.setText(CityActivity.selectedCity);
            //System.out.println("codigo: " + data.getDataString());
            String CityId = String.valueOf(CityActivity.selectedCityId);
            writeToFile(CityId,this);
            String url = BASE_URI + CityId;
            volleyJsonObjectRequest(url);
        }
    }

    private void loadTable() {

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        TableLayout tblAddLayout = (TableLayout) findViewById(R.id.tableLayout);
        tblAddLayout.removeAllViews();
        TextView dayName, tempDay, tempNight;
        ImageView dayIcon, nightIcon;
        String day = "";
        Integer aux;
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
            tempNight = (TextView) tr.findViewById(R.id.tvNightTemp);
            aux = tempList.get(i).getDayTemp();
            System.out.println("aux: " + aux);
            if (aux != 99){
                tempDay.setText(aux.toString() + "°C");
            } else {
                tempDay.setText("");
            }
            dayIcon = (ImageView) tr.findViewById(R.id.ivDay);
            tempNight = (TextView) tr.findViewById(R.id.tvNightTemp);
            aux = tempList.get(i).getNightTemp();
            if (aux != 99){
                tempNight.setText(aux.toString() + "°C");
            } else {
                tempNight.setText("");
            }
            nightIcon = (ImageView) tr.findViewById(R.id.ivNight);
            URL iconurl = null;
            try {
                iconurl = new URL(tempList.get(i).getDayIcon());

                Glide.with(this).load(iconurl).into(dayIcon);
                iconurl = new URL(tempList.get(i).getNightIcon());
                Glide.with(this).load(iconurl).into(nightIcon);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (java.io.IOException e) {
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            tblAddLayout.addView(tr);
        }
    }

    public void volleyJsonObjectRequest(String url){

        String  REQUEST_TAG = "com.tp0.climagrupo2.volleyJsonObjectRequest";
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("respuesta: " + response.toString());
                        parseResponse(response.toString());
                        loadTable();
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
            String tmp, day, dayIcon, nightIcon;
            Integer dayTemp, nightTemp;
            for (int j=0;j<jsonInfo.length();j++){
                aux = jsonInfo.getJSONObject(j);
                tmp = aux.getString("date");
                day = getDay(tmp);
                day = day + ", " + tmp.substring(8, 10) + "/" + tmp.substring(5, 7);
                if (aux.has("dayTemp")) {
                    dayTemp = aux.getInt("dayTemp");
                    dayIcon = "http://openweathermap.org/img/w/"+aux.getString("dayIcon")+".png";
                } else {
                    dayTemp = 99;
                    dayIcon = "";
                }
                if (aux.has("nightTemp")) {
                    nightTemp = aux.getInt("nightTemp");
                    nightIcon = "http://openweathermap.org/img/w/" + aux.getString("nightIcon") + ".png";
                }else {
                    nightTemp = 99;
                    nightIcon = "";
                }
                ResponseInfo elem = new ResponseInfo(day, dayTemp, nightTemp,dayIcon,nightIcon);
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
