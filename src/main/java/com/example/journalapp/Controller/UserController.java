package com.example.journalapp.Controller;


import com.example.journalapp.Entity.JournalEntry;
import com.example.journalapp.Entity.UserEntity;
import com.example.journalapp.Entity.WeatherEntity;
import com.example.journalapp.Service.JournalEntryService;
import com.example.journalapp.Service.UserService;
import com.example.journalapp.Service.WeatherService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeatherService weatherService;


//    @GetMapping()
//    public ResponseEntity<List<UserEntity>> getAll(){
//        List<UserEntity> userEntities=userService.getAll();
//        if(userEntities!=null && !userEntities.isEmpty()){
//            return new ResponseEntity<List<UserEntity>>(userEntities, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

    @GetMapping("byId/{username}")
    public ResponseEntity<?> getByUserName(@PathVariable String username){
        UserEntity optionalUserEntity=userService.getByUsername(username);
        if(optionalUserEntity!=null)return new ResponseEntity<>(optionalUserEntity,HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping()
    public ResponseEntity<UserEntity> changeById(@RequestBody UserEntity user){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        UserEntity userEntity=userService.putValue(user,userName);
        return new ResponseEntity<>(userEntity,HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteById(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        if(getByUserName(username).getStatusCode()==HttpStatus.NOT_FOUND)return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        userService.deleteById(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("greeting")
    public ResponseEntity<?> greeting(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        Optional<WeatherEntity> weatherEntity=weatherService.getWeather("Delhi");

        if(weatherEntity.isPresent() && weatherEntity.get().getCurrent()!=null){
            Integer weatherResponse=weatherEntity.get().getCurrent().getFeelslike();
            return new ResponseEntity<>("Hi "+username+" today's weather feels like "+weatherResponse,HttpStatus.OK);
        }
        return new ResponseEntity<>("Hi "+username,HttpStatus.OK);
    }
    @GetMapping("sentimentAnalysis")
    public ResponseEntity<?> sendSentimentAnalysis(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        return userService.sentimentAnalysis(username);
    }



}
