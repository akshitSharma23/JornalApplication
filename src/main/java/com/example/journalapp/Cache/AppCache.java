package com.example.journalapp.Cache;

import com.example.journalapp.Entity.CacheEntity;
import com.example.journalapp.Repository.CacheRepository;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Data
public class AppCache {

    private Map<String,String> appCache;

    @Autowired
    private CacheRepository cacheRepository;


    @PostConstruct
    public void init(){
        appCache=new HashMap<>();
        List<CacheEntity> cacheEntityList=cacheRepository.findAll();
        for(CacheEntity i:cacheEntityList){
            appCache.put(i.getKey(),i.getValue());
        }
    }
}
