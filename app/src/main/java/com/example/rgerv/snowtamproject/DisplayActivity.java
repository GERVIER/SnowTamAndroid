package com.example.rgerv.snowtamproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rgerv.snowtamproject.Controller.DrawerItemAdapter;
import com.example.rgerv.snowtamproject.Model.Airport;
import com.example.rgerv.snowtamproject.Model.AirportList;
import com.example.rgerv.snowtamproject.Model.ItemModel;

import java.util.List;

public class DisplayActivity extends AppCompatActivity {
    private enum codeType {DECODED, CODED};
    private int aiportDisplayId;
    public DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private FloatingActionButton buttonSwitch;
    private codeType codeShowed;
    private RelativeLayout drawerContainer;
    private List<Airport> airportList;
    private TextView airportNameDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display);
        mTitle = mDrawerTitle = getTitle();
        aiportDisplayId = getIntent().getIntExtra("airportCode",0);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);
        drawerContainer = findViewById(R.id.drawerContainer);
        airportNameDisplay = findViewById(R.id.airportName);
        buttonSwitch = findViewById(R.id.encryptedInfo);

        setupToolbar();

        airportList = AirportList.getInstance().getAirportList();
        airportNameDisplay.setText(airportList.get(aiportDisplayId).getSnowtam().getDecodedSnowTam());
        codeShowed = codeType.DECODED;
        ItemModel[] drawerItem = new ItemModel[airportList.size()];

        for(int i = 0; i< airportList.size(); i++){
            drawerItem[i] = new ItemModel(airportList.get(i).getIcaoCode());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemAdapter adapter = new DrawerItemAdapter(this, R.layout.drawer_list_view_item, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        setupDrawerToggle();

        buttonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Debug-DisplayActivity", "fab_info.onclick()\n");
                switch (codeShowed){
                    case CODED:
                        codeShowed = codeType.DECODED;
                        Log.d("Debug-DisplayActivity", "switched to decoded\n");
                        airportNameDisplay.setText(airportList.get(aiportDisplayId).getSnowtam().getDecodedSnowTam());
                        break;
                    case DECODED:
                        codeShowed = codeType.CODED;
                        Log.d("Debug-DisplayActivity", "switched to coded\n");
                        airportNameDisplay.setText(airportList.get(aiportDisplayId).getSnowtam().getCodedSnowTam());
                        break;
                    default:
                        codeShowed = codeType.DECODED;
                        Log.d("Debug-DisplayActivity", "switched to decoded\n");
                        airportNameDisplay.setText(airportList.get(aiportDisplayId).getSnowtam().getDecodedSnowTam());
                }
            }
        });
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {
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
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }
}
