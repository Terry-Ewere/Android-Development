package com.example.osaho.smarttools;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Program to convert temperature
 */

public class Main2Activity extends AppCompatActivity {

    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        calculate();
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);
        getCurrentTemperature();



    }

    /**
     * Function to convert between celsius and fahrenheit degrees
     */
    public void calculate() {

        //get all the views from the corresponding XML
        final EditText value = findViewById(R.id.temp);
        final Button conv = findViewById(R.id.conv);
        final RadioButton celfah = findViewById(R.id.c2f);
        final RadioButton fahcel = findViewById(R.id.f2c);


        conv.setOnClickListener(new View.OnClickListener() {

            //execute when the button is clicked
            @Override
            public void onClick(View view) {

                //Reading the value from the EditText view
                String val = value.getText().toString();


                //if celcius to fahrenheit is checked. Then convert celsius value to fahrenheit
                if (celfah.isChecked() && !val.isEmpty()) {
                    Double msg = Double.parseDouble(val);
                    final String celcius = String.valueOf(msg);
                    Double fahrenheit = (1.8 * msg) + 32;
                    // value.setText(fahrenheit.toString());
                    value.setText(String.format("%.0f", (fahrenheit)));

                    //if fahrenheit to censius is checked. Then convert fahrenheit value to celsius
                } else if (fahcel.isChecked() && !val.isEmpty()) {
                    Double msg = Double.parseDouble(val);
                    Double celsius = (msg - 32) * 5 / 9;
                    //value.setText(celsius.toString());

                    value.setText(String.format("%.0f", (celsius)));

                    //Make a toast if input is invalid
                } else if (val.isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Enter Temperature", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    /**
     * Function to get the current temperature and the city
     */
    public void getCurrentTemperature() {

                    //Granting permssion to access location
                if (ActivityCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

                    return;
                }
                client.getLastLocation().addOnSuccessListener(Main2Activity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {


                            final TextView temp = findViewById(R.id.cur);


                            HelperClass.placeIdTask asyncTask =new HelperClass.placeIdTask(new HelperClass.AsyncResponse() {
                                @Override
                                public void doneProcessing(String temperature, String city) {

                                    //Output the current temperature and city
                                   temp.setText("Current Temperature: " + temperature + ", " + city);

                                }

                            });

                            //converting the location latitude to string
                            String lat = String.valueOf(location.getLatitude());

                            //converting the current location longitude to string
                            String lon = String.valueOf(location.getLongitude());

                            //running task
                            asyncTask.execute(lat, lon);
                        }

                });


        }


    /**
     * Function to request permission
     */
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    /**
     * Function to render the the device location (Map)
     * @param view is the the view to be rendered which is the map
     */
    public void getLocation(View view){

        Intent StartNewActivity = new Intent(this, MapsActivity.class);
        startActivity(StartNewActivity);
    }

}
