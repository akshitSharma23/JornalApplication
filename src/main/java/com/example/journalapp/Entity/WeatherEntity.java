package com.example.journalapp.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class WeatherEntity {
    private Current current;

    @Data
    @NoArgsConstructor
    public class Current {

        private int temperature;
        @JsonProperty("weatherDescriptions")
        private ArrayList<String> weather_descriptions;
        private int feelslike;
    }
}


