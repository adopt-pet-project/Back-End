package com.adoptpet.server.adopt.mongo;

import com.adoptpet.server.adopt.domain.mongo.Chatting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface MongoChatRepository extends MongoRepository<Chatting, String> {

    List<Chatting> findByChatRoomNo(Integer chatNo);

    @Query("{'chatRoomNo': {$in: ?0}, 'sendMemberNo': {$ne: ?1}, 'readCount': 1}")
    List<Long> findUnreadChattingCount(List<Integer> chatNos, int sendMemberNo);

    @Query("{'chatRoomNo': ?0}")
    long updateReadCountToZeroByChatRoomNo(long chatRoomNo);

    @Query("{'chatRoomNo': ?0}")
    int updateReadCountToOneByChatRoomNo(long chatRoomNo);


}
