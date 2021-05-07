package com.example.mobilkiprojekt2.weather;

import android.os.AsyncTask;

public class FetchWeatherTask extends AsyncTask<String, Void, Weather> {
    private WeatherTaskListener listener;

    public FetchWeatherTask(WeatherTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onWeatherPreExecute();
    }

    @Override
    protected void onPostExecute(Weather weather) {
        super.onPostExecute(weather);
        listener.onWeatherPostExecute(weather);
    }

    @Override
    protected Weather doInBackground(String... strings) {
        Weather weather = null;

        String json = WeatherHelper.getJSON(strings[0]);
        System.out.println(json);

        if (json != null) {
            weather = WeatherParser.getWeatherInfo(json);
        }

        System.out.println(weather);

        return weather;
    }
}
