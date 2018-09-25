package com.example.osaho.smarttools;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Program to insert data into an external database (MYSQL Database)
 */

public class Main5Activity extends AppCompatActivity {
    private TextView inch,kilometer,yard,feet,mile;
    private Button convert, recent;
    private Spinner spin;
    private int index;
    private EditText input;
    private Double result;
    private String [] r = new String[20];


    //Link to MYSQL server URL for POST request
    String server_url = "http://18.219.106.103/unit.php";
    AlertDialog.Builder builder;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        db = new DatabaseHelper(this);
        calculate();
        showData();


       builder = new AlertDialog.Builder(Main5Activity.this);


    }

    /**
     * Function to insert data into MYSQL Database
     * @param inch is the first data
     * @param feet is the second data
     * @param yard is the third data
     * @param kilometer is the fourth data
     * @param mile is the fifth data
     */
    public void addData(String inch, String feet, String yard, String kilometer, String mile) {


        db.insertData(inch, feet, yard,kilometer, mile );

    }


    /**
     * Function to post data to MYSQL database on server connection
     * @param inch is the first String
     * @param feet is the second String
     * @param yard is the third String
     * @param kilometer is the fourth String
     * @param mile is the fifth String
     */
    public void postExDB(final String inch, final String feet, final String yard, final String kilometer, final String mile) {

        //Making a POST request to MYSQL database
        StringRequest stringrequest = new StringRequest(Request.Method.POST, server_url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {

            /**
             * Function to get any error from the POST request
             * @param error is the error associated with the request
             */
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Main5Activity.this, "Error...", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){

            /**
             * Function to put data into a Map collection
             * @return the data inside the HashMap collection
             * @throws AuthFailureError if no connection to MYSQL databse
             */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("INCH", inch);
                param.put("FEET", feet);
                param.put("YARD", yard);
                param.put("KILOMETER", kilometer);
                param.put("MILE", mile);
                return param;
            }
        };

        //Adding the POST request
        MYSQLDatabaseHelper.getmInstance(Main5Activity.this).addTorequestque(stringrequest);

    }

    /**
     * Function to convert between measurement units and insert them into an SQLIte Databse and MYSQL Database
     */
    public void calculate () {

        //getting all the views based on the corresponding XML
        inch = findViewById(R.id.inch);
        kilometer = findViewById(R.id.km);
        yard = findViewById(R.id.yard);
        feet = findViewById(R.id.feet);
        mile = findViewById(R.id.miles);
        convert = findViewById(R.id.conv);
        input = findViewById(R.id.ed);
        input = findViewById(R.id.ed);
        spin = findViewById(R.id.spinner);
        recent = findViewById(R.id.recent);


        //created an adapter to hold the string value items
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set adapter to show items on spinner
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


        convert.setOnClickListener(new View.OnClickListener() {

            //Convert and insert the values into SQLite Database and MYSQL Database
            @Override
            public void onClick(View view) {
                if (input.getText().toString().trim().length() > 0 && !input.getText().toString().trim().equals(".")) {
                    String textValue = input.getText().toString();
                    result = Double.parseDouble(textValue);

                    //Converting from inch to feet, yard, kilometer, and mile
                    if (index == 0) {
                        r[0] = String.valueOf(1);
                        r[1] = String.valueOf(0.083333);
                        r[2] = String.valueOf(0.027778);
                        r[3] = String.valueOf(0.000025);
                        r[4] = String.valueOf(0.000016);

                        inch.setText("" + String.format("%.2f", result * 1) + " inch");
                        feet.setText("" + String.format("%.2f", (Double.parseDouble(r[1]) * result)) + " ft");
                        yard.setText("" + String.format("%.2f", (Double.parseDouble(r[2]) * result)) + " yard");
                        kilometer.setText("" + String.format("%.2f", (Double.parseDouble(r[3]) * result)) + " km");
                        mile.setText("" + String.format("%.2f", (Double.parseDouble(r[4]) * result)) + " mile");


                        //insering data into internal database SQLite
                        addData(String.format("%.2f", result * 1), String.format("%.2f", (Double.parseDouble(r[1]) * result)),
                                String.format("%.2f", (Double.parseDouble(r[2]) * result)), String.format("%.2f", (Double.parseDouble(r[3]) * result)),
                                String.format("%.2f", (Double.parseDouble(r[4]) * result)));

                        //inserting data into external database MYSQL
                        postExDB(String.format("%.2f", result * 1), String.format("%.2f", (Double.parseDouble(r[1]) * result)),
                                String.format("%.2f", (Double.parseDouble(r[2]) * result)), String.format("%.2f", (Double.parseDouble(r[3]) * result)),
                                String.format("%.2f", (Double.parseDouble(r[4]) * result)));

                        //Converting from feet to inch, yard, kilometer, and mile
                    } else if (index == 1) {

                        r[0] = String.valueOf(12);
                        r[1] = String.valueOf(1);
                        r[2] = String.valueOf(0.3333333);
                        r[3] = String.valueOf(0.000305);
                        r[4] = String.valueOf(0.000189);


                        inch.setText("" + String.format("%.2f", result * Double.parseDouble(r[0])) + " inch");
                        feet.setText("" + String.format("%.2f", (result)) + " ft");
                        yard.setText("" + String.format("%.2f", (Double.parseDouble(r[2]) * result)) + " yard");
                        kilometer.setText("" + String.format("%.2f", (Double.parseDouble(r[3]) * result)) + " km");
                        mile.setText("" + String.format("%.2f", (Double.parseDouble(r[4]) * result)) + " mile");

                        //insering data into internal database SQLite
                        addData(String.format("%.2f", result * Double.parseDouble(r[0])), String.format("%.2f", (result)),
                                String.format("%.2f", (Double.parseDouble(r[2]) * result)), String.format("%.2f", (Double.parseDouble(r[3]) * result)),
                                String.format("%.2f", (Double.parseDouble(r[4]) * result)));

                        //inserting data into external database MYSQL
                        postExDB(String.format("%.2f", result * Double.parseDouble(r[0])), String.format("%.2f", (result)),
                                String.format("%.2f", (Double.parseDouble(r[2]) * result)), String.format("%.2f", (Double.parseDouble(r[3]) * result)),
                                String.format("%.2f", (Double.parseDouble(r[4]) * result)));


                        //Converting from yard to inch, feet, kilometer, and mile
                    } else if (index == 2) {

                        r[0] = String.valueOf(36);
                        r[1] = String.valueOf(3);
                        r[2] = String.valueOf(1);
                        r[3] = String.valueOf(0.000914);
                        r[4] = String.valueOf(0.000568);


                        inch.setText("" + String.format("%.2f", result * Double.parseDouble(r[0])) + " inch");
                        feet.setText("" + String.format("%.2f", (Double.parseDouble(r[1]) * result)) + " ft");
                        yard.setText("" + String.format("%.2f", (result)) + " yard");
                        kilometer.setText("" + String.format("%.2f", (Double.parseDouble(r[3]) * result)) + " km");
                        mile.setText("" + String.format("%.2f", (Double.parseDouble(r[4]) * result)) + " mile");

                        //insering data into internal database SQLite
                        addData(String.format("%.2f", result * Double.parseDouble(r[0])), String.format("%.2f", (Double.parseDouble(r[1]) * result)),
                                String.format("%.2f", (result)), String.format("%.2f", (Double.parseDouble(r[3]) * result)),
                                String.format("%.2f", (Double.parseDouble(r[4]) * result)));

                        //inserting data into external database MYSQL
                        postExDB(String.format("%.2f", result * Double.parseDouble(r[0])), String.format("%.2f", (Double.parseDouble(r[1]) * result)),
                                String.format("%.2f", (result)), String.format("%.2f", (Double.parseDouble(r[3]) * result)),
                                String.format("%.2f", (Double.parseDouble(r[4]) * result)));


                        //Converting from kilometer to inch, feet, yard, and mile
                    } else if (index == 3) {


                        r[0] = String.valueOf(39370.079);
                        r[1] = String.valueOf(3280.84);
                        r[2] = String.valueOf(1093.613);
                        r[3] = String.valueOf(1);
                        r[4] = String.valueOf(0.621);


                        inch.setText("" + String.format("%.2f", result * Double.parseDouble(r[0])) + " inch");
                        feet.setText("" + String.format("%.2f", (Double.parseDouble(r[1]) * result)) + " ft");
                        yard.setText("" + String.format("%.2f", (Double.parseDouble(r[2]) * result)) + " yard");
                        kilometer.setText("" + String.format("%.2f", (result)) + " km");
                        mile.setText("" + String.format("%.2f", (Double.parseDouble(r[4]) * result)) + " mile");

                        //insering data into internal database SQLite
                        addData(String.format("%.2f", result * Double.parseDouble(r[0])), String.format("%.2f", (Double.parseDouble(r[1]) * result)),
                                String.format("%.2f", (Double.parseDouble(r[2]) * result)), String.format("%.2f", (result)),
                                String.format("%.2f", (Double.parseDouble(r[4]) * result)));


                        //inserting data into external database MYSQL
                        postExDB(String.format("%.2f", result * Double.parseDouble(r[0])), String.format("%.2f", (Double.parseDouble(r[1]) * result)),
                                String.format("%.2f", (Double.parseDouble(r[2]) * result)), String.format("%.2f", (result)),
                                String.format("%.2f", (Double.parseDouble(r[4]) * result)));

                        //Converting from mile to inch, feet, yard, and kilometer
                    } else if (index == 4) {

                        r[0] = String.valueOf(63360);
                        r[1] = String.valueOf(5280);
                        r[2] = String.valueOf(1760);
                        r[3] = String.valueOf(1.609344);
                        r[4] = String.valueOf(1);

                        inch.setText("" + String.format("%.2f", result * Double.parseDouble(r[0])) + " inch");
                        feet.setText("" + String.format("%.2f", (Double.parseDouble(r[1]) * result)) + " ft");
                        yard.setText("" + String.format("%.2f", (Double.parseDouble(r[2]) * result)) + " yard");
                        kilometer.setText("" + String.format("%.2f", (Double.parseDouble(r[3]) * result)) + " km");
                        mile.setText("" + String.format("%.2f", (result)) + " mile");

                        //insering data into internal database SQLite

                        addData(String.format("%.2f", result * Double.parseDouble(r[0])), String.format("%.2f", (Double.parseDouble(r[1]) * result)),
                                String.format("%.2f", (Double.parseDouble(r[2]) * result)), String.format("%.2f", (Double.parseDouble(r[3]) * result)),
                                String.format("%.2f", (result)));

                        //inserting data into external database MYSQL
                        postExDB(String.format("%.2f", result * Double.parseDouble(r[0])), String.format("%.2f", (Double.parseDouble(r[1]) * result)),
                                String.format("%.2f", (Double.parseDouble(r[2]) * result)), String.format("%.2f", (Double.parseDouble(r[3]) * result)),
                                String.format("%.2f", (result)));

                    }

                    //Make a toast when value is invalid
                } else {
                    Toast.makeText(getApplicationContext(), "Not allowed. Enter number", Toast.LENGTH_LONG).show();
                    yard.setText("");
                    mile.setText("");
                    inch.setText("");
                    kilometer.setText("");
                    feet.setText("");
                }
            }
        });

    }


    /**
     * Function to display data from SQLite database using an Alert Dialog
     */
    public void showData() {


        final Button show = findViewById(R.id.recent);


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //using cursor to read all the data from the SQLite database
                Cursor res = db.getAllData();

                //if there is no data in the database
                if(res.getCount() == 0){

                    showMessage("Recent Conversion","No Data");
                    return;


                }
                StringBuffer buffer = new StringBuffer();

                //while there is data in the database, continue to read and add data
                while (res.moveToNext()) {

                    buffer.append("INCH: " + res.getString(0) + "\n");
                    buffer.append("FEET: " + res.getString(1) + "\n");
                    buffer.append("YARD: " + res.getString(2) + "\n");
                    buffer.append("KILOMETER: " + res.getString(3) + "\n");
                    buffer.append("MILE: " + res.getString(4) + "\n\n");
                }

                //display data if there is data in the SQLite Database
                showMessage("Recent Conversion", buffer.toString());


            }
        });

    }

    /**
     * Function to display the data
     * @param title is the title message
     * @param message is the message or data to be outputted
     */
    public void showMessage(String title, String message) {

        //Using an Alert Dialog to output data
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();


    }

}

