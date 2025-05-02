package com.example.spring.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "AnonymousSenderMappings")
@Data
public class AnonymousSenderMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    private String anonymousSenderId;

}