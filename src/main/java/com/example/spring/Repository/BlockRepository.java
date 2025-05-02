package com.example.spring.Repository;

import com.example.spring.Model.Block;
import com.example.spring.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<Block, Integer> {
    Optional<Block> findByReceiverAndAnonymousSenderId(User receiver, String anonymousSenderId);
}
