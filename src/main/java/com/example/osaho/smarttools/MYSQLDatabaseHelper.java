package com.example.osaho.smarttools;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Program to use Volley library to make a request queue to MYSQL Database
 */
public class MYSQLDatabaseHelper {

    private static MYSQLDatabaseHelper mysqlDatabaseHelper;
    private RequestQueue requestQueue;
    private static Context mCtx;

    /**
     * Overloaded constructor
     * @param context is the Context object
     */
    private MYSQLDatabaseHelper(Context context){

        mCtx = context;
        requestQueue = getRequestQueue();
    }

    /**
     * Function to create an instance of MYSQLDatabaseHelper
     * @param context is the Context object
     * @return the instance
     */
    public static synchronized MYSQLDatabaseHelper getmInstance(Context context){

        if(mysqlDatabaseHelper==null){

            mysqlDatabaseHelper = new MYSQLDatabaseHelper(context);
        }

        return mysqlDatabaseHelper;
    }


    /**
     * Function to make a request queue
     * @return the request
     */
    public RequestQueue getRequestQueue() {

        if(requestQueue==null){

            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Functon to add a request to the request queue
     * @param request is the request to make
     * @param <T>
     */
    public <T> void addTorequestque(Request<T> request){
requestQueue.add(request);

    }
}
