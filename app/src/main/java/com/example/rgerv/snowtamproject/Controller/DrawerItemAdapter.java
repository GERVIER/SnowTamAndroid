package com.example.rgerv.snowtamproject.Controller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rgerv.snowtamproject.Model.ItemModel;
import com.example.rgerv.snowtamproject.R;

/**
 * Created by Elisabeth on 20/11/2017.
 * Provides data to DisplayActivity
 */

public class DrawerItemAdapter extends ArrayAdapter<ItemModel>{

        Context mContext;
        int layoutResourceId;
        ItemModel data[] = null;

    public DrawerItemAdapter(Context mContext, int layoutResourceId, ItemModel[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);
        ItemModel folder = data[position];


        textViewName.setText(folder.name);

        return listItem;
    }
}
