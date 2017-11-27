package com.example.rgerv.snowtamproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.rgerv.snowtamproject.Controller.DrawerItemAdapter;
import com.example.rgerv.snowtamproject.Model.Airport;
import com.example.rgerv.snowtamproject.Model.AirportList;
import com.example.rgerv.snowtamproject.Model.ItemModel;
import com.example.rgerv.snowtamproject.Model.SnowTam;
import com.example.rgerv.snowtamproject.Utils.AirportInfoRetrieving;
import com.example.rgerv.snowtamproject.Utils.AirportSnowTamRetrieving;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayActivity extends AppCompatActivity {

    Context context;
    private int aiportDisplayId;
    public DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout drawerContainer;
    private List<Airport> airportList;
    private TextView airportNameDisplay;
    public static ArrayList<ItemModel> drawerItem;
    public static DrawerItemAdapter adapter;

    private ImageButton dValidate;
    private EditText searchCode;
    private int duration;
    CharSequence msg;
    Toast infos ;
    LinearLayout layout;

    public static Activity display_activity;
    public static int currentId;

    private String DebugTag = "Debug-DisplayActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        display_activity = this;
        mTitle = mDrawerTitle = getTitle();
        aiportDisplayId = getIntent().getIntExtra("airportCode",0);
        currentId = aiportDisplayId;
        duration = Toast.LENGTH_LONG;
        context = this;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);
        drawerContainer = findViewById(R.id.drawerContainer);
        airportNameDisplay = findViewById(R.id.airportName);
        dValidate = findViewById(R.id.validate);
        searchCode = findViewById(R.id.search_code);

        setupToolbar();

        airportList = AirportList.getInstance().getAirportList();
        airportNameDisplay.setText(airportList.get(aiportDisplayId).getIcaoCode());
        drawerItem = new ArrayList<>();

        for(int i = 0; i< airportList.size(); i++){
            drawerItem.add(new ItemModel(airportList.get(i).getIcaoCode()));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        adapter = new DrawerItemAdapter(context, R.layout.drawer_list_view_item, drawerItem, mDrawerList);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawerToggle();
        mDrawerLayout.addDrawerListener(mDrawerToggle);


        dValidate.setOnClickListener(
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
    }

    public void searchAirportLocation(){
        //TODO LINK WITH THE LIST OF AIRPORT.
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
                        //layout.addView(addCde(a.getIcaoCode() + " " + a.getStateName()));
                        AirportList.getInstance().getAirportList().add(a);
                        addItems();
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

    public void addItems() {
        drawerItem.add(new ItemModel(airportList.get(airportList.size()-1).getIcaoCode()));
        adapter.notifyDataSetChanged();
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }
    //probably no longer called, if so could be deleted, same as above ^
    public void selectItem(int position) {
        Log.d("Debug-", "Click on item" + position);
        Intent intent = new Intent(DisplayActivity.this, DisplayActivity.class);
        intent.putExtra("airportCode", position);
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            //setTitle(mNavigationDrawerItemIDs[position]);
            mDrawerLayout.closeDrawer(drawerContainer);
            //To prevent endless previous activities of display and instead return directly to homeActivity
            finish();
            startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name)/*{
            public void onDrawerOpened(View drawerView) {
                mDrawerList.bringToFront(); //to solve the ItemClickListener not working problem
                Log.d("Debug-", "Set to front");
                mDrawerLayout.requestLayout();
            }
        }*/;
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }


}
