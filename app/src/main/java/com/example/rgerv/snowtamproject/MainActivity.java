package com.example.rgerv.snowtamproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.rgerv.snowtamproject.Model.SnowTam;
import com.example.rgerv.snowtamproject.Utils.AirportInfoRetrieving;
import com.example.rgerv.snowtamproject.Model.AirportList;
import com.example.rgerv.snowtamproject.Model.Airport;
import com.example.rgerv.snowtamproject.Utils.AirportSnowTamRetrieving;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private String DebugTag = "Debug-MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void searchAirportLocation(View view){
        //TODO LINK WITH THE LIST OF AIRPORT.
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    if(response.length() == 0){
                        Log.d(DebugTag, "No airport Found");
                    }
                    else{
                        JSONObject airportJson = response.getJSONObject(0);
                        Log.d(DebugTag, airportJson.toString());

                        Airport airport = new Airport(airportJson);

                        Log.d(DebugTag, airport.toString());

                        AirportList.getInstance().getAirportList().add(airport);
                        Log.d(DebugTag, "Airport n° " + AirportList.getInstance().getAirportList().size() + " added !");

                        searchSnowTam(AirportList.getInstance().getAirportList().size()-1);
                    }

                }catch (JSONException e ){
                    Log.d(DebugTag, "JSONException --- R\n" + e.getMessage());
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(DebugTag, "onErrorResponse: " + error.getMessage());
            }
        };

        AirportInfoRetrieving.RetrieveInformation("ENZV", this, responseListener, errorListener);
    }

    public void searchSnowTam(final int airportIndex){
        Airport airport = AirportList.getInstance().getAirportList().get(airportIndex);
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String code = "";
                Boolean isSnowTamExisting = false;
                for(int i = 0; i<response.length(); i++){
                    try{
                        code = response.getJSONObject(i).getString("all");
                        if(code.contains("SNOWTAM")){
                            isSnowTamExisting = true;
                            break;
                        }
                    }catch (JSONException e){
                        Log.d(DebugTag, "onErrorResponse: " + e.getMessage());
                    }
                }

                if(isSnowTamExisting){
                    Log.d(DebugTag, "Coded: " + code);
                    Airport airport = AirportList.getInstance().getAirportList().get(airportIndex);
                    SnowTam snowtam = new SnowTam(code);
                    snowtam.decodeSnowTam(airport.getLocation());
                    airport.setSnowtam(snowtam);
                    Log.d(DebugTag, "Decoded: \n" + airport.getSnowtam().getDecodedSnowTam());
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(DebugTag, "onErrorResponse: \n" + error.getMessage());
            }
        };


        AirportSnowTamRetrieving.getInstance().RetrieveInformation(airport.getIcaoCode(),this,  responseListener, errorListener);
    }

}
