package ru.haw41k.wsstompchat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.Collection;
import java.util.List;

@Controller
public class ChatController {
    private final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final UserRepository userRepository;
    private final ChatMessageRepository messageRepository;

    public ChatController(UserRepository userRepository, ChatMessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @MessageMapping("/main")
    @SendTo("/topic/main")
    public ChatMessage sendMessageToChat(ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

        ChatMessage msg = new ChatMessage(
                HtmlUtils.htmlEscape(chatMessage.getBody()),
                userRepository.users.get(headerAccessor.getSessionId()).getName()
        );

        messageRepository.messages.add(msg);

        return msg;
    }

    @MessageMapping("/user")
    @SendTo("/topic/userlist")
    public Collection<User> setUserName(User user, SimpMessageHeaderAccessor headerAccessor) {

        userRepository.users.put(headerAccessor.getSessionId(), user);

        return userRepository.users.values();
    }

    @SubscribeMapping("/init/messagelist")
    public List<ChatMessage> getMesssageList() {

        logger.info("user init");
        return messageRepository.messages;
    }
}
