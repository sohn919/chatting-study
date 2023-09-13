package com.server.chatting.entity.chatroom;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    @Query("{$and: [{memberList: ?0}, {memberList: ?1}]}")
    ChatRoom findDuplicatedRoom(Long memberId, Long opId);

    @Query("{memberList: ?0, type: 'PERSONAL'}")
    List<ChatRoom> findPersonalRoomById(Long memberId);

    @Query("{memberList: ?0, type: 'GROUP'}")
    List<ChatRoom> findGroupRoomById(Long memberId);

    @Query("{memberList: ?0}")
    List<ChatRoom> findMemberRoomById(Long memberId);

    @Query("{id: ?0}")
    ChatRoom findByRoomById(String roomId);

}
