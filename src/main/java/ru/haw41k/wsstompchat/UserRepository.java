package ru.haw41k.wsstompchat;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserRepository {
    public final Map<String, User> users = new ConcurrentHashMap<>();
}
