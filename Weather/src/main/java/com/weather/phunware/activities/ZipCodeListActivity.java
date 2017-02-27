package com.weather.phunware.activities;


import android.app.Activity;
import android.os.Bundle;
import com.weather.phunware.fragments.ZipCodeListFragment;
import com.weather.phunware.methods.ReusableMethods;

/**
 * Created by Sreeram on 2/26/17.
 *
 * This is the activity for setting fragment for showing the list of zipcodes
 */

public class ZipCodeListActivity extends Activity {


    ReusableMethods methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zipcode_list);

        methods=new ReusableMethods(getApplicationContext());
        methods.createDatabase();
        if(savedInstanceState==null)
        {
            getFragmentManager().beginTransaction().add(R.id.flFragmentHolder,new ZipCodeListFragment()).commit();
        }
    }




}
