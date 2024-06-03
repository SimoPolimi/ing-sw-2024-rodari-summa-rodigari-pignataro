package it.polimi.ingsw.gc42.model.classes.game;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ChatMessage implements Serializable {
    private String sender;
    private String text;
    private LocalDateTime dateTime;

    public ChatMessage(String text, String player){
        this.text = text;
        this.sender = player;
        this.dateTime = LocalDateTime.now();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
