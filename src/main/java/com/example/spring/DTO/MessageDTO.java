package com.example.spring.DTO;

import com.example.spring.Enums.MessageStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class MessageDTO {

private int id;

@NotBlank(message = "message can't be empty")
@NotNull(message = "content can't null")
private String content;

@NotBlank (message = "receiver username can't be empty")
@NotNull(message="Receiver username can't be null")
private String receiverUsername;

private String time;
private MessageStatus status;
    private boolean Starred;
}
