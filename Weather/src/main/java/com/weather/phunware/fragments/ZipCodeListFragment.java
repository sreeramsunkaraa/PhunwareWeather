package com.weather.phunware.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.weather.phunware.activities.R;
import com.weather.phunware.activities.SearchandDetailWeatherActivity;
import com.weather.phunware.adapters.ZIpCodeSqliteAdapter;
import com.weather.phunware.adapters.ZipCodeListAdapter;
import com.weather.phunware.constants.PhunwareWeatherConstants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;


public class ZipCodeListFragment extends Fragment {


    @BindView(R.id.btnAdd)
    Button btnAdd;

    @BindView(R.id.lvZipCodes)
    ListView lvZipCodes;

    public static Cursor cursor;
    public static String zipcodeSelected=null;
     double longitude;
     double latitude;

    String zipcode = "";

    public ZipCodeListFragment() {
        //Default constructor - to instantiated when restoring its activity's state.
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_zipcode_list, container, false);
        ButterKnife.bind(this,view);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        new ZipCodeFetchFromDB().execute();


        lvZipCodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                Intent intent=new Intent(getActivity(),SearchandDetailWeatherActivity.class);
                intent.putExtra("zipcodeselected",cursor.getString(0));
                zipcodeSelected=cursor.getString(0);
                startActivity(intent);
            }
        });
    }


    @OnClick(R.id.btnAdd)
    public void addNewZipCode() {



       startActivity(new Intent(getActivity(), SearchandDetailWeatherActivity.class));

    }


    public class ZipCodeFetchFromDB extends AsyncTask<Void,Void,Void> {


       ProgressDialog pdLoading;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
           pdLoading = ProgressDialog.show(getActivity(), "", "Fetching Data..");
        }

        @Override
        protected Void doInBackground(Void... params) {

            ZIpCodeSqliteAdapter dataBaseAdapter = new ZIpCodeSqliteAdapter(getActivity().getApplicationContext(), PhunwareWeatherConstants.DB_NAME, null, PhunwareWeatherConstants.DB_VERSION);

                cursor = dataBaseAdapter.SELECT(getActivity().getApplicationContext(), PhunwareWeatherConstants.TABLE_NAME, PhunwareWeatherConstants.COLUMN_NAME_ZIP);
                if(cursor.getCount()<3)
                {
                    dataBaseAdapter.INSERT(getActivity().getApplicationContext(), PhunwareWeatherConstants.TABLE_NAME,"60115,'1990-01-01 00:00:00','','','',''), (60116,'1990-01-01 00:00:00','','','',''), (60117,'1990-01-01 00:00:00','','','',''");
                    cursor = dataBaseAdapter.SELECT(getActivity().getApplicationContext(), PhunwareWeatherConstants.TABLE_NAME, PhunwareWeatherConstants.COLUMN_NAME_ZIP);

                }


            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ZipCodeListAdapter zipCodeListAdapter = new ZipCodeListAdapter(getActivity().getApplicationContext());
            lvZipCodes.setAdapter(zipCodeListAdapter);
            zipCodeListAdapter.notifyDataSetInvalidated();
            zipCodeListAdapter.notifyDataSetChanged();
            pdLoading.dismiss();


        }
    }




}
