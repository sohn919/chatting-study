package com.server.chatting.dto;

import com.server.chatting.entity.chatmessage.ChatMessage;
import com.server.chatting.entity.member.Member;
import lombok.Getter;

@Getter
public class ChatListDTO {
    private final Long userId;
    private final String username;
    private final String content;

    public ChatListDTO(ChatMessage chatMessage, Member member) {
        this.userId = member.getId();
        this.username = member.getUsername();
        this.content = chatMessage.getContent();
    }
}
