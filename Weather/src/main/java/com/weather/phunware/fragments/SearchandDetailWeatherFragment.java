package com.weather.phunware.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.squareup.picasso.Picasso;
import com.weather.phunware.businessobjects.WeatherAPIResponse;
import com.weather.phunware.methods.ReusableMethods;
import com.weather.phunware.services.GSONServiceConnection;
import com.weather.phunware.activities.R;
import com.weather.phunware.adapters.ZIpCodeSqliteAdapter;
import com.weather.phunware.constants.PhunwareWeatherConstants;


import java.text.ParseException;
import java.util.ArrayList;
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

    String oldDate;
    String newDate;
    String ZIPCODE;
    String zip;

    boolean serviceCalled=false;
    boolean isRefreshed=false;
    boolean alreadyExist=false;

    private Boolean dialogShownOnceFullName = false;
    private Dialog mdialog;
    static ProgressDialog pdLoading;


    Cursor cursor;
    WeatherAPIResponse weatherAPIResponse;
    PhunwareWeatherConstants constants;
    GSONServiceConnection gsonServiceConnection;
    ReusableMethods methods;
    ZIpCodeSqliteAdapter dataBaseAdapter;

    ArrayList<String> columnName=new ArrayList<>();
    ArrayList<String> columnValue=new ArrayList<>();
    ArrayList<String> columnQuote=new ArrayList<>();

    public SearchandDetailWeatherFragment()
    {
        constants=new PhunwareWeatherConstants();
        gsonServiceConnection=new GSONServiceConnection();
        methods=new ReusableMethods(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_and_detail_weather, container, false);
        ButterKnife.bind(this,view);

        setRetainInstance(true);
         if(constants.zipcodeSelected!=null)
        {
            edtZipcodeSearch.setText(constants.zipcodeSelected);
            edtZipcodeSearch.setSelection(edtZipcodeSearch.getText().length());
            constants.zipcodeSelected=null;
        }

        if(edtZipcodeSearch.getText().toString().length()==constants.zipCodeLength)
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
                if(s.length()==constants.zipCodeLength)
                {
                    searchZipCode();
                }
            }
        });



        return view;
    }

    @OnClick(R.id.ivRefresh)
    public void refreshTemp()
    {

        isRefreshed=true;
        searchZipCode();
    }


    private void searchZipCode()
    {
        resetView();
        keyBoardHide(edtZipcodeSearch);
        ZIPCODE=edtZipcodeSearch.getText().toString();
        new FetchResponse().execute();

    }

    private void resetView()
    {
        tvPlaceName.setText(getString(R.string.placename));
        tvTemp.setText(getString(R.string.tempinc));
        tvDescription.setText(getString(R.string.description));
        tvLastUpdatedOn.setText(getString(R.string.lastupdatedon));
    }

    private void keyBoardHide(View view)
    {
        InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(view.getWindowToken(),0);
    }



    private boolean forceServiceCall()
    {
        if(oldDate!=null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(constants.dateTimeFormat);
            try {
                Date date1 = sdf.parse(oldDate);
                Date date2 = new Date();
                sdf.format(date2);
                long elapsedseconds = (date2.getTime() - date1.getTime())/1000;
                if (elapsedseconds > constants.durationInSec) {
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void getLastUpdatedDate(Cursor cursor)
    {
        if(cursor!=null) {
            cursor.moveToFirst();
            oldDate = cursor.getString(cursor.getColumnIndex(constants.COLUMN_NAME_LASTUPDATEDON));
        }
    }

    private void showAlertDialogs()
    {
        AlertDialog.Builder alertbuilder=new AlertDialog.Builder(getActivity());
        alertbuilder.setTitle(methods.errorMessage(constants.ERROR_DISPAY));
        alertbuilder.setPositiveButton("OK", null);
        alertbuilder.setCancelable(true);
        try {
            mdialog = alertbuilder.create();
            if (!mdialog.isShowing() && !dialogShownOnceFullName) {
                mdialog.show();
                dialogShownOnceFullName = true;
            }

            mdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dialogShownOnceFullName = false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appendDataToView(WeatherAPIResponse weatherAPIResponse)
    {
        tvDescription.append(weatherAPIResponse.getDescription());
        tvTemp.append(weatherAPIResponse.getTemperatureInCelsius() + "°C");
        tvPlaceName.append(weatherAPIResponse.name);
        Picasso.with(getActivity()).load(weatherAPIResponse.getIconAddress()).into(ivIconForWeather);
        Date d = new Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(constants.dateTimeFormat);
        newDate = sdf.format(d);
        tvLastUpdatedOn.append(newDate.toString());
    }

    private void insertIntoTable()
    {
        columnName.add(ZIPCODE);
        columnName.add(newDate);
        columnName.add(weatherAPIResponse.name);
        columnName.add(weatherAPIResponse.getDescription());
        columnName.add(weatherAPIResponse.getTemperatureInCelsius());
        columnName.add(weatherAPIResponse.getIconAddress());

        columnQuote.add("N");
        columnQuote.add("Y");
        columnQuote.add("Y");
        columnQuote.add("Y");
        columnQuote.add("Y");
        columnQuote.add("Y");
    }

    private void updateToTable()
    {
        columnName.add(constants.COLUMN_NAME_ZIP);
        columnName.add(constants.COLUMN_NAME_LASTUPDATEDON);
        columnName.add(constants.COLUMN_NAME_PLACENAME);
        columnName.add(constants.COLUMN_NAME_DESCRIPTION);
        columnName.add(constants.COLUMN_NAME_TEMPINC);
        columnName.add(constants.COLUMN_NAME_ICON);

        columnValue.add(ZIPCODE);
        columnValue.add(newDate);
        columnValue.add(weatherAPIResponse.name);
        columnValue.add(weatherAPIResponse.getDescription());
        columnValue.add(weatherAPIResponse.getTemperatureInCelsius());
        columnValue.add(weatherAPIResponse.getIconAddress());

        columnQuote.add("N");
        columnQuote.add("Y");
        columnQuote.add("Y");
        columnQuote.add("Y");
        columnQuote.add("Y");
        columnQuote.add("Y");
    }

    private void clearArrayList()
    {
        columnValue.clear();
        columnName.clear();
        columnQuote.clear();
    }

    private void appendDataFromCursor(Cursor cursor)
    {
        cursor.moveToFirst();
        tvDescription.append(cursor.getString(cursor.getColumnIndex(constants.COLUMN_NAME_DESCRIPTION)));
        tvTemp.append(cursor.getString(cursor.getColumnIndex(constants.COLUMN_NAME_TEMPINC)) + "°C");
        tvPlaceName.append(cursor.getString(cursor.getColumnIndex(constants.COLUMN_NAME_PLACENAME)));
        tvLastUpdatedOn.append(cursor.getString(cursor.getColumnIndex(constants.COLUMN_NAME_LASTUPDATEDON)));
        oldDate = cursor.getString(cursor.getColumnIndex(constants.COLUMN_NAME_LASTUPDATEDON));
        Picasso.with(getActivity()).load(cursor.getString(cursor.getColumnIndex(constants.COLUMN_NAME_ICON))).into(ivIconForWeather);

    }

    public class FetchResponse extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading=ProgressDialog.show(getActivity(),"",getActivity().getString(R.string.loading));
        }

        @Override
        protected Void doInBackground(Void... params) {
            dataBaseAdapter = new ZIpCodeSqliteAdapter(getActivity().getApplicationContext(), constants.DB_NAME,null,constants.DB_VERSION);

            columnName.add(constants.COLUMN_NAME_ZIP);
            columnValue.add(ZIPCODE);

            cursor = dataBaseAdapter.SELECT(getActivity().getApplicationContext(), constants.TABLE_NAME, "*", methods.whereCondition(columnName,columnValue));

            clearArrayList();

            if(cursor.getCount()>0) {
                getLastUpdatedDate(cursor);
            }

            boolean forceCall= forceServiceCall();
            if(forceCall || cursor.getCount()==0 || isRefreshed ) {
                weatherAPIResponse= gsonServiceConnection.fetchTheResponse(ZIPCODE);
                serviceCalled=true;
                if(cursor.getCount()==0) {
                    alreadyExist = false;
                }else {
                    alreadyExist=true;
                }
            }
            else {
                alreadyExist=true;
                serviceCalled=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(constants.ERROR_DISPAY!=0)
            {
                if(pdLoading!=null) {
                    pdLoading.dismiss();
                }
                showAlertDialogs();
            }
            else {
                if (!alreadyExist) {
                   appendDataToView(weatherAPIResponse);

                    insertIntoTable();


                    dataBaseAdapter.INSERT(getActivity().getApplicationContext(), constants.TABLE_NAME, methods.insertSingleQuote(columnName,columnQuote));

                    clearArrayList();

                } else if (forceServiceCall() || isRefreshed) {
                    isRefreshed = false;
                    resetView();
                   appendDataToView(weatherAPIResponse);

                    updateToTable();

                    dataBaseAdapter.UPDATE(getActivity().getApplicationContext(), constants.TABLE_NAME, methods.createUpdateArgs(columnName,columnValue,columnQuote), constants.COLUMN_NAME_ZIP + "=" + ZIPCODE);

                    clearArrayList();

                } else {
                    appendDataFromCursor(cursor);
                }
                if(pdLoading!=null) {
                    pdLoading.dismiss();
                }
            }


        }
    }
}
