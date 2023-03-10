package com.example.komunikator.repository;

import com.example.komunikator.domain.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConversationRepo extends JpaRepository<Conversation, Integer> {
    @Query("SELECT c FROM Conversation c JOIN c.users u WHERE u.id IN (:usersId) GROUP BY c.id HAVING COUNT(c.id)>1")
    List<Conversation> findConversationByUserId(int[] usersId);
}
