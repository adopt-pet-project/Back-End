package com.adoptpet.server.adopt.mongo;

import com.adoptpet.server.adopt.domain.mongo.Chatting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;


public interface MongoChatRepository extends MongoRepository<Chatting, String> {

    List<Chatting> findByChatRoomNo(Integer chatNo);

    Page<Chatting> findByChatRoomNoOrderBySendDateDesc(Integer chatRoomNo, Pageable pageable);
}
