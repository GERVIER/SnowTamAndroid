package com.example.rgerv.snowtamproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.rgerv.snowtamproject.Model.Airport;
import com.example.rgerv.snowtamproject.Model.AirportList;
import com.example.rgerv.snowtamproject.Model.SnowTam;
import com.example.rgerv.snowtamproject.Utils.AirportInfoRetrieving;
import com.example.rgerv.snowtamproject.Utils.AirportSnowTamRetrieving;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ImageButton validate;
    EditText searchCode;
    Toast infos ;
    Context context;
    CharSequence msg;
    int duration;
    LinearLayout layout;
    private String DebugTag = "Debug-MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        context = this;
        duration = Toast.LENGTH_LONG;
        AirportList.getInstance().resetAirportList();

        searchCode =  findViewById(R.id.search_code);
        validate =  findViewById(R.id.validate);
        layout =  findViewById(R.id.layout2);
        fab = findViewById(R.id.floatingActionButton);


        validate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!searchCode.getText().toString().equals("")) {
                            //we have to check the airplane's existence
                            searchAirportLocation();
                        } else {
                            msg = getString(R.string.code_missing);
                            infos = Toast.makeText(context, msg, duration);
                            infos.show();
                        }
                    }
                }
        );

        fab.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if(AirportList.getInstance().getAirportList().size()>0) {
                       Intent intent = new Intent(context, DisplayActivity.class);
                       intent.putExtra("airportCode", 0);
                       startActivity(intent);
                   }
                   else{
                       Toast.makeText(context ,context.getString(R.string.no_aiport_added), Toast.LENGTH_SHORT).show();
                   }
               }
           }
        );

    }

    public void searchAirportLocation(){
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    if(response.length() == 0){
                        Log.d(DebugTag, "No airport Found");
                        //affichage d'un message pour l'utilisateur
                        msg = getString(R.string.incorrect_code);
                        infos = Toast.makeText(context, msg, duration);
                        infos.show();
                    }
                    else{
                        JSONObject airport = response.getJSONObject(0);
                        Log.d(DebugTag, airport.toString());

                        Airport a = new Airport(airport);

                        Log.d(DebugTag, a.toString());
                        layout.addView(addCde(a.getIcaoCode() + " " + a.getStateName()));
                        AirportList.getInstance().getAirportList().add(a);
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
        AirportInfoRetrieving.RetrieveInformation(searchCode.getText().toString().trim(), this, responseListener, errorListener);
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
                    snowtam.decodeSnowTam(airport.getLocation(), context);
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

    private LinearLayout addCde(String s){
        final LinearLayout l = new LinearLayout(context);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.addView(createNewTextView(s));
        l.addView(createNewButton(l));
        l.setGravity(Gravity.CENTER);
        return l;
    }

    private TextView createNewTextView(String s) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,3);
        final TextView airplane_code = new TextView(this);
        airplane_code.setLayoutParams(lparams);
        airplane_code.setText(s);
        return airplane_code;
    }


    private ImageButton createNewButton(final LinearLayout l) {
        final ImageButton airplane_delete = new ImageButton(context);
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        airplane_delete.setImageResource(R.mipmap.ic_delete);
        airplane_delete.setLayoutParams(lparams);
        airplane_delete.setPadding(0,0,0,0);
        airplane_delete.setBackgroundColor(getResources().getColor(R.color.transparent));
        airplane_delete.setMaxHeight(10);
        airplane_delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = layout.indexOfChild(l);
                        l.removeAllViews();
                        layout.removeView(l);
                        Log.d("Debug-", "Click on : " + index);
                        AirportList.getInstance().getAirportList().remove(index);
                    }
                });
        return airplane_delete;
    }

}

