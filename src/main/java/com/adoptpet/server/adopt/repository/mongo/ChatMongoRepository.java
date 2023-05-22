package com.adoptpet.server.adopt.repository.mongo;

import com.adoptpet.server.adopt.domain.mongo.Chatting;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMongoRepository extends MongoRepository<Chatting, String> {

}
