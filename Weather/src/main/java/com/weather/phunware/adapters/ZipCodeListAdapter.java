package com.weather.phunware.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weather.phunware.activities.R;
import com.weather.phunware.constants.PhunwareWeatherConstants;
import com.weather.phunware.fragments.ZipCodeListFragment;

class Holder
{
    TextView tvZipcodeItem;
    ImageView ivDelete;
}
public class ZipCodeListAdapter extends BaseAdapter {

    Holder holder;
    LayoutInflater layoutInflater;
    Context context;

    public ZipCodeListAdapter(Context context)
    {
        layoutInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.context=context;
    }
    @Override
    public int getCount() {
        return ZipCodeListFragment.cursor.getCount();
        //return 10;
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
        ZipCodeListFragment.cursor.moveToPosition(position);
        holder.tvZipcodeItem.setText(ZipCodeListFragment.cursor.getString(0));

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ZipCodeListFragment.cursor.getCount()>3) {
                    ZipCodeListFragment.cursor.moveToPosition(position);
                    ZIpCodeSqliteAdapter dataBaseAdapter = new ZIpCodeSqliteAdapter(context.getApplicationContext(), PhunwareWeatherConstants.DB_NAME, null, PhunwareWeatherConstants.DB_VERSION);

                    dataBaseAdapter.DELETE(context.getApplicationContext(), PhunwareWeatherConstants.TABLE_NAME, PhunwareWeatherConstants.COLUMN_NAME_ZIP + "='" + ZipCodeListFragment.cursor.getString(0) + "'");
                    ZipCodeListFragment.cursor = dataBaseAdapter.SELECT(context.getApplicationContext(), PhunwareWeatherConstants.TABLE_NAME, PhunwareWeatherConstants.COLUMN_NAME_ZIP);

                    notifyDataSetChanged();
                    notifyDataSetInvalidated();
                }
                else
                {
                    Toast.makeText(context.getApplicationContext(),"Minimum three to delete",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }
}
