package com.gordonchild.websocket;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @NotNull
    private final String allowedOrigins;

    public WebSocketConfig(@Value("${allowed.origins}") @NotNull String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/chatApp");
    }

    @Override
    public void registerStompEndpoints(@NotNull StompEndpointRegistry registry) {
        String[] origins = {};
        if(StringUtils.isNotBlank(this.allowedOrigins)) {
            origins = this.allowedOrigins.split(",");
        }
        registry.addEndpoint("/chat")
                .setAllowedOrigins(origins)
                .withSockJS();
    }
}
