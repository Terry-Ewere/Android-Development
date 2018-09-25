package com.example.osaho.smarttools;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Program to display a redirection to other activities
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * Function to start the TemperaturePage activity
     * @param view is the view object
     */
    public void TemperaturePage(View view){

        Intent startNewActivity = new Intent(this, Main2Activity.class);
        startActivity(startNewActivity);

    }

    /**
     * Function to start the CurrencyPage activity
     * @param view is the view object
     */
    public void CurrencyPage(View view){

        Intent StartNewActivity = new Intent(this, Main3Activity.class);
        startActivity(StartNewActivity);
    }

    /**
     * Function to start the UnitPage activity
     * @param view is the view object
     */
    public void UnitPage(View view){

        Intent StartNewActivity = new Intent(this, Main5Activity.class);
        startActivity(StartNewActivity);
    }

    /**
     * Function to start the MapPage activity
     * @param view is the view object
     */
    public void CurrentLocation(View view){

        Intent StartNewActivity = new Intent(this, MapsActivity.class);
        startActivity(StartNewActivity);
    }


}
