package com.server.chatting.dto;

import com.server.chatting.entity.chatmessage.ChatMessage;
import com.server.chatting.entity.chatroom.ChatRoom;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatPersonalRoomListDTO {
    private final String roomId;
    private final String title;
    private final String recentText;
    private final LocalDateTime recentTime;

    public ChatPersonalRoomListDTO(ChatRoom chatRoom, String title, ChatMessage chatMessage) {
        this.roomId = chatRoom.getId();
        this.title = title;
        this.recentText = chatMessage.getContent();
        this.recentTime = chatMessage.getCreatedAt();
    }
}
