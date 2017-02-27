package com.weather.phunware.businessobjects;

import com.weather.phunware.constants.PhunwareWeatherConstants;

import java.util.List;

/**
 * Created by apple on 2/24/17.
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
