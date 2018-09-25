package com.example.osaho.smarttools;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Program to get exchange rate and also to convert between currencies in real time
 */

public class Main3Activity extends AppCompatActivity
{

    EditText val;
    TextView dol, eur, pound, naira;
    Button conv;
    Spinner spin;
    private int index;
    private String [] r = new String[20];
    private Double result;

    /**
     * Default value when exchage rates are not in real time
     */
    public static Double currencyRate = 1.1405; //USD
    public static  Double currencyRateGBP = 0.8947;//Britain POUND
    public static  Double currencyRateEUR = 1.00; //Europe EURO
    public static  Double currencyRateNGN = 416.3695; //Nigerian NAIRA



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        getLiveCurrencyRate();
        calculate();

    }


    /**
     * Function to convert between currencies using live exchange rate in real time
     */
    public void calculate () {
        val = findViewById(R.id.value);
        dol = findViewById(R.id.dollar);
        eur = findViewById(R.id.euro);
        pound = findViewById(R.id.pound);
        naira = findViewById(R.id.naira);
        conv = findViewById(R.id.conv);
        spin = findViewById(R.id.spin);


        //setting an adapter to hold resource values items(array)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //setting the items to show in the spinner view
        spin.setAdapter(adapter);


        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                index = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> pareny) {

            }
        });


        conv.setOnClickListener(new View.OnClickListener() {

            /**
             * Function to convert between currencies when the button is clicked
             * @param view is the button view
             */
            @Override
            public void onClick(View view) {

                //execute calculation when input is not empty
                if (val.getText().toString().trim().length() > 0 && !val.getText().toString().trim().equals(".")) {

                    //get the text from the EditView
                    String textValue = val.getText().toString();

                    //Convert the gotten string value to a double
                    result = Double.parseDouble(textValue);

                    //converting from USD to EURO, POUND, and NAIRA
                    if (index == 0) {
                        r[0] = String.valueOf(currencyRate);
                        r[1] = String.valueOf(currencyRateEUR);
                        r[2] = String.valueOf(currencyRateGBP);
                        r[3] = String.valueOf(currencyRateNGN);


                        dol.setText("" + String.format("%.2f", result) + " USD");
                        eur.setText("" + String.format("%.2f", (currencyRateEUR * result) / currencyRate) + " EUR");
                        pound.setText("" + String.format("%.2f", (currencyRateGBP * result) / currencyRate) + " GBP");
                        naira.setText("" + String.format("%.2f", (currencyRateNGN * result) / currencyRate) + " NGN");


                        //converting EURO to USD, POUND, and NAIRA
                    } else if (index == 1) {

                        r[0] = String.valueOf(currencyRate);
                        r[1] = String.valueOf(currencyRateEUR);
                        r[2] = String.valueOf(currencyRateGBP);
                        r[3] = String.valueOf(currencyRateNGN);


                        dol.setText("" + String.format("%.2f", (currencyRate * result) / currencyRateEUR) + " USD");
                        eur.setText("" + String.format("%.2f", result) + " EUR");
                        pound.setText("" + String.format("%.2f", (currencyRateGBP * result) / currencyRateEUR) + " GBP");
                        naira.setText("" + String.format("%.2f", (currencyRateNGN * result) / currencyRateEUR) + " NGN");


                        //converting from POUND to USD, EURO, and NAIRA
                    } else if (index == 2) {


                        r[0] = String.valueOf(currencyRate);
                        r[1] = String.valueOf(currencyRateEUR);
                        r[2] = String.valueOf(currencyRateGBP);
                        r[3] = String.valueOf(currencyRateNGN);


                        dol.setText("" + String.format("%.2f", (currencyRate * result) / currencyRateGBP) + " USD");
                        eur.setText("" + String.format("%.2f", (currencyRateEUR * result) / currencyRateGBP) + " EUR");
                        pound.setText("" + String.format("%.2f", result) + " GBP");
                        naira.setText("" + String.format("%.2f", (currencyRateNGN * result) / currencyRateGBP) + " NGN");


                        //converting from NAIRA to USD, EURO, and POUND
                    } else if (index == 3) {


                        r[0] = String.valueOf(currencyRate);
                        r[1] = String.valueOf(currencyRateEUR);
                        r[2] = String.valueOf(currencyRateGBP);
                        r[3] = String.valueOf(currencyRateNGN);


                        dol.setText("" + String.format("%.2f", (currencyRate * result) / currencyRateNGN) + " USD");
                        eur.setText("" + String.format("%.2f", (currencyRateEUR * result) / currencyRateNGN) + " EUR");
                        pound.setText("" + String.format("%.2f", (currencyRateGBP * result) / currencyRateNGN) + " GBP");
                        naira.setText("" + String.format("%.2f", result) + " NGN");


                    }

                    //Make a toast if input is invalid
                }else {
                    Toast.makeText(getApplicationContext(), "Not allowed. Enter number", Toast.LENGTH_LONG).show();
                    pound.setText("");
                    dol.setText("");
                    naira.setText("");
                    dol.setText("");

                }
            }


        });


    }


    /**
     * Function to get currency exchange rate in real time using fixer currency exchange rate API
     */
    public void getLiveCurrencyRate()
    {
        /**
         * getting all the views from the curresponding XML
         */
        TextView euroTextview = findViewById(R.id.euroTextview);
        TextView dollarTextview = findViewById(R.id.dollarTextview);
        TextView poundTextview =  findViewById(R.id.poundTextview);
        TextView nairaTextview = findViewById(R.id.nairaTextview);

        //Create a new thread to run in background
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    //URL to Fixer currency exchange rate API using the API KEY
                    URL url = new URL("http://data.fixer.io/api/latest?access_key=50ee19027817c7f3abf8fbf8361b15ff");
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));


                    String inputLine = in.readLine();

                    //reading live exchange rate using JSON object
                    if(inputLine != null)
                    {
                        JSONObject json = new JSONObject(inputLine);
                        JSONObject rates = json.getJSONObject("rates");
                        Double rate = rates.getDouble("USD");
                        Double rateEUR = rates.getDouble("EUR");
                        Double rateGBP = rates.getDouble("GBP");
                        Double rateNGN = rates.getDouble("NGN");

                        currencyRate = rate;
                        currencyRateEUR = rateEUR;
                        currencyRateGBP = rateGBP;
                        currencyRateNGN = rateNGN;


                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable()
                        {
                            public void run()
                            {
                                Toast.makeText(Main3Activity.this, "Rate successfully updated.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    in.close();
                }

                //handle any IO exceptions
                catch(IOException e)
                {
                    e.printStackTrace();

                    Log.i("myException", e.toString());

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable()
                    {
                        public void run()
                        {
                            //Make a toast if rate fail to update
                            Toast.makeText(Main3Activity.this, "Rate failed to update.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //catch any JSONObject exception
                catch(JSONException e)
                {
                    e.printStackTrace();

                    Log.i("myException", e.toString());

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable()
                    {
                        public void run()
                        {
                            //Make a toast if rate fail to update
                            Toast.makeText(Main3Activity.this, "Rate failed to update.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        //starting the thread
        thread.start();

        //Output the current exchage rate in real time
        euroTextview.setText("1 EUR = " + String.format("%.2f", (currencyRateEUR * currencyRate)) + " USD");
        dollarTextview.setText("1 USD = " + String.format("%.2f", (currencyRateEUR / currencyRate)) + " EUR");
        poundTextview.setText("1 USD = " + String.format("%.2f", ( currencyRateGBP / currencyRate )) + " GBP");
        nairaTextview.setText("1 USD = " + String.format("%.2f", (currencyRateNGN / currencyRate)) + " NGN");


    }


}