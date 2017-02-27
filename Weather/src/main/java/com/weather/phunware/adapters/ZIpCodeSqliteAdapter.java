package com.weather.phunware.adapters;


import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.weather.phunware.constants.PhunwareWeatherConstants;

/**
 * Created by Sreeram on 2/26/17.
 *
 * Class for opening SQLite and has the methods to access the tables inside
 */

public class ZIpCodeSqliteAdapter extends SQLiteOpenHelper{

    private SQLiteDatabase myDataBase;
    private Context Context;
    PhunwareWeatherConstants constants;

    public ZIpCodeSqliteAdapter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
        this.Context = context;
        constants=new PhunwareWeatherConstants();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();

        }

    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = constants.DB_PATH + constants.DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

        }

        return checkDB != null ? true : false;
    }


    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }


    public void CREATE(Context context,String TableName,String cols) {

        myDataBase = this.getWritableDatabase();
        String query="CREATE TABLE IF NOT EXISTS "+TableName+" ("+cols+")";
        myDataBase.execSQL(query);
        if(myDataBase!=null)
            myDataBase.close();
    }

    public void INSERT(Context context,String TableName,String values) {

        myDataBase = this.getWritableDatabase();
        String query="INSERT INTO "+TableName+" VALUES ("+values+")";
        myDataBase.execSQL(query);
        if(myDataBase!=null)
            myDataBase.close();
    }

    public void DELETE(Context context, String TableName,String conditions)
    {

        myDataBase = this.getWritableDatabase();
        String query="DELETE FROM "+TableName+" WHERE "+conditions;
        Log.e("**",query);
        myDataBase.execSQL(query);
        if(myDataBase!=null)
            myDataBase.close();
    }

    public Cursor SELECT(Context context, String TableName, String columns)
    {

        myDataBase = this.getReadableDatabase();
        String query="SELECT "+columns+" FROM "+TableName;
        Log.e("**",query);
        System.out.println(query);
        Cursor selectionCursor=myDataBase.rawQuery(query, null);

        return selectionCursor;
    }
    public Cursor SELECT(Context context, String TableName,String columns,String whereArgs)
    {

        myDataBase = this.getReadableDatabase();
        String query="SELECT "+columns+" FROM "+TableName+" WHERE "+whereArgs;
        Log.e("**",query);
        Cursor selectionCursor=myDataBase.rawQuery(query, null);
        return selectionCursor;
    }

    public void UPDATE(Context context, String TableName,String updations,String conditions)
    {

        myDataBase = this.getWritableDatabase();
        String query="UPDATE "+TableName+" SET "+updations+"  WHERE "+conditions;
        Log.e("**",query);
        myDataBase.execSQL(query);
        if(myDataBase!=null)
            myDataBase.close();
    }
}
