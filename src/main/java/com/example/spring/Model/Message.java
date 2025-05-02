package com.example.spring.Model;

import com.example.spring.Enums.MessageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name="_messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private String anonymousSenderId;
    private String content;
    @Enumerated(EnumType.STRING)
    private MessageStatus status;
    private LocalDateTime time;
    private boolean Starred;


}
