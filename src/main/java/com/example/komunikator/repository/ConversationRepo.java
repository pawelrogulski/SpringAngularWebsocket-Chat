package com.example.komunikator.repository;

import com.example.komunikator.domain.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepo extends JpaRepository<Conversation, Integer> {
}
