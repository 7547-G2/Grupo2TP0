package com.tp0.climagrupo2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the cities button */
    public void selectCity(View view) {
        Intent intent = new Intent(this, CitiesActivity.class);
        startActivity(intent);

    }

}
