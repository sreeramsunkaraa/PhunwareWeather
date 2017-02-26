package com.weather.phunware.Services;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.weather.phunware.BusinessObjects.WeatherAPIResponse;
import com.weather.phunware.constants.PhunwareWeatherConstants;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by apple on 2/24/17.
 */

public class GSONServiceConnection {



    public static String URLformed(String zipcode)
    {
        return PhunwareWeatherConstants.URL_PART1+""+zipcode+""+PhunwareWeatherConstants.URL_PART2;
    }

    public static WeatherAPIResponse fetchTheResponse(String zipcode)
    {

        try {
            URL url=new URL(URLformed(zipcode));
            Reader readdata=new InputStreamReader(url.openStream());

            WeatherAPIResponse weatherReport=new Gson().fromJson(readdata,WeatherAPIResponse.class);

            return weatherReport;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

       return null;

    }
}
