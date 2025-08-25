package com.springboot.journalapp.Service;

import com.springboot.journalapp.Dto.WeatherDTO;
import com.springboot.journalapp.Entity.WeatherResponse;
import com.springboot.journalapp.Exception.CityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String apikey;
    @Value("${weather.api.url}")
    private String BASE_URL;

    public WeatherDTO getWeather(String city) {
        try {
            log.info("Fetching weather for city: {}", city);

            ResponseEntity<WeatherResponse> response = restTemplate.exchange(
                    BASE_URL,
                    HttpMethod.GET,
                    null,
                    WeatherResponse.class,
                    apikey,
                    city
            );

            WeatherResponse body = response.getBody();

            if (body == null || body.getLocation() == null) {
                log.warn("No weather data found for city: {}", city);
                throw new CityNotFoundException("City not found:" + city);
            }

            log.info("Weather data fetched successfully for city: {}", city);

            WeatherDTO weatherDTO = new WeatherDTO();
            weatherDTO.setCity(body.getLocation().getName());
            weatherDTO.setCountry(body.getLocation().getCountry());
            weatherDTO.setCondition(String.join(", ", body.getCurrent().getWeatherdescriptions()));
            weatherDTO.setTemperature(body.getCurrent().getTemperature());
            weatherDTO.setFeelsLike(body.getCurrent().getFeelslike());
            weatherDTO.setHumidity(body.getCurrent().getHumidity());

            return weatherDTO;

        } catch (HttpClientErrorException e) {
            log.error("Weather API returned error: {}", e.getResponseBodyAsString());
            return null;
        } catch (ResourceAccessException e) {
            log.error("Network error while calling Weather API", e);
            return null;
        } catch (Exception e) {
            log.error("Unexpected error while calling Weather API", e);
            return null;
        }
    }

}