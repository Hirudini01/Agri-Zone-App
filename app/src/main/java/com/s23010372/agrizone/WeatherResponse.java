package com.s23010372.agrizone;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {

    @SerializedName("name")    public String name;
    @SerializedName("main")    public Main main;
    @SerializedName("weather") public List<Weather> weather;

    public static class Main {
        @SerializedName("temp") public double temp;
    }
    public static class Weather {
        @SerializedName("description") public String description;
    }
}
