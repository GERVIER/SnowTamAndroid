package com.example.rgerv.snowtamproject;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.rgerv.snowtamproject.Model.AirportInfoRetrieving;
import com.example.rgerv.snowtamproject.Model.AirportList;
import com.example.rgerv.snowtamproject.Model.Airport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);

        context = this;
        duration = Toast.LENGTH_LONG;
        msg = getString(R.string.code_missing);

        searchCode = (EditText) findViewById(R.id.search_code);
        validate = (ImageButton) findViewById(R.id.validate);
        layout = (LinearLayout) findViewById(R.id.layout2);

        validate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!searchCode.getText().toString().equals("")) {
                            //we have to check the airplane's existence
                            layout.addView(addCde(searchCode.getText().toString()));

                        } else {
                            infos = Toast.makeText(context, msg, duration);
                            infos.show();
                        }
                    }
                }
        );
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
                        JSONObject airport = response.getJSONObject(0);
                        Log.d(DebugTag, airport.toString());

                        Airport a = new Airport(airport);

                        Log.d(DebugTag, a.toString());

                        AirportList.getInstance().getAirportList().add(a);
                        Log.d(DebugTag, "Airport n° " + AirportList.getInstance().getAirportList().size() + " added !");
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

        AirportInfoRetrieving.RetrieveInformation("ENBO", this, responseListener, errorListener);
    }

    private LinearLayout addCde(String s){
        final LinearLayout l = new LinearLayout(context);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.addView(createNewTextView(s));
        l.addView(createNewButton(l));
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
                        l.removeAllViews();
                    }
                });
        return airplane_delete;
    }}

