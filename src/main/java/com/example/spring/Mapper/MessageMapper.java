package com.example.spring.Mapper;

import com.example.spring.DTO.MessageDTO;
import com.example.spring.Model.Message;
import com.example.spring.Model.User;
import com.example.spring.Repository.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "anonymousSenderId", ignore = true)
    @Mapping(target = "receiver", source = "receiverUsername", qualifiedByName = "usernameToUser")
    @Mapping(target = "status", constant = "UNREAD")

    @Mapping(target = "time", expression = "java(java.time.LocalDateTime.now())")
    Message toEntity(MessageDTO messageDTO, @Context UserRepository userRepository);

    @Mapping(target = "time", expression = "java(message.getTime().format(java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm:ss\")))")
    @Mapping(target = "receiverUsername", source = "receiver.username")

    MessageDTO toDTO(Message message);

    @Named("usernameToUser")
    default User usernameToUser(String username, @Context UserRepository userRepository) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }
}