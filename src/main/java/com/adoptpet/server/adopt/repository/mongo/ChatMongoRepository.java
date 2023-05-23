package com.adoptpet.server.adopt.repository.mongo;

import com.adoptpet.server.adopt.domain.mongo.Chatting;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMongoRepository extends MongoRepository<Chatting, String> {

    List<Chatting> findByChatRoomNo(Integer chatNo);
}
