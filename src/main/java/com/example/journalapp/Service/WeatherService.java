package com.example.journalapp.Service;

import com.example.journalapp.Cache.AppCache;
import com.example.journalapp.Entity.WeatherEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
public class WeatherService {

    @Value("${Weather.API.Key}")
    private String APIkey;
//    @Value("${Weather.API.EndPoint}")
//    private String API;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    public Optional<WeatherEntity> getWeather(String city){
        ResponseEntity<WeatherEntity> weatherEntity;
        String url=UriComponentsBuilder.fromHttpUrl(appCache.getAppCache().get("WEATHER_API"))
//        String url=UriComponentsBuilder.fromHttpUrl(API)
                .queryParam("access_key", APIkey)
                .queryParam("query", city)
                .toUriString();
        try{
        weatherEntity = restTemplate.exchange(url, HttpMethod.GET, null, WeatherEntity.class);
        return weatherEntity.getStatusCode()== HttpStatus.OK? Optional.of(weatherEntity.getBody()):null;
        }catch(Exception e){
            return Optional.empty();
        }
    }

}
