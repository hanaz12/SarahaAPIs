package com.example.spring.ServiceImpl;

import com.example.spring.DTO.MessageDTO;
import com.example.spring.Enums.MessageStatus;
import com.example.spring.Exceptions.UserNotFoundException;
import com.example.spring.Mapper.MessageMapper;
import com.example.spring.Model.AnonymousSenderMapping;
import com.example.spring.Model.Block;
import com.example.spring.Model.Message;
import com.example.spring.Model.User;
import com.example.spring.Repository.AnonymousSenderMappingRepository;
import com.example.spring.Repository.BlockRepository;
import com.example.spring.Repository.MessageRepository;
import com.example.spring.Repository.UserRepository;
import com.example.spring.Service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {


    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;
    private final BlockRepository blockRepository;
    private final AnonymousSenderMappingRepository anonymousSenderMappingRepository;
    private final DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;

    public User getCurrentUser() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return user;
    }

    public MessageDTO sendAnonymous(MessageDTO messageDTO) {
        var sender = getCurrentUser();
        User receiver = userRepository
                .findByUsername(messageDTO.getReceiverUsername())
                .orElseThrow(() -> new UserNotFoundException("receiver not found"));
        Optional<AnonymousSenderMapping> mapping = anonymousSenderMappingRepository.findBySender(sender);
        String anonymousSenderId;
        if (mapping.isPresent()) {
            anonymousSenderId = mapping.get().getAnonymousSenderId();
        } else {
            anonymousSenderId = UUID.randomUUID().toString();
            AnonymousSenderMapping newMapping = new AnonymousSenderMapping();
            newMapping.setSender(sender);
            newMapping.setAnonymousSenderId(anonymousSenderId);
            anonymousSenderMappingRepository.save(newMapping);
        }
        Optional<Block> block = blockRepository.findByReceiverAndAnonymousSenderId(receiver, anonymousSenderId);
        if (block.isPresent()) {
            throw new IllegalArgumentException("this sender has blocked you from sending anonymous message");
        }

        Message message = messageMapper.toEntity(messageDTO,userRepository);
        message.setReceiver(receiver);
        message.setStatus(MessageStatus.UNREAD);
        message.setAnonymousSenderId(anonymousSenderId);
        message = messageRepository.save(message);
        return messageMapper.toDTO(message);
    }

    @Override
    public List<MessageDTO> getAllUnreadMessages() {
        List<MessageDTO> messages= messageRepository
                .findAllByReceiverAndStatus(getCurrentUser(),MessageStatus.UNREAD)
                .stream()
                .map(messageMapper::toDTO).collect(Collectors.toList());
        messageRepository.setAllMessagesAsReadForUser(getCurrentUser().getId());
        return messages;
    }

    @Override
    public List<MessageDTO> getAllreadMessages() {
        return messageRepository
                .findAllByReceiverAndStatus(getCurrentUser(),MessageStatus.READ)
                .stream()
                .map(messageMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public  MessageDTO findMessageById(int id) {
        Message message=messageRepository
                .findMessageByIdForloggedInUser(getCurrentUser().getId(),id)
                .orElseThrow(()->new IllegalArgumentException("Message id not found"));
                return messageMapper.toDTO(message);
    }

    @Override
    public void starMessage(int id) {
        Message message=messageRepository
                .findMessageByIdForloggedInUser(getCurrentUser().getId(),id)
                .orElseThrow(()->new IllegalArgumentException("Message id not found"));
        messageRepository.markMessageAsStarred(getCurrentUser().getId(),id);

    }

    @Override
    public void unStarMessage(int id) {
        Message message=messageRepository
                .findMessageByIdForloggedInUser(getCurrentUser().getId(),id)
                .orElseThrow(()->new IllegalArgumentException("Message id not found"));
        messageRepository.markMessageAsNotStarred(getCurrentUser().getId(),id);
    }

    @Override
    public List<MessageDTO> findAllStarMessages() {
        return messageRepository
                .findAllByReceiverIdAndStarredTrue(getCurrentUser().getId())
                .stream()
                .map(messageMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageDTO> findAllUnStarMessages() {
        return messageRepository
                .findAllByReceiverIdAndStarredFalse(getCurrentUser().getId())
                .stream()
                .map(messageMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void blockSenderOfMessage(int id) {
        Message message=messageRepository
                .findMessageByIdForloggedInUser(getCurrentUser().getId(),id)
                .orElseThrow(()->new IllegalArgumentException("Message id not found"));
        Block block=new Block();
        User receiver=message.getReceiver();
        String anonymousSenderId=message.getAnonymousSenderId();
        if (!blockRepository.findByReceiverAndAnonymousSenderId(receiver, anonymousSenderId).isPresent()) {
            block.setReceiver(receiver);
            block.setAnonymousSenderId(anonymousSenderId);
            blockRepository.save(block);
        }
    }

    @Override
    public void unBlockSenderOfMessage(int id) {
        Message message=messageRepository
                .findMessageByIdForloggedInUser(getCurrentUser().getId(),id)
                .orElseThrow(()->new IllegalArgumentException("Message id not found"));

        User receiver=message.getReceiver();
        String anonymousSenderId=message.getAnonymousSenderId();
        Optional<Block> blockOptional = blockRepository.findByReceiverAndAnonymousSenderId(receiver, anonymousSenderId);

        if (blockOptional.isPresent()) {
            blockRepository.delete(blockOptional.get());
        } else {
            throw new IllegalArgumentException("No block found for this sender and receiver");
        }
    }

    @Override
    public void deleteMessage(int id) {

        Message message = messageRepository
                .findMessageByIdForloggedInUser(getCurrentUser().getId(), id)
                .orElseThrow(() -> new IllegalArgumentException("Message id not found"));
        messageRepository.deleteMessage(getCurrentUser().getId(), id);
    }

    @Override
    public List<MessageDTO> getAllMessages() {
        return messageRepository
                .findAllByReceiver(getCurrentUser())
                .stream()
                .map(messageMapper::toDTO)
                .collect(Collectors.toList());
    }


}
