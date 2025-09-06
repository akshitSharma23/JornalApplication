package com.example.journalapp.Service;

import com.example.journalapp.Entity.JournalEntry;
import com.example.journalapp.Entity.UserEntity;
import com.example.journalapp.Repository.JournalEntryRepo;
import com.example.journalapp.Repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.MediaSize;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    ElevenLabsService elevenLabsService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username) {
        UserEntity user=userRepository.findByUserName(username);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved=journalEntryRepo.save(journalEntry);
        user.getJournalEntries().add(saved);
        userRepository.save((user));
    }
    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepo.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> getById(ObjectId id,String userName) {
        UserEntity user=userRepository.findByUserName(userName);
        if(user.getJournalEntries().stream().anyMatch(x->x.getId().equals(id)))
            return journalEntryRepo.findById(id);
        else
            return Optional.empty();
    }

    @Transactional
    public boolean deleteById(ObjectId id,String userName) {
        try{
            UserEntity user=userRepository.findByUserName(userName);
            if (user.getJournalEntries().stream().anyMatch(x -> x.getId().equals(id))) {
                journalEntryRepo.deleteById(id);
                user.getJournalEntries().remove(id);
                userRepository.save(user);
                return true;
            }
        }
        catch(Exception e){
            throw new RuntimeException("an error occurred while deleting th entry");
        }
        return false;
    }

    public JournalEntry putValue(JournalEntry journalEntry,ObjectId id,String username){
        UserEntity user=userRepository.findByUserName(username);
        JournalEntry old=journalEntryRepo.findById(id).orElse(null);
        if(old==null)saveEntry(journalEntry);
        if(journalEntry.getTitle()!=null && journalEntry.getTitle().length()!=0){
            old.setTitle(journalEntry.getTitle());
        }
        if(journalEntry.getContent()!=null && journalEntry.getContent().length()!=0){
            old.setContent(journalEntry.getContent());
        }
        journalEntryRepo.save(old);
        return old;
    }

    public ResponseEntity<?> getAudioMsg(String userName, ObjectId id){
        Optional<JournalEntry> optionalJournalEntry=getById(id,userName);
        if(optionalJournalEntry.isEmpty())return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        ResponseEntity<?> response=elevenLabsService.getAudio(optionalJournalEntry.get());
        return new ResponseEntity<>(response.getStatusCode());
    }




}