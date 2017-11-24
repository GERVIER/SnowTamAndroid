package com.example.rgerv.snowtamproject.Utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by rgerv on 20/11/2017.
 * Allow the user to find the snowtam of an airport with it's ICAO code.
 * The information came from the website : https://www.icao.int/safety/iStars/Pages/API-Data-Service.aspx
 */

public class AirportSnowTamRetrieving {

    private static AirportSnowTamRetrieving instance = new AirportSnowTamRetrieving();

    public static AirportSnowTamRetrieving getInstance(){
        return instance;
    }

    private AirportSnowTamRetrieving(){

    }

    /**
     * Create a request to the API to get the airport last snowtam
     * @param airportCode : ICAO code of the airport you want the last snowtam
     * @param context : context of the calling view
     * @param responseListener : listener launched when the request is finish. Typically, what is wanted to do with the data retrieved
     * @param errorListener : listener launched is an error occur during the request
     */
    public void RetrieveInformation(String airportCode, Context context, Response.Listener responseListener, Response.ErrorListener errorListener){
        RequestQueue queue = Volley.newRequestQueue(context);
        String ApiKey = "f08b79d0-ca26-11e7-b99a-fde72d126a17";

        String request = "https://v4p4sz5ijk.execute-api.us-east-1.amazonaws.com/anbdata/states/notams/notams-list?api_key=" + ApiKey
                + "&format=json&type=airport&Qcode=&locations="+airportCode+"&qstring=&states=&ICAOonly=";

        JsonArrayRequest airPortRequest = new JsonArrayRequest(request, responseListener, errorListener );

        queue.add(airPortRequest);
    }
}
