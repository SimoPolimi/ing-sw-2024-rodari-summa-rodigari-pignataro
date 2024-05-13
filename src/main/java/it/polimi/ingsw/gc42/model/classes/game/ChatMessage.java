package it.polimi.ingsw.gc42.model.classes.game;

import java.time.LocalDateTime;

public class ChatMessage {
    private Player sender;
    private String text;
    private LocalDateTime dateTime;

    public ChatMessage(String mess, Player player){
        this.text = mess;
        this.sender = player;
        this.dateTime = LocalDateTime.now();
    }

    public Player getSender() {
        return sender;
    }

    public void setSender(Player sender) {
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
