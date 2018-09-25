package com.example.osaho.smarttools;

import android.os.AsyncTask;
import java.net.URL;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * An helper class to get weather data from OpenWeather
 */
public class HelperClass {

    //URL to openweather API
    private static final String URL =
            "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";

    //Open weather API KEY
    private static final String API_KEY= "b77684ef8b00e7a26fbd00260b3b81fb";


    public interface AsyncResponse {

        void doneProcessing(String temperature, String city);
    }

    /**
     * Static class to get weather data
     */
    public static class placeIdTask extends AsyncTask<String, Void, JSONObject> {

        public AsyncResponse setTask = null;//Call back interface

        public placeIdTask(AsyncResponse asyncResponse) {
            setTask = asyncResponse;
        }


        /**
         * Function to get weather data on post exection
         * @param getData is the JSONObject
         */
        @Override
        protected void onPostExecute(JSONObject getData) {
            try {
                if(getData != null){
                    //JSONObject details = getData.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = getData.getJSONObject("main");

                    String city = getData.getString("name").toUpperCase(Locale.US) + ", " + getData.getJSONObject("sys").getString("country");

                    //get the temperature and format it to whole number
                    String temperature = String.format("%.0f", (main.getDouble("temp") * 1.8)+32)+ "°F";

                    //get the current temperature and the city
                    setTask.doneProcessing(temperature, city);

                }

                //Handle any JSONexception
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

        //Function to read weather as a background process
        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject readWeatherInfo = null;
            try {
                readWeatherInfo = getData(params[0], params[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return readWeatherInfo;
        }


    }

    /**
     * Function to get latitude and longitude of the location
     * @param lat is the latitude
     * @param lon is the longitude
     * @return the latitude and longitude
     */

    public static JSONObject getData(String lat, String lon){
        try {
            URL url = new URL(String.format(URL, lat, lon));

            //creating an HTTP connection to the openweather API
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            //Creating a connecion based on the API KEY
            connection.addRequestProperty("x-api-key", API_KEY);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer(1024);
            String string="";

            //reading data
            while((string=reader.readLine())!=null)
                stringBuffer.append(string).append("\n");
            reader.close();

            JSONObject data = new JSONObject(stringBuffer.toString());

            if(data.getInt("cod") != 200){
                return null;
            }

            return data;

            //handle any exceptions
        }catch(Exception e){
            return null;
        }
    }

}