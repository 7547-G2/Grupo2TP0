package com.tp0.climagrupo2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CityActivity extends AppCompatActivity {

    ListView lvCities;

    String[] cities = new String[]{"Buenos Aires, AR", "Mar del Plata, AR", "Montevideo, UR"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        lvCities = (ListView)findViewById(R.id.cityList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, cities);

        lvCities.setAdapter(adapter);

        lvCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("pos: " + position);

            }
        });
    }
}
