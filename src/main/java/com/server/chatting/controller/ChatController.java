package com.server.chatting.controller;

import com.server.chatting.entity.chatmessage.ChatMessage;
import com.server.chatting.entity.chatmessage.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessageSendingOperations template;

    @MessageMapping("/chat/send")
    public void sendMessage(@Payload ChatMessage chatMessage, @Header("Authorization") String token) {
        System.out.println(token);
        chatMessage.setCreatedAt(LocalDateTime.now());
        chatMessageRepository.save(chatMessage);
        template.convertAndSend("/sub/public/" + chatMessage.getRoomId(), chatMessage);
    }
}
