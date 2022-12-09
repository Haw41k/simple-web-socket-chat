package ru.haw41k.wsstompchat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;

public class ChatMessage {
    private String senderName = "unnamed";
    private String body;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime creationDate = ZonedDateTime.now();

    public ChatMessage() {
    }

    public ChatMessage(String body) {
        this.body = body;
    }

    public ChatMessage(String body, String senderName) {
        this.senderName = senderName;
        this.body = body;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
