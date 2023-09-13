package com.server.chatting.service;

import com.server.chatting.dto.ChatGroupRoomListDTO;
import com.server.chatting.dto.ChatListDTO;
import com.server.chatting.dto.ChatPersonalRoomListDTO;
import com.server.chatting.entity.chatmessage.ChatMessage;
import com.server.chatting.entity.chatmessage.ChatMessageRepository;
import com.server.chatting.entity.chatmessage.MessageType;
import com.server.chatting.entity.chatroom.ChatRoom;
import com.server.chatting.entity.chatroom.ChatRoomRepository;
import com.server.chatting.entity.chatroom.ChatRoomType;
import com.server.chatting.entity.member.Member;
import com.server.chatting.entity.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public String openChatRoom(Long userId, Long memberId) {
        //중복처리 검증 로직
        ChatRoom duplicatedRoom = chatRoomRepository.findDuplicatedRoom(userId, memberId);
        if (duplicatedRoom == null) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.getMemberList().add(userId);
            chatRoom.getMemberList().add(memberId);

            chatRoom.setType(ChatRoomType.PERSONAL);
            chatRoom.setTitle(userId + "_" + memberId);
            ChatRoom savedRoom = chatRoomRepository.save(chatRoom);
            return savedRoom.getId();
        }
        return duplicatedRoom.getId();
    }

    public List<ChatPersonalRoomListDTO> getPersonalRoomList(Long userId) {
        List<ChatRoom> rooms = chatRoomRepository.findPersonalRoomById(userId);

        List<ChatPersonalRoomListDTO> chatPersonalRoomListDTOList = new ArrayList<>();

        for (ChatRoom room : rooms) {
            String title = room.getTitle().replaceAll(userId + "_", "");

            List<ChatMessage> chatMessagesList = chatMessageRepository.findTopByRoomIdOrderByCreatedAtDesc(room.getId());

            if (chatMessagesList.size() != 0) {
                ChatMessage topMessage = chatMessagesList.get(0);
                ChatPersonalRoomListDTO chatPersonalRoomListDTO = new ChatPersonalRoomListDTO(room, title, topMessage);
                chatPersonalRoomListDTOList.add(chatPersonalRoomListDTO);
            }
        }
        return chatPersonalRoomListDTOList;
    }

    public List<ChatGroupRoomListDTO> getGroupRoomList(Long userId) {
        List<ChatRoom> rooms = chatRoomRepository.findGroupRoomById(userId);

        List<ChatGroupRoomListDTO> chatGroupRoomListDTOList = new ArrayList<>();

        for (ChatRoom room : rooms) {
            List<ChatMessage> chatMessagesList = chatMessageRepository.findTopByRoomIdOrderByCreatedAtDesc(room.getId());

            if (chatMessagesList.size() != 0) {
                ChatMessage topMessage = chatMessagesList.get(0);
                ChatGroupRoomListDTO chatGroupRoomListDTO = new ChatGroupRoomListDTO(room, topMessage);
                chatGroupRoomListDTOList.add(chatGroupRoomListDTO);
            }
        }
        return chatGroupRoomListDTOList;
    }

    public List<ChatListDTO> getChattingList(Long userId, String roomId) {
        ChatRoom room = chatRoomRepository.findByRoomById(roomId);

        List<ChatMessage> chatMessages = chatMessageRepository.mFindByRoomId(roomId);

        if (room.getType() == ChatRoomType.GROUP) {
            return chattingListGroupResolver(userId);
        }

        List<ChatListDTO> chatListDTOList = new ArrayList<>();

        for (ChatMessage chatMessage : chatMessages) {
            String sender = chatMessage.getSender();

            Member member = memberRepository.findById(Long.valueOf(sender)).get();
            ChatListDTO chatListDTO = new ChatListDTO(chatMessage, member);

            chatListDTOList.add(chatListDTO);
        }

        return chatListDTOList;
    }

    private List<ChatListDTO> chattingListGroupResolver(Long userId) {
        ChatMessage joinChatMessage = chatMessageRepository.findChatMessageBySenderAndType(String.valueOf(userId), MessageType.JOIN).get();

        List<ChatMessage> chatMessages = chatMessageRepository.findByCreatedAtGreaterThan(joinChatMessage.getCreatedAt());

        List<ChatListDTO> chatListDTOList = new ArrayList<>();

        for (ChatMessage chatMessage : chatMessages) {
            String sender = chatMessage.getSender();

            Member member = memberRepository.findById(Long.valueOf(sender)).get();
            ChatListDTO chatListDTO = new ChatListDTO(chatMessage, member);

            chatListDTOList.add(chatListDTO);
        }
        return chatListDTOList;
    }
}
