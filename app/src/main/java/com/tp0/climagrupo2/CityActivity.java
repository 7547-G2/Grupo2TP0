package com.tp0.climagrupo2;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityActivity extends AppCompatActivity {

    String BASE_URI = "https://grupo2-api-backend.herokuapp.com/city/country/";
    String BASE_URI2 = "http://api.openweathermap.org/data/2.5/forecast?";
    ListView lvCities;

    //Adapter for listview
    ArrayAdapter<String> adapter;

    //ArrayList for listview
    ArrayList<String> data = new ArrayList<String>();

    public static String selectedCity;
    public static int selectedCityId;

    //Edittext for search
    EditText searchdata;

    String[] cities = new String[]{"Buenos Aires, AR", "Mar del Plata, AR", "Montevideo, UR"};

    final List<City> cities2 = new ArrayList();
    final List<String> cities3 = new ArrayList<String>();

    String jsonInput = "[{\"id\": 101, \"city\": \"Buenos Aires\", \"country\": \"AR\"}, " +
            "{\"id\": 102, \"city\": \"Mar del Plata\", \"country\": \"AR\"}, " +
            "{\"id\": 103, \"city\": \"Montevideo\", \"country\": \"UR\"}]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        lvCities = (ListView) findViewById(R.id.cityList);
        searchdata = (EditText) findViewById(R.id.txtFilter);

        volleyJsonObjectRequest(BASE_URI + "AR");

        searchdata.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CityActivity.this.adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        lvCities.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {

                if (scrollState == 0) {
                    InputMethodManager inputManager = ( InputMethodManager ) getApplicationContext().getSystemService( Context.INPUT_METHOD_SERVICE );
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {


            }
        });

        lvCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cad = String.valueOf(cities2.get(position).getId());
                System.out.println("id: " + cities2.get(position).getId());
                Intent data = new Intent();
                data.setData(Uri.parse(cad));
                setResult(RESULT_OK, data);
                selectedCity = adapter.getItem(position);
                City city = new City(0,selectedCity.substring(0,selectedCity.indexOf(',')),"AR");

                for (City c : cities2) {
                    if (selectedCity.substring(0,selectedCity.indexOf(',')).equals(c.getName())) {
                        city = c;
                    }
                }

                selectedCityId = city.getId();
                System.out.println("ciudad: " + selectedCity);
                finish();
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void volleyJsonObjectRequest(String url){

        String REQUEST_TAG = "cities";

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response.toString());
                        parseResponse(response.toString());
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        System.out.println("error 2: " + error.toString());
                    }
                }
        );

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest,REQUEST_TAG);
    }

    private void parseResponse(String s) {

        JSONArray jArray = null;
        try {
            jArray = new JSONArray(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String city, country;
        for(int i=0;i<jArray.length();i++){
            try {
                JSONObject json_data = jArray.getJSONObject(i);
                city = json_data.getString("name");
                country = json_data.getString("country");
                City elem = new City(json_data.getInt("id"), city, country);
                cities2.add(elem);
                cities3.add(city + ", " + country);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String[] simpleArray = new String[ cities3.size() ];
        Collections.sort(cities2, new Comparator<City>() {
            public int compare(City c1, City c2) {
                if (c1.getFullName().compareTo(c2.getFullName()) < 0) return -1;
                if (c1.getFullName().compareTo(c2.getFullName()) > 0) return 1;
                return 0;
            }});
        java.util.Collections.sort(cities3);
        cities3.toArray(simpleArray);
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, simpleArray);

        adapter=new ArrayAdapter<String>(this,R.layout.searchresults,R.id.results,simpleArray);

        lvCities.setAdapter(adapter);

    }

    public static void hideKeyboard(Context context ) {

        try {
            InputMethodManager inputManager = ( InputMethodManager ) context.getSystemService( Context.INPUT_METHOD_SERVICE );

            View view = ( (Activity) context ).getCurrentFocus();
            if ( view != null ) {
                inputManager.hideSoftInputFromWindow( view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
