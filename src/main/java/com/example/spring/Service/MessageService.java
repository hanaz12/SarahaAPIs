package com.example.spring.Service;


import com.example.spring.DTO.MessageDTO;
import com.example.spring.Model.User;

import java.util.List;

public interface MessageService {
    List<MessageDTO> getAllMessages();
    MessageDTO sendAnonymous( MessageDTO message);
    List<MessageDTO> getAllUnreadMessages();
    List<MessageDTO> getAllreadMessages();
    MessageDTO findMessageById(int id);
    void starMessage(int id);
    void unStarMessage(int id);
    List<MessageDTO> findAllStarMessages();
    List<MessageDTO> findAllUnStarMessages();
    void blockSenderOfMessage(int id);
    void unBlockSenderOfMessage(int id);
    void deleteMessage(int id);
}
