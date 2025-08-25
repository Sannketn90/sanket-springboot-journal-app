package com.springboot.journalapp.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherResponse {

    private Location location;
    private Current current;

    @Getter
    @Setter
    public class Current {

        @JsonProperty("temperature")
        private int temperature;
        @JsonProperty("weather_descriptions")
        private List<String> weatherdescriptions;
        @JsonProperty("humidity")
        private int humidity;
        @JsonProperty("feelslike")
        private int feelslike;
    }

    @Getter
    @Setter
    public class Location {
        @JsonProperty("name")
        private String name;
        @JsonProperty("country")
        private String country;
        @JsonProperty("region")
        private String region;
    }


}







