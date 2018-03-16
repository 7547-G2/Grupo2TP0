package com.tp0.climagrupo2;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity {

    ListView lvCities;

    String[] cities = new String[]{"Buenos Aires, AR", "Mar del Plata, AR", "Montevideo, UR"};

    String jsonInput = "[{\"id\": 101, \"city\": \"Buenos Aires\", \"country\": \"AR\"}, " +
            "{\"id\": 102, \"city\": \"Mar del Plata\", \"country\": \"AR\"}, " +
            "{\"id\": 103, \"city\": \"Montevideo\", \"country\": \"UR\"}]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        lvCities = (ListView)findViewById(R.id.cityList);

        JSONArray jArray = null;
        try {
            jArray = new JSONArray(jsonInput);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final List<City> cities2 = new ArrayList();
        List<String> cities3 = new ArrayList<String>();
        String city, country;
        for(int i=0;i<jArray.length();i++){
            try {
                JSONObject json_data = jArray.getJSONObject(i);
                city = json_data.getString("city");
                country = json_data.getString("country");
                City elem = new City(json_data.getInt("id"), city, country);
                cities2.add(elem);
                cities3.add(city + ", " + country);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String[] simpleArray = new String[ cities3.size() ];
        cities3.toArray(simpleArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, simpleArray);

        lvCities.setAdapter(adapter);

        lvCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cad = String.valueOf(cities2.get(position).getId());
                System.out.println("id: " + cities2.get(position).getId());
                Intent data = new Intent();
                data.setData(Uri.parse(cad));
                setResult(RESULT_OK, data);
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
}
