package com.example.journalapp.Service;

import com.example.journalapp.Entity.JournalEntry;
import com.example.journalapp.Entity.UserEntity;
import com.example.journalapp.Enums.Sentiments;
import com.example.journalapp.Repository.JournalEntryRepo;
import com.example.journalapp.Repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class UserService {

    @Autowired
    EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserRepository userRepository;

    public boolean saveEntry(UserEntity user) {
        try{
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
//            log.trace("TRACE");         // (R1) need to make it available in YAML or XML file
//            log.debug("DEBUG");         // (R2) need to make it available in YAML or XML file
//            log.info("INFO");           // (R3) Automatically available
//            log.warn("WARN");           // (R4) Automatically available
//            log.error("ERROR");         // (R5) Automatically available
            return false;
        }
    }
    public void saveAdminEntry(UserEntity user) {
        if (userRepository.findByUserName(user.getUserName())!=null) {
            throw new RuntimeException("Username already exists!");
        }
        else{
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("ADMIN"));
            userRepository.save(user);
        }
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public UserEntity getByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }

    public void deleteById(String userName){
        UserEntity user=userRepository.findByUserName(userName);
        userRepository.deleteById(user.getId());
    }

    public UserEntity putValue(UserEntity user,String username){
        UserEntity old=userRepository.findByUserName(username);
        if(old==null)saveEntry(user);
        if(user.getUserName()!=null && user.getUserName().length()!=0){
            old.setUserName(user.getUserName());
        }
        if(user.getPassword()!=null && user.getPassword().length()!=0){
            old.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(old);
        return old;
    }


    public ResponseEntity<?> sentimentAnalysis(String username){
        UserEntity user=userRepository.findByUserName(username);
        if(user.isSentientAnalysisFlag() && user.getUserEmail()!=null){
            Map<Sentiments,Integer> map=new HashMap<>();
            for(JournalEntry i:user.getJournalEntries()){
                if(i.getSentiments()!=null){
                if(map.containsKey(i.getSentiments()))
                    map.put(i.getSentiments(),map.get(i.getSentiments())+1);
                else
                    map.put(i.getSentiments(),1);
            }}
            if(map.isEmpty())return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            Sentiments val=null;
            int count=0;
            for (Map.Entry<Sentiments, Integer> entry : map.entrySet()) {
                if(count<entry.getValue()){
                    count=entry.getValue();
                    val=entry.getKey();
                }
            }
            String subject="Sentiment analysis";
            String body="hello "+username+"\nYour Sentiment analysis says your mood in past days was majorly "+val;
            emailService.sendEmail(user.getUserEmail(),subject,body);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
    }
}