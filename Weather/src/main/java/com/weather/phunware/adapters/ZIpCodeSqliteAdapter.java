package com.weather.phunware.adapters;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by apple on 2/24/17.
 */

public class ZIpCodeSqliteAdapter extends SQLiteOpenHelper{

    private static String DB_PATH="/data/data/com.weather.phunware.phunwareweather/databases/";
    private static String DB_NAME="zipcode_database";
    private static int DB_VERSION=1;
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public ZIpCodeSqliteAdapter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.e("****","did I enter");
        this.myContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     * */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            // do nothing - database already exist
            Log.e("*****","DB Exist");
        } else {
            // By calling this method and empty database will be created
            // into the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            this.getReadableDatabase();
            Log.e("*****","DB Created");
            try {
                //copyDataBase();

            } catch (Exception e) {
                //throw new Error("Error copying database");
                e.printStackTrace();

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            // database does't exist yet.
        }

		/*if (checkDB != null) {
			checkDB.close();
		}*/
        return checkDB != null ? true : false;
    }


    public void openDataBase() throws SQLException {

        // Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READONLY);

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
        myDataBase.execSQL(query);
        if(myDataBase!=null)
            myDataBase.close();
    }

    public Cursor SELECT(Context context, String TableName, String columns)
    {

        myDataBase = this.getReadableDatabase();
        String query="SELECT "+columns+" FROM "+TableName;
        System.out.println(query);
        Cursor selectionCursor=myDataBase.rawQuery(query, null);

        return selectionCursor;
    }
    public Cursor SELECT(Context context, String TableName,String columns,String whereArgs)
    {

        myDataBase = this.getReadableDatabase();
        String query="SELECT "+columns+" FROM "+TableName+" WHERE "+whereArgs;
        Cursor selectionCursor=myDataBase.rawQuery(query, null);
        return selectionCursor;
    }

    public void UPDATE(Context context, String TableName,String updations,String conditions)
    {

        myDataBase = this.getWritableDatabase();
        String query="UPDATE "+TableName+" SET "+updations+"  WHERE "+conditions;
        myDataBase.execSQL(query);
        if(myDataBase!=null)
            myDataBase.close();
    }
}
