package com.example.rgerv.snowtamproject.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rgerv on 15/11/2017.
 * Allow the recuperation of the information of an airport like it's localisation and it's name
 * The information came from the website : https://www.icao.int/safety/iStars/Pages/API-Data-Service.aspx
 */

public class AirportInfoRetrieving{

    public static void RetriveInformation(String AirportName, Context context){
    //TODO : FINISH TO IMPLEMENT THE GSON REQUEST
        RequestQueue queue = Volley.newRequestQueue(context);
        /*
        GSONRequest<ArtistList> airPortRequest =
                new GSONRequest<>("http://api.deezer.com/search/artist?q=" + artist,
                        ArtistList.class, null, responseListener, errorListener);

        queue.add(artistRequest);*/

    }
}
