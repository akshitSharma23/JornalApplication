package com.example.journalapp.Service;

import com.example.journalapp.Cache.AppCache;
import com.example.journalapp.Entity.WeatherEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
@Slf4j
public class WeatherService {

    @Value("${Weather.API.Key}")
    private String APIkey;
//    @Value("${Weather.API.EndPoint}")
//    private String API;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;


    public Optional<WeatherEntity> getWeather(String city){
        WeatherEntity obj=redisService.get(city,WeatherEntity.class);
        if(obj!=null){
            return Optional.of(obj);
        }
        ResponseEntity<WeatherEntity> weatherEntity;
        String url=UriComponentsBuilder.fromHttpUrl(appCache.getAppCache().get("WEATHER_API"))
//        String url=UriComponentsBuilder.fromHttpUrl(API)
                .queryParam("access_key", APIkey)
                .queryParam("query", city)
                .toUriString();
        try{
        weatherEntity = restTemplate.exchange(url, HttpMethod.GET, null, WeatherEntity.class);
        if(weatherEntity.getStatusCode()== HttpStatus.OK && weatherEntity.getBody()!=null){
            redisService.set(city,weatherEntity.getBody(),5000L);
            return Optional.of(weatherEntity.getBody());
        }
        else{
            log.error("something wrong in try block of weather service");
            return null;
        }
        }catch(Exception e){
            log.error("Fail to get weather details",e);
            return Optional.empty();
        }
    }

}
