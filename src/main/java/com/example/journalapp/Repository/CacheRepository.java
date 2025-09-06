package com.example.journalapp.Repository;

import com.example.journalapp.Entity.CacheEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheRepository extends MongoRepository<CacheEntity, ObjectId> {

}