package com.server.chatting.entity.chatmessage;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    @Query("{roomId: ?0}")
    List<ChatMessage> mFindByRoomId(String id);

    List<ChatMessage> findTopByRoomIdOrderByCreatedAtDesc(String id);

    Optional<ChatMessage> findChatMessageBySenderAndType(String id, MessageType type);

    @Query("{createdAt: {$gt: ?0}}")
    List<ChatMessage> findByCreatedAtGreaterThan(LocalDateTime localDateTime);
}
