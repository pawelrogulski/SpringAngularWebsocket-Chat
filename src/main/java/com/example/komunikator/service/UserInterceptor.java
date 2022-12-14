package com.example.komunikator.service;

import com.example.komunikator.domain.MessageUser;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RequiredArgsConstructor
public class UserInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String username = "";
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw2 = message.getHeaders().get("simpUser");
            if(raw2 instanceof UsernamePasswordAuthenticationToken){
                username = ((UsernamePasswordAuthenticationToken) message.getHeaders().get("simpUser")).getName();
            }
            accessor.setUser(new MessageUser(username));

        }
        return message;
    }
}