package com.example.journalapp.Repository;

import com.example.journalapp.Entity.JournalEntry;
import com.example.journalapp.Entity.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, ObjectId> {
    UserEntity findByUserName(String Username);
}
