package com.example.spring.Repository;

import com.example.spring.DTO.MessageDTO;
import com.example.spring.Enums.MessageStatus;
import com.example.spring.Model.Message;
import com.example.spring.Model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findAllByReceiver(User receiver);

    List<Message> findAllByReceiverAndStatus(User receiver, MessageStatus status);

    @Query("SELECT m FROM Message m WHERE m.id = :messageId AND m.receiver.id = :receiverId")
    Optional<Message> findMessageByIdForloggedInUser(int receiverId, int messageId);

    @Query("SELECT m FROM Message m WHERE m.Starred=true AND m.receiver.id=:receiverId")
    List<Message> findAllByReceiverIdAndStarredTrue(int receiverId);

    @Query("SELECT m FROM Message m WHERE m.Starred=false AND m.receiver.id=:receiverId")
    List<Message> findAllByReceiverIdAndStarredFalse(int receiverId);


    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.status = 'READ' WHERE m.receiver.id = :id")
    int setAllMessagesAsReadForUser(int id);


    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.Starred=true WHERE m.receiver.id=:receiverId and m.id=:messageId ")
   int markMessageAsStarred(int receiverId, int messageId);

    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.Starred=false WHERE m.receiver.id=:receiverId and m.id=:messageId ")
    int markMessageAsNotStarred(int receiverId, int messageId);

    @Modifying
    @Transactional
    @Query("DELETE Message m WHERE m.receiver.id=:receiverId and m.id=:messageId ")
    int deleteMessage(int receiverId, int messageId);
}
