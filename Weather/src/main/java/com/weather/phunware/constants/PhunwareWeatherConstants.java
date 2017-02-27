package com.weather.phunware.constants;


import android.database.Cursor;

/**
 * Created by Sreeram on 2/26/17.
 *
 * Various constants that are used inside the project
 */
public class PhunwareWeatherConstants {


    public final String COLUMN_NAME_ZIP="zip";
    public final String COLUMN_NAME_LASTUPDATEDON="lastupdatedon";
    public final String COLUMN_NAME_PLACENAME="placename";
    public final String COLUMN_NAME_DESCRIPTION="description";
    public final String COLUMN_NAME_TEMPINC="tempinc";
    public final String COLUMN_NAME_ICON="icon";
    public final String COLUMN_NAME_INSERT=COLUMN_NAME_ZIP+" int NOT NULL PRIMARY KEY,"+COLUMN_NAME_LASTUPDATEDON+" datetime,placename varchar(255),"+COLUMN_NAME_DESCRIPTION+" varchar(255),"+COLUMN_NAME_TEMPINC+" varchar(255),"+COLUMN_NAME_ICON+" varchar(255)";

    public final String URL_PART1="http://api.openweathermap.org/data/2.5/weather?zip=";
    public final String URL_PART2="&APPID=1efb80148abe3eb37cd51de8b62175ea";
    public final String ICON_ADDR = "http://openweathermap.org/img/w/";


    public static int ERROR_DISPAY=0;
    public final int ERROR_UNEXPECTED=1001;
    public final int ERROR_SERVICE_PROBLEM=1002;
    public final String ERROR_UNEXPECTED_TEXT="Unexpected error, please try after sometime.";
    public final String ERROR_SERVICE_PROBLEM_TEXT="Unable to connect.";
    public final String ERROR_COMMON_TEXT="Please try after sometime.";

    public final String DB_PATH="/data/data/com.weather.phunware.phunwareweather/databases/";
    public final String DB_NAME="zipcode_database";
    public final String TABLE_NAME="Zipcode";
    public final int DB_VERSION=1;

    public static String zipcodeSelected=null;
    public static Cursor cursor;

    public final String dateTimeFormat="yyyy-MM-dd hh:mm:ss aa";

    public final int zipCodeLength=5;
    public final int durationInSec=180;
    public final int MINIMUM_ZIPCODES=3;

}
