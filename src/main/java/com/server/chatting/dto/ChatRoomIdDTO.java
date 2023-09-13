package com.server.chatting.dto;

import lombok.Getter;

@Getter
public class ChatRoomIdDTO {

    private final String roomId;

    public ChatRoomIdDTO(String roomId) {
        this.roomId = roomId;
    }
}
