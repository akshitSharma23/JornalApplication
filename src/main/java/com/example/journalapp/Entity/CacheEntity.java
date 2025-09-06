package com.example.journalapp.Entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="cache")
@Data
@NoArgsConstructor
public class CacheEntity {
    @Id
    ObjectId id;
    private String key;
    private String value;
}
