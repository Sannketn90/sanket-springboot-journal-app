package com.springboot.journalapp.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDTO {
    private String city;
    private String country;
    private String condition;
    private int temperature;
    private int feelsLike;
    private int humidity;
}
