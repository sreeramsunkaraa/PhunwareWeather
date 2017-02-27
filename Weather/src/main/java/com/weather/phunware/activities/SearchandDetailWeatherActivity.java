package com.weather.phunware.activities;

import android.app.Activity;

import android.os.Bundle;


import com.weather.phunware.fragments.SearchandDetailWeatherFragment;

public class SearchandDetailWeatherActivity extends Activity {

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
