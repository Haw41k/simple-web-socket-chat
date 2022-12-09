package ru.haw41k.wsstompchat;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChatMessageRepository {
    public final List<ChatMessage> messages = new CopyOnWriteArrayList<>();
}
