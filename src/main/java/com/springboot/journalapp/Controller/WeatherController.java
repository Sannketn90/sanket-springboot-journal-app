package com.springboot.journalapp.Controller;

import com.springboot.journalapp.Dto.WeatherDTO;
import com.springboot.journalapp.Service.WeatherService;
import com.springboot.journalapp.api.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/{city}")
    public ResponseEntity<ApiResponse<WeatherDTO>> getWeather(@PathVariable String city) {
        WeatherDTO weatherResponse = weatherService.getWeather(city);
        return ResponseEntity.ok(ApiResponse.success(weatherResponse, "Weather Data fetched successfully"));
    }
}



