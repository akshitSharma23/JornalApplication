package com.example.journalapp.Entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document (collection= "user")
@Data
@NoArgsConstructor
public class UserEntity {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @NonNull
    private String userName;
    @NonNull
    private String password;
    private String userEmail;
    private boolean sentientAnalysisFlag;
    @DBRef
    private List<JournalEntry> journalEntries=new ArrayList<>();
    private List<String> roles;

//    public @NonNull String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(@NonNull String userName) {
//        this.userName = userName;
//    }
//
//    public @NonNull String getPassword() {
//        return password;
//    }
//
//    public void setPassword(@NonNull String password) {
//        this.password = password;
//    }
//
//    public List<JournalEntry> getJournalEntries() {
//        return journalEntries;
//    }
//
//    public void setJournalEntries(List<JournalEntry> journalEntries) {
//        this.journalEntries = journalEntries;
//    }
//
//    public List<String> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(List<String> roles) {
//        this.roles = roles;
//    }
}