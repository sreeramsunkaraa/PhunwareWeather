package com.weather.phunware.activities;


import android.app.Activity;
import android.os.Bundle;

import com.weather.phunware.adapters.ZIpCodeSqliteAdapter;
import com.weather.phunware.constants.PhunwareWeatherConstants;
import com.weather.phunware.fragments.ZipCodeListFragment;

import java.io.IOException;


public class ZipCodeListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zipcode_list);

        ZIpCodeSqliteAdapter dataBaseAdapter = new ZIpCodeSqliteAdapter(getApplicationContext(), PhunwareWeatherConstants.DB_NAME, null, PhunwareWeatherConstants.DB_VERSION);

        try {
            dataBaseAdapter.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataBaseAdapter.CREATE(getApplicationContext(), PhunwareWeatherConstants.TABLE_NAME, PhunwareWeatherConstants.COLUMN_NAME_INSERT);

        if(savedInstanceState==null)
        {
            getFragmentManager().beginTransaction().add(R.id.flFragmentHolder,new ZipCodeListFragment()).commit();
        }
    }




}
