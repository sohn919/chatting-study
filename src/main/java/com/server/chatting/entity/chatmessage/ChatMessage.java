package com.server.chatting.entity.chatmessage;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "chat")
@Builder
public class ChatMessage {
    @Id
    private String id;

    private String roomId;
    private String content;
    private String sender;

    private MessageType type;

    private String imageUrl;

    private LocalDateTime createdAt;
}
