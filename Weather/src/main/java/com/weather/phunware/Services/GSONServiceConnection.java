package com.weather.phunware.services;

import android.content.Context;

import com.google.gson.Gson;
import com.weather.phunware.businessobjects.WeatherAPIResponse;
import com.weather.phunware.constants.PhunwareWeatherConstants;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Sreeram on 2/26/17.
 *
 * This class is used for getting JSON data from Open Weather MAP
 */

public class GSONServiceConnection {

    PhunwareWeatherConstants constants;

    public GSONServiceConnection()
    {
        constants=new PhunwareWeatherConstants();
    }

    //This method is for framing the URL
    public String URLformed(String zipcode)
    {
        constants=new PhunwareWeatherConstants();
        return constants.URL_PART1+""+zipcode+""+constants.URL_PART2;
    }

    //This method is to get the JSON data from Weather API
    public WeatherAPIResponse fetchTheResponse(String zipcode)
    {

        try {
            URL url=new URL(URLformed(zipcode));
            Reader readdata=new InputStreamReader(url.openStream());
            WeatherAPIResponse weatherReport=new Gson().fromJson(readdata,WeatherAPIResponse.class);
            constants.ERROR_DISPAY=0;
            return weatherReport;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            constants.ERROR_DISPAY= constants.ERROR_UNEXPECTED;
        } catch (IOException e) {
            e.printStackTrace();
            constants.ERROR_DISPAY=constants.ERROR_SERVICE_PROBLEM;
        }

       return null;

    }
}
