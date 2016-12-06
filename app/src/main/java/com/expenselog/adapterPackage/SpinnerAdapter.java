package com.expenselog.adapterPackage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.expenselog.R;

import java.util.List;

/**
 * Created by siddhant on 11/16/16.
 */
public class SpinnerAdapter extends ArrayAdapter<ItemData> {

    int groupid;
    Activity context;
    List<ItemData> list;
    LayoutInflater inflater;

    public SpinnerAdapter(Activity context, int groupid, int id, List<ItemData>
            list){
        super(context,id,list);
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid=groupid;
    }

    public View getView(int position, View convertView, ViewGroup parent ){
        View itemView=inflater.inflate(groupid,parent,false);
        ImageView imageView=(ImageView)itemView.findViewById(R.id.img);
        imageView.setImageResource(list.get(position).getImageId());
        TextView textView=(TextView)itemView.findViewById(R.id.txt);
        textView.setText(list.get(position).getText());
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup
            parent){
        return getView(position,convertView,parent);

    }

    public ItemData getItem(int position)
    {
        return list.get(position);
    }
}
