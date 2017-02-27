package com.weather.phunware.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weather.phunware.activities.R;
import com.weather.phunware.constants.PhunwareWeatherConstants;
import com.weather.phunware.methods.ReusableMethods;

import java.util.ArrayList;

/**
 * Created by Sreeram on 2/26/17.
 */

//View Holder for list view items
class Holder
{
    TextView tvZipcodeItem;
    ImageView ivDelete;
}

//Customized adapter for setting the items to ListView
public class ZipCodeListAdapter extends BaseAdapter {


    private LayoutInflater layoutInflater;
    private Context context;
    private PhunwareWeatherConstants constants;
    private Holder holder;
    private ReusableMethods methods;
    private ArrayList<String> columnName=new ArrayList<>();
    private ArrayList<String> columnValue=new ArrayList<>();

    public ZipCodeListAdapter(Context context)
    {
        this.context=context;
        methods=new ReusableMethods(context);
        constants=new PhunwareWeatherConstants();
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return constants.cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {



        if(convertView==null)
        {
            convertView=layoutInflater.inflate(R.layout.item_zipcodes,parent,false);
            holder=new Holder();
            holder.tvZipcodeItem=(TextView)convertView.findViewById(R.id.tvZipCodeItem);
            holder.ivDelete=(ImageView)convertView.findViewById(R.id.ivDelete);
            convertView.setTag(holder);
        }
        else
        {
            holder=(Holder)convertView.getTag();
        }

        constants.cursor.moveToPosition(position);
        holder.tvZipcodeItem.setText(constants.cursor.getString(0));

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(constants.cursor.getCount()>constants.MINIMUM_ZIPCODES) {
                    constants.cursor.moveToPosition(position);
                    columnName.add(constants.COLUMN_NAME_ZIP);
                    columnValue.add(constants.cursor.getString(0));
                    methods.deleteZipCodeFromDB(columnName,columnValue);
                    notifyDataSetInvalidated();
                    notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(context.getApplicationContext(),R.string.nomoredeletion,Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

}
