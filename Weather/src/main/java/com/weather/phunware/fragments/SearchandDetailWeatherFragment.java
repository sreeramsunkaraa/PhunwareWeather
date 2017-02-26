package com.weather.phunware.fragments;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.weather.phunware.BusinessObjects.WeatherAPIResponse;
import com.weather.phunware.Services.GSONServiceConnection;
import com.weather.phunware.activities.R;
import com.weather.phunware.adapters.ZIpCodeSqliteAdapter;
import com.weather.phunware.constants.PhunwareWeatherConstants;


import java.text.ParseException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchandDetailWeatherFragment extends Fragment {

    @BindView(R.id.ivRefresh)
    ImageView ivRefresh;

    @BindView(R.id.edtZipcodeSearch)
    EditText edtZipcodeSearch;

    @BindView(R.id.tvDescription)
    TextView tvDescription;

    @BindView(R.id.tvTemp)
    TextView tvTemp;

    @BindView(R.id.ivIconForWeather)
    ImageView ivIconForWeather;

    @BindView(R.id.tvPlaceName)
    TextView tvPlaceName;

    @BindView(R.id.tvLastUpdatedOn)
    TextView tvLastUpdatedOn;

    String cs;
    String ZIPCODE;
    boolean serviceCalled=false, isRefreshed=false;

    public SearchandDetailWeatherFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_and_detail_weather, container, false);
        ButterKnife.bind(this,view);
         if(ZipCodeListFragment.zipcodeSelected!=null)
        {
            edtZipcodeSearch.setText(ZipCodeListFragment.zipcodeSelected);
            edtZipcodeSearch.setSelection(edtZipcodeSearch.getText().length());
            ZipCodeListFragment.zipcodeSelected=null;
        }

        if(edtZipcodeSearch.getText().toString().length()==5)
        {
            searchZipCode();
        }
        edtZipcodeSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==5)
                {
                    searchZipCode();
                }
            }
        });



        return view;
    }


    @OnClick(R.id.ivRefresh)
    public void searchZipCode()
    {
        tvPlaceName.setText(R.string.placename);
        tvTemp.setText(R.string.tempinc);
        tvDescription.setText(R.string.description);
        tvLastUpdatedOn.setText("Last updated on : ");
        InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(edtZipcodeSearch.getWindowToken(),0);
        ZIPCODE=edtZipcodeSearch.getText().toString();
        new FetchResponse().execute();
        isRefreshed=true;
    }

    public boolean forceServiceCall()
    {
        if(cs!=null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyy-MM-dd hh:mm:ss");
            try {
                Date d1 = sdf.parse(cs);
                Date d2 = new Date();
                sdf.format(d2);
                long l = (d2.getTime() - d1.getTime()) / 1000;
                if (l > 180) {


                    //Toast.makeText(getActivity().getApplicationContext(),"true",Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                   // Toast.makeText(getActivity().getApplicationContext(),"false",Toast.LENGTH_SHORT).show();

                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return false;
    }
    public class FetchResponse extends AsyncTask<Void,Void,Void>
    {
        WeatherAPIResponse weatherAPIResponse;
        ProgressDialog pdLoading;
        boolean alreadyExist=false;
        ZIpCodeSqliteAdapter dataBaseAdapter;
        String zip;
        Cursor c;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           pdLoading=ProgressDialog.show(getActivity(),"","Loading");
            zip=edtZipcodeSearch.getText().toString();

        }

        @Override
        protected Void doInBackground(Void... params) {




            dataBaseAdapter = new ZIpCodeSqliteAdapter(getActivity().getApplicationContext(), PhunwareWeatherConstants.DB_NAME,null,PhunwareWeatherConstants.DB_VERSION);
            c=dataBaseAdapter.SELECT(getActivity().getApplicationContext(),PhunwareWeatherConstants.TABLE_NAME,"*",PhunwareWeatherConstants.COLUMN_NAME_ZIP+"='"+zip+"'");
            if(c.getCount()>0) {
                c.moveToFirst();
                int last = c.getColumnIndex(PhunwareWeatherConstants.COLUMN_NAME_LASTUPDATEDON);
                cs = c.getString(last);
            }
            boolean forceCall= forceServiceCall();
            if(forceCall || c.getCount()==0 || isRefreshed ) {
                weatherAPIResponse= GSONServiceConnection.fetchTheResponse(ZIPCODE);
                serviceCalled=true;
                if(c.getCount()==0) {
                    alreadyExist = false;
                }else
                {
                    alreadyExist=true;
                }

            }
            else
            {


                alreadyExist=true;
                serviceCalled=false;
            }





            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(!alreadyExist) {
                tvDescription.append(weatherAPIResponse.getDescription());
                tvTemp.append(weatherAPIResponse.getTemperatureInCelsius() + "°C");
                tvPlaceName.append(weatherAPIResponse.name);
                Picasso.with(getActivity()).load(weatherAPIResponse.getIconAddress()).into(ivIconForWeather);
                Date d = new Date();
                java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyy-MM-dd hh:mm:ss");
                CharSequence s = sdf.format(d);
                tvLastUpdatedOn.append(s.toString());
                dataBaseAdapter.INSERT(getActivity().getApplicationContext(), PhunwareWeatherConstants.TABLE_NAME, ZIPCODE+",'"+s+"','"+weatherAPIResponse.name+"','"+weatherAPIResponse.getDescription()+"','"+weatherAPIResponse.getTemperatureInCelsius()+"','"+weatherAPIResponse.getIconAddress()+"'");

            }
            else if(forceServiceCall() || isRefreshed)
            {
                isRefreshed=false;
                tvPlaceName.setText(R.string.placename);
                tvTemp.setText(R.string.tempinc);
                tvDescription.setText(R.string.description);
                tvLastUpdatedOn.setText(R.string.lastupdatedon);

                tvDescription.append(weatherAPIResponse.getDescription());
                tvTemp.append(weatherAPIResponse.getTemperatureInCelsius() + "°C");
                tvPlaceName.append(weatherAPIResponse.name);
                Picasso.with(getActivity()).load(weatherAPIResponse.getIconAddress()).into(ivIconForWeather);
                Date d = new Date();
                java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyy-MM-dd hh:mm:ss");
                CharSequence s = sdf.format(d);
                tvLastUpdatedOn.append(s.toString());
                dataBaseAdapter.UPDATE(getActivity().getApplicationContext(), PhunwareWeatherConstants.TABLE_NAME, PhunwareWeatherConstants.COLUMN_NAME_ZIP+"="+ZIPCODE+", "+PhunwareWeatherConstants.COLUMN_NAME_LASTUPDATEDON +"='"+s+"', "+PhunwareWeatherConstants.COLUMN_NAME_PLACENAME+"='"+weatherAPIResponse.name+"', "+PhunwareWeatherConstants.COLUMN_NAME_DESCRIPTION+"='"+weatherAPIResponse.getDescription()+"',"+PhunwareWeatherConstants.COLUMN_NAME_TEMPINC+"='"+weatherAPIResponse.getTemperatureInCelsius()+"',"+PhunwareWeatherConstants.COLUMN_NAME_ICON+"='"+weatherAPIResponse.getIconAddress()+"'",PhunwareWeatherConstants.COLUMN_NAME_ZIP+"="+ZIPCODE);
                }
            else {

                int icon=c.getColumnIndex(PhunwareWeatherConstants.COLUMN_NAME_ICON);
                int place=c.getColumnIndex(PhunwareWeatherConstants.COLUMN_NAME_PLACENAME);
                int temp=c.getColumnIndex(PhunwareWeatherConstants.COLUMN_NAME_TEMPINC);
                int desc=c.getColumnIndex(PhunwareWeatherConstants.COLUMN_NAME_DESCRIPTION);
                int last=c.getColumnIndex(PhunwareWeatherConstants.COLUMN_NAME_LASTUPDATEDON);

                c.moveToFirst();
                tvDescription.append(c.getString(desc));
                tvTemp.append(c.getString(temp) + "°C");
                tvPlaceName.append(c.getString(place));
                Picasso.with(getActivity()).load(c.getString(icon)).into(ivIconForWeather);
                tvLastUpdatedOn.append(c.getString(last));

                 cs=c.getString(last);

            }
            pdLoading.dismiss();

            }
    }
}
