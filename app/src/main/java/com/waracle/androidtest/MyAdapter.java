package com.waracle.androidtest;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by cbnewham on 14/03/2018.
 */
class MyAdapter extends BaseAdapter
{
    // Can you think of a better way to represent these items???

    // The JSON should really be decoded into proper objects (I'd use GSON to do this).
    // If there are a huge number of items they'd be better off in a database backed with an ORM
    // such as GreenDAO.
    private JSONArray mItems;
    private LayoutInflater inflater;


    public MyAdapter()
    {
        this(new JSONArray());
    }


    public MyAdapter(JSONArray items)
    {
        inflater = LayoutInflater.from(MyApplication.getContext());
        mItems = items;
    }


    @Override
    public int getCount()
    {
        return mItems.length();
    }


    @Override
    public Object getItem(int position)
    {
        try
        {
            return mItems.getJSONObject(position);
        }
        catch (JSONException e)
        {
            Log.e("", e.getMessage());
        }
        return null;
    }


    @Override
    public long getItemId(int position)
    {
        return 0;
    }


    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder = null;

        if (convertView != null)
        {
            holder = (Holder) convertView.getTag();
        }
        else
        {
            convertView = inflater.inflate(R.layout.list_item_layout, parent,
                    false);
        }

        if (holder == null)
        {
            // Save these in a holder for better performance.
            holder = new Holder();
            holder.title = convertView.findViewById(R.id.title);
            holder.desc = convertView.findViewById(R.id.desc);
            holder.image = convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }

        try
        {
            JSONObject object = (JSONObject) getItem(position);
            holder.title.setText(object.getString("title"));
            holder.desc.setText(object.getString("desc"));

            String url = object.getString("image");

            // Load image.
            holder.image.loadFromURL(url);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        return convertView;
    }

    public void setItems(JSONArray items)
    {
        mItems = items;
    }


    private static class Holder
    {
        public TextView title;
        public TextView desc;
        public MyImageView image;
    }
}
