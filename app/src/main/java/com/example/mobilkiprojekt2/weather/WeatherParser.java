package com.example.mobilkiprojekt2.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherParser {
    public static Weather getWeatherInfo(String json) {
        Weather weather = new Weather();

        try {
            JSONObject root = new JSONObject(json);

            JSONArray weatherArray = root.getJSONArray("weather");
            JSONObject firstJsonObject = weatherArray.getJSONObject(0);
            weather.setCondition(firstJsonObject.getString("main"));
            weather.setDescription(firstJsonObject.getString("description"));
            weather.setIcon(firstJsonObject.getString("icon"));

            JSONObject secondJsonObject = root.getJSONObject("main");
            weather.setTemperature((float) secondJsonObject.getDouble("temp"));
            weather.setFeelsLikeTemperature((float) secondJsonObject.getDouble("feels_like"));
            weather.setPressure(secondJsonObject.getInt("pressure"));
            weather.setHumidity(secondJsonObject.getInt("humidity"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weather;
    }
}
