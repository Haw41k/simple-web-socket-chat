package ru.haw41k.wsstompchat.eventlisteners;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.haw41k.wsstompchat.UserRepository;


@Component
public class SessionDisconnectedEventListener implements ApplicationListener<SessionDisconnectEvent> {

    private final Logger logger = LoggerFactory.getLogger(SessionDisconnectedEventListener.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;


    public SessionDisconnectedEventListener(SimpMessagingTemplate messagingTemplate, UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
    }


    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {

        String sessionId = SimpMessageHeaderAccessor.wrap(event.getMessage()).getSessionId();

        userRepository.users.remove(sessionId);

        messagingTemplate.convertAndSend("/topic/userlist", userRepository.users.values());

        logger.info("User disconnected. Total connected users: {}", userRepository.users.size());

    }
}
