package com.weather.phunware.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.weather.phunware.activities.R;
import com.weather.phunware.activities.SearchandDetailWeatherActivity;
import com.weather.phunware.adapters.ZIpCodeSqliteAdapter;
import com.weather.phunware.adapters.ZipCodeListAdapter;
import com.weather.phunware.constants.PhunwareWeatherConstants;
import com.weather.phunware.methods.ReusableMethods;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
        * Created by Sreeram on 2/26/17.
        *
        * Fragment to display zip code list
        */

public class ZipCodeListFragment extends Fragment {


    @BindView(R.id.btnAdd)
    Button btnAdd;

    @BindView(R.id.lvZipCodes)
    ListView lvZipCodes;

    PhunwareWeatherConstants constants;
    ReusableMethods methods;

    //Object Initialization
    public ZipCodeListFragment() {
        constants=new PhunwareWeatherConstants();
        methods=new ReusableMethods(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_zipcode_list, container, false);
        ButterKnife.bind(this,view);
        setRetainInstance(true);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        new ZipCodeFetchFromDB().execute();

        lvZipCodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                constants.cursor.moveToPosition(position);
                constants.zipcodeSelected=constants.cursor.getString(0);
                startActivity(new Intent(getActivity(),SearchandDetailWeatherActivity.class));
            }
        });
    }


    @OnClick(R.id.btnAdd)
    public void addNewZipCode() {
       startActivity(new Intent(getActivity(), SearchandDetailWeatherActivity.class));

    }

    //Fetch the zipcode list from SQLite to show on the screen
    public class ZipCodeFetchFromDB extends AsyncTask<Void,Void,Void> {


       ProgressDialog pdLoading;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
           pdLoading = ProgressDialog.show(getActivity(), "", getActivity().getString(R.string.fetching));
        }

        @Override
        protected Void doInBackground(Void... params) {


            ZIpCodeSqliteAdapter dataBaseAdapter = new ZIpCodeSqliteAdapter(getActivity().getApplicationContext(), constants.DB_NAME, null, constants.DB_VERSION);

            constants.cursor = dataBaseAdapter.SELECT(getActivity().getApplicationContext(), constants.TABLE_NAME, constants.COLUMN_NAME_ZIP);
                if(constants.cursor.getCount()<constants.MINIMUM_ZIPCODES)
                {
                    dataBaseAdapter.INSERT(getActivity().getApplicationContext(), constants.TABLE_NAME,"78757,'2000-01-01 01:01:01 AM','','','',''), (92660,'2000-01-01 01:01:01 AM','','','',''), (33137,'2000-01-01 01:01:01 AM','','','',''");
                    constants.cursor = dataBaseAdapter.SELECT(getActivity().getApplicationContext(), constants.TABLE_NAME, constants.COLUMN_NAME_ZIP);

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
