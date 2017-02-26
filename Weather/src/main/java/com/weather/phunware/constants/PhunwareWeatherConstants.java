package com.weather.phunware.constants;



public class PhunwareWeatherConstants {

    public static final int ERROR_NO_WEATHER_REPORT=1001;
    public static final int DB_VERSION=1;
    public static final String DB_NAME="zipcode_database";
    public static final String TABLE_NAME="Zipcode";
    public static final String COLUMN_NAME_INSERT="zip int NOT NULL PRIMARY KEY,lastupdatedon datetime,placename varchar(255),description varchar(255),tempinc varchar(255),icon varchar(255)";
    public static final String COLUMN_NAME_ZIP="zip";
    public static final String COLUMN_NAME_LASTUPDATEDON="lastupdatedon";
    public static final String COLUMN_NAME_PLACENAME="placename";
    public static final String COLUMN_NAME_DESCRIPTION="description";
    public static final String COLUMN_NAME_TEMPINC="tempinc";
    public static final String COLUMN_NAME_ICON="icon";
    public static final String URL_PART1="http://api.openweathermap.org/data/2.5/weather?zip=";
    public static final String URL_PART2="&APPID=1efb80148abe3eb37cd51de8b62175ea";
    public final static String ICON_ADDR = "http://openweathermap.org/img/w/";

}
