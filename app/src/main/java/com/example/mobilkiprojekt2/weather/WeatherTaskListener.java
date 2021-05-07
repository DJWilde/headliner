package com.example.mobilkiprojekt2.weather;

public interface WeatherTaskListener {
    void onWeatherPreExecute();
    void onWeatherPostExecute(Weather weather);
}
