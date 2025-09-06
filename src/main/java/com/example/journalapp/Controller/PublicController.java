package com.example.journalapp.Controller;

import com.example.journalapp.Entity.UserEntity;
import com.example.journalapp.Service.UserDetailsServiceIMPL;
import com.example.journalapp.Service.UserService;
import com.example.journalapp.Utils.JWTutil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Executable;


@RestController
@Slf4j
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceIMPL userDetailsServiceIMPL;

    @Autowired
    private JWTutil jwTutil;

    @GetMapping("/healthcheck")
    public String health(){
        return "OK";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserEntity user){
        return userService.saveEntry(user)?new ResponseEntity<>(HttpStatus.OK):new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody UserEntity user){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            UserDetails userDetails=userDetailsServiceIMPL.loadUserByUsername(user.getUserName());
            String jwt=jwTutil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt,HttpStatus.OK);
        }catch(Exception e){
            log.error("error occured while creating auth token",e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
