package com.weather.phunware.businessobjects;

import com.weather.phunware.constants.PhunwareWeatherConstants;

import java.util.List;

/**
 * Created by Sreeram on 2/26/17.
 *
 * To describe the structure of the JSON response
 */

public class WeatherAPIResponse {

    static class Weather{
        String description;
        String icon;
    }

    static class Main{
        float temp;
    }

    public String name;

    List<Weather> weather;

    Main main;

    public String getTemperatureInCelsius() {
        float temp = main.temp - 273.15f;
        return String.format("%.2f", temp);
    }

    public String getIconAddress() {
        PhunwareWeatherConstants constants=new PhunwareWeatherConstants();
        return constants.ICON_ADDR + weather.get(0).icon + ".png";
    }

    public String getDescription() {
        if (weather != null && weather.size() > 0)
            return weather.get(0).description;
        return null;
    }

}
