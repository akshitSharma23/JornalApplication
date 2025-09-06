package com.example.journalapp.Repository;

import com.example.journalapp.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryIMPl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<UserEntity> getUserForSA(){
        Query query=new Query();
        Criteria criteria=new Criteria();
        criteria.andOperator(
                Criteria.where("userEmail").exists(true),
                Criteria.where("userEmail").ne(null),
                criteria.where("userEmail").regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"),
//                Criteria.where("roles").in("ADMIN","USER"),
                Criteria.where("sentientAnalysisFlag").is(true)
        );
        query.addCriteria(criteria);
        List<UserEntity> userEntities=mongoTemplate.find(query,UserEntity.class);
        return userEntities;
    }

}
