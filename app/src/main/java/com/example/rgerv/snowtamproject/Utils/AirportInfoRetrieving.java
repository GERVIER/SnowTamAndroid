package com.example.rgerv.snowtamproject.Utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by rgerv on 15/11/2017.
 * Allow the recuperation of the information of an airport like it's localisation and it's name
 * The information came from the website : https://www.icao.int/safety/iStars/Pages/API-Data-Service.aspx
 */

public class AirportInfoRetrieving{

    private static AirportInfoRetrieving instance = new AirportInfoRetrieving();

    public static AirportInfoRetrieving getInstance(){
        return instance;
    }

    private AirportInfoRetrieving(){

    }

    /**
     * Create a request to the API to get the airport information
     * @param airportName : ICAO code of the airport you want the information
     * @param context : context of the calling view
     * @param responseListener : listener launched when the request is finish. Typically, what is wanted to do with the data retrieved
     * @param errorListener : listener launched is an error occur during the request
     */
    public static void RetrieveInformation(String airportName, Context context, Response.Listener responseListener, Response.ErrorListener errorListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        String ApiKey = "f08b79d0-ca26-11e7-b99a-fde72d126a17";
        String request = "https://v4p4sz5ijk.execute-api.us-east-1.amazonaws.com/anbdata/airports/locations/doc7910?api_key="+ApiKey+"&airports="+airportName+"&format=json";

        JsonArrayRequest airPortRequest = new JsonArrayRequest(request, responseListener, errorListener );

        queue.add(airPortRequest);
    }
}
