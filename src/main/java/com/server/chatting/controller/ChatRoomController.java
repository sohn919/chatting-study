package com.server.chatting.controller;

import com.server.chatting.dto.ChatGroupRoomListDTO;
import com.server.chatting.dto.ChatListDTO;
import com.server.chatting.dto.ChatPersonalRoomListDTO;
import com.server.chatting.security.CustomUserDetails;
import com.server.chatting.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/chat/open/p/{memberId}")
    public String openPersonalRoom(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long memberId) {
        String roomId = chatRoomService.openChatRoom(userDetails.getUserId(), memberId);
//        return new ResponseEntity<>(new ChatRoomIdDTO(roomId), HttpStatus.OK);
        return "redirect:/chat/list/" + roomId;
    }

    @GetMapping("/chat/list/{roomId}")
    public ResponseEntity<?> chattingList(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable String roomId) {
        List<ChatListDTO> response = chatRoomService.getChattingList(userDetails.getUserId(), roomId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/chat/open/p/list")
    public ResponseEntity<?> personalRoomList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChatPersonalRoomListDTO> response = chatRoomService.getPersonalRoomList(userDetails.getUserId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/chat/open/g/list")
    public ResponseEntity<?> groupRoomList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChatGroupRoomListDTO> response = chatRoomService.getGroupRoomList(userDetails.getUserId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
