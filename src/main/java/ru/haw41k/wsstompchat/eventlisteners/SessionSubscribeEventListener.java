package ru.haw41k.wsstompchat.eventlisteners;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import ru.haw41k.wsstompchat.UserRepository;


@Component
public class SessionSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    private final Logger logger = LoggerFactory.getLogger(SessionSubscribeEventListener.class);
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public SessionSubscribeEventListener(UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {

        String destination = SimpMessageHeaderAccessor.wrap(event.getMessage()).getDestination();

        if ("/topic/main".equals(destination)) {

            messagingTemplate.convertAndSend("/topic/userlist", userRepository.users.values());

            logger.info("user subscribed to chat");

        }
    }
}
