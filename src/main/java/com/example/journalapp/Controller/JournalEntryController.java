package com.example.journalapp.Controller;


import com.example.journalapp.Entity.JournalEntry;
import com.example.journalapp.Entity.UserEntity;
import com.example.journalapp.Service.JournalEntryService;
import com.example.journalapp.Service.UserService;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @GetMapping()
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        UserEntity user=userService.getByUsername(userName);
        List<JournalEntry> journalEntries=user.getJournalEntries();
        if(journalEntries!=null && !journalEntries.isEmpty()){
            return new ResponseEntity<List<JournalEntry>>(journalEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry journalEntry){
        try{
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String userName=authentication.getName();
            UserEntity user=userService.getByUsername(userName);
            journalEntryService.saveEntry(journalEntry,userName);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("byId/{ID}")
    public ResponseEntity<?> getById(@PathVariable ObjectId ID){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        Optional<JournalEntry> journalEntry=journalEntryService.getById(ID,userName);
        if(journalEntry.isPresent())return new ResponseEntity<>(journalEntry.get(),HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("change/{id}")
    public ResponseEntity<JournalEntry> changeById(@RequestBody JournalEntry journalEntry,@PathVariable ObjectId id){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        JournalEntry journalEntry1=journalEntryService.putValue(journalEntry,id,username);
        if(journalEntry1!=null)return new ResponseEntity<>(journalEntry1,HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("byId/{ID}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId ID){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        if(getById(ID).getStatusCode()==HttpStatus.NOT_FOUND){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return journalEntryService.deleteById(ID,userName)?new ResponseEntity<>(HttpStatus.OK):new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("Audio/{id}")
    public ResponseEntity<?> getAudio(@PathVariable ObjectId id){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        return journalEntryService.getAudioMsg(userName,id);
    }

}
