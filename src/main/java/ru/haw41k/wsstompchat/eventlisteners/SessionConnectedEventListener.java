package ru.haw41k.wsstompchat.eventlisteners;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import ru.haw41k.wsstompchat.User;
import ru.haw41k.wsstompchat.UserRepository;


@Component
public class SessionConnectedEventListener implements ApplicationListener<SessionConnectedEvent> {

    private final Logger logger = LoggerFactory.getLogger(SessionConnectedEventListener.class);
    private final UserRepository userRepository;

    public SessionConnectedEventListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {

        String sessionId = SimpMessageHeaderAccessor.wrap(event.getMessage()).getSessionId();

        userRepository.users.put(sessionId, new User("Guest"));

        logger.info("New user connected. Total connected users: {}", userRepository.users.size());
    }
}
