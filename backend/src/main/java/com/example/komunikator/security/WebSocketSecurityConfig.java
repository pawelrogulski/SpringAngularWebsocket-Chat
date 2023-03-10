package com.example.komunikator.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected boolean sameOriginDisabled(){
        return true;
    }
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages){
        messages.simpSubscribeDestMatchers("/chat/**").authenticated();
    }
}