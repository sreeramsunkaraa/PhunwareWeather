package com.weather.phunware.activities;

import android.app.Activity;

import android.os.Bundle;
import android.widget.Toast;

import com.weather.phunware.fragments.SearchandDetailWeatherFragment;
import com.weather.phunware.fragments.ZipCodeListFragment;

public class SearchandDetailWeatherActivity extends Activity {
    Bundle bundle=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_detail_weather);
            if(savedInstanceState==null) {
                SearchandDetailWeatherFragment fragment = new SearchandDetailWeatherFragment();
                getFragmentManager().beginTransaction().add(R.id.flFragmentContainer, fragment).commit();
            }

    }
}
