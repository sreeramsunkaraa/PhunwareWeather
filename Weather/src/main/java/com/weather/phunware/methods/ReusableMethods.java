package com.weather.phunware.methods;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.weather.phunware.activities.R;
import com.weather.phunware.adapters.ZIpCodeSqliteAdapter;
import com.weather.phunware.constants.PhunwareWeatherConstants;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sreeram on 2/26/17.
 *
 * Methods for reusing in the applciation
 */

public class ReusableMethods {

    private PhunwareWeatherConstants constants;
    private ZIpCodeSqliteAdapter dataBaseAdapter;
    private Context context;



    public ReusableMethods(Context context)
    {

        this.context=context;
        constants=new PhunwareWeatherConstants();
        dataBaseAdapter= new ZIpCodeSqliteAdapter(context, constants.DB_NAME, null, constants.DB_VERSION);
    }

    //Creating database
    public void createDatabase()
    {   try {
            dataBaseAdapter.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataBaseAdapter.CREATE(context, constants.TABLE_NAME, constants.COLUMN_NAME_INSERT);
    }

    //delete zip code from the db
    public  void deleteZipCodeFromDB(ArrayList<String> columnName, ArrayList<String> columnValue)
    {
        dataBaseAdapter.DELETE(context, constants.TABLE_NAME,whereCondition(columnName,columnValue) );
        constants.cursor = dataBaseAdapter.SELECT(context.getApplicationContext(), constants.TABLE_NAME, constants.COLUMN_NAME_ZIP);
        columnName.clear();
        columnValue.clear();
    }

    //creating argument list for writing where condition in SQLite
    public String whereCondition(ArrayList<String> columnName,ArrayList<String> columnValue)
    {
        String condition="";
        String equalTo="='";
        String singleQuote="'";
        StringBuilder sb = new StringBuilder();
         for(int i=0;i<=columnName.size()-1;i++)
        {
            if(i>0)
            {
                condition=""+sb.append(",");
            }

            condition=""+sb.append(columnName.get(i)).append("='").append(columnValue.get(i)).append("'");

        }

        return condition;
    }

    //formating text with single quotation
    public String insertSingleQuote(ArrayList<String> columnName,ArrayList<String> columnQuote)
    {
        String condition="";
        String singleQuote="'";
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<=columnName.size()-1;i++)
        {
            if(columnQuote.get(i).equals("N"))
            {
                condition=""+sb.append(columnName.get(i));
                if(i!=columnName.size()-1)
                {
                    condition=""+sb.append(",");
                }

            }
            else
            {
                condition=""+sb.append(singleQuote).append(columnName.get(i)).append(singleQuote);
                if(i!=columnName.size()-1)
                {
                    condition=""+sb.append(",");
                }
            }

        }

        return condition;
    }

    //formating text with column name and single quotation
    public String createUpdateArgs(ArrayList<String> columnName,ArrayList<String> columnValue,ArrayList<String> columnQuote)
    {
        String condition="";
        String singleQuote="'";
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<=columnName.size()-1;i++)
        {
            condition=""+sb.append(columnName.get(i)).append("=");
            if(columnQuote.get(i).equals("N"))
            {
                condition=""+sb.append(columnValue.get(i));
                if(i!=columnName.size()-1)
                {
                    condition=""+sb.append(",");
                }
                Log.e("***->","N"+i+condition);

            }
            else
            {
                condition=""+sb.append(singleQuote).append(columnValue.get(i)).append(singleQuote);
                if(i!=columnName.size()-1)
                {
                    condition=""+sb.append(",");
                }
                Log.e("***->","Y"+i+condition);
            }


        }

        return condition;
    }

    //Error message for the network situations
    public String errorMessage(int errorCode)
    {
        if(errorCode==constants.ERROR_SERVICE_PROBLEM)
        {
           return constants.ERROR_SERVICE_PROBLEM_TEXT;
        }
        if(errorCode==constants.ERROR_UNEXPECTED)
        {
            return constants.ERROR_UNEXPECTED_TEXT;
        }
        return constants.ERROR_COMMON_TEXT;
    }




}
