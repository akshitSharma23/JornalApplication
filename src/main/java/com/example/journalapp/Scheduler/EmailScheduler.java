package com.example.journalapp.Scheduler;

import com.example.journalapp.Entity.JournalEntry;
import com.example.journalapp.Entity.UserEntity;
import com.example.journalapp.Enums.Sentiments;
import com.example.journalapp.Repository.UserRepositoryIMPl;
import com.example.journalapp.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class EmailScheduler {

    @Autowired
    private UserRepositoryIMPl userRepositoryIMPl;

    @Autowired
    private EmailService emailService;

//    @Scheduled(cron = "0 0 9 * * SUN")
    @Scheduled(cron = "0 * * ? * *")
    public void fetchUserAndSendEmail(){
        List<UserEntity> userEntityList=userRepositoryIMPl.getUserForSA();
        for(UserEntity user:userEntityList){
            Map<Sentiments,Integer> map=new HashMap<>();
            for(JournalEntry i:user.getJournalEntries()){
                if(i.getSentiments()!=null){
                    if(map.containsKey(i.getSentiments()))
                        map.put(i.getSentiments(),map.get(i.getSentiments())+1);
                    else
                        map.put(i.getSentiments(),1);
                }
            }
            Sentiments val=null;
            int count=0;
            for (Map.Entry<Sentiments, Integer> entry : map.entrySet()) {
                if(count<entry.getValue()){
                    count=entry.getValue();
                    val=entry.getKey();
                }
            }
            String subject="Sentiment analysis";
            String body="Hello "+user.getUserName()+"\n\nYour Sentiment analysis says your mood in past days was majorly "+val;
            emailService.sendEmail(user.getUserEmail(),subject,body);
        }
    }

}
