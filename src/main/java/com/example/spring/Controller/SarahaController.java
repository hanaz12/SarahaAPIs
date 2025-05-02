package com.example.spring.Controller;

import com.example.spring.DTO.MessageDTO;
import com.example.spring.Exceptions.SuccessResponse;
import com.example.spring.Repository.MessageRepository;
import com.example.spring.Service.MessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/saraha")
public class SarahaController {
    private final MessageService messageService;
    private final MessageRepository messageRepository;

    @PostMapping("/send_anonymous")
    public ResponseEntity<MessageDTO> sendAnonymous(@Valid @RequestBody MessageDTO messageDTO) {
        return ResponseEntity.ok(messageService.sendAnonymous(messageDTO));
    }

    @GetMapping("/getAllUnreadMessages")
    public ResponseEntity<List<MessageDTO>> getAllUnreadMessages() {
        return ResponseEntity.ok(messageService.getAllUnreadMessages());
    }

    @GetMapping("/getAllreadMessages")
    public ResponseEntity<List<MessageDTO>> getAllreadMessages() {
        return ResponseEntity.ok(messageService.getAllreadMessages());
    }

    @GetMapping("/getAllMessages")
    public ResponseEntity<List<MessageDTO>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/getMessage/{id}")
    public ResponseEntity<MessageDTO> getMessage(@PathVariable int id) {
        return ResponseEntity.ok(messageService.findMessageById(id));
    }

    @PutMapping("/starMessage/{id}")
    public ResponseEntity<Void> starMessage(@PathVariable int id) {
        messageService.starMessage(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/unStarMessage/{id}")
    public ResponseEntity<Void> unStarMessage(@PathVariable int id) {
        messageService.unStarMessage(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getStarredMessages")
    public ResponseEntity<List<MessageDTO>> getAllStarredMessages() {
        return ResponseEntity.ok(messageService.findAllStarMessages());
    }
    @GetMapping("/getUnStarredMessages")
    public ResponseEntity<List<MessageDTO>> getAllUnStarredMessages() {
        return ResponseEntity.ok(messageService.findAllUnStarMessages());
    }
    @PutMapping("/blockSenderOfMessage/{id}")
    public ResponseEntity<Void> blockSenderOfMessage(@PathVariable int id) {
        messageService.blockSenderOfMessage(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/unBlockSenderOfMessage/{id}")
    public ResponseEntity<Void> unBlockSenderOfMessage(@PathVariable int id) {
        messageService.unBlockSenderOfMessage(id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/deleteMessage/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable int id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }
}
