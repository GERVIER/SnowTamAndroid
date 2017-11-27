package com.example.rgerv.snowtamproject.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rgerv.snowtamproject.DisplayActivity;
import com.example.rgerv.snowtamproject.MainActivity;
import com.example.rgerv.snowtamproject.Model.AirportList;
import com.example.rgerv.snowtamproject.Model.ItemModel;
import com.example.rgerv.snowtamproject.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.rgerv.snowtamproject.R.color.colorAccent;

/**
 * Created by Elisabeth on 20/11/2017.
 * Provides data to DisplayActivity
 */

public class DrawerItemAdapter extends ArrayAdapter<ItemModel>{

        Context mContext;
        int layoutResourceId;
        ArrayList<ItemModel> data = null;
        ListView drawerList;

    public DrawerItemAdapter(Context mContext, int layoutResourceId, ArrayList<ItemModel> data, ListView drawerList) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
        this.drawerList = drawerList;
    }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);
        ImageButton deleteB = (ImageButton) listItem.findViewById(R.id.deleteViewItem);
        deleteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("Debug-", "Click on delete : " + position);
                AirportList.getInstance().getAirportList().remove(position);
                DisplayActivity.drawerItem.remove(position);
                DisplayActivity.adapter.notifyDataSetChanged();
                /*Go back to new mainActivity if all are deleted*/
                if(AirportList.getInstance().getAirportList().isEmpty()){
                    Intent intent = new Intent(mContext, MainActivity.class);
                    DisplayActivity.display_activity.finish();
                    MainActivity.main_activity.finish();
                    mContext.startActivity(intent);
                }
                /*If deleted airport was displayed, reload activity*/
                else if(position == DisplayActivity.currentId){
                    Intent intent = new Intent(mContext, DisplayActivity.class);
                    intent.putExtra("airportCode", 0);
                    DisplayActivity.display_activity.finish();
                    mContext.startActivity(intent);
                }

            }
        });

        if(position ==DisplayActivity.currentId)
            textViewName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        else {
                textViewName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d("Debug-", "Click on text : " + position);
                        Intent intent = new Intent(mContext, DisplayActivity.class);
                        intent.putExtra("airportCode", position);

                        drawerList.setItemChecked(position, true);
                        drawerList.setSelection(position);

                        DisplayActivity.display_activity.finish();
                        mContext.startActivity(intent);
                    }
                });
            }
        ItemModel folder = data.get(position);


        textViewName.setText(folder.name);


        return listItem;
    }
}
