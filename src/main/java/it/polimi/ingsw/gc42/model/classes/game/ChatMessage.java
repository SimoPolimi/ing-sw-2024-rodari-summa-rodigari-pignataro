package it.polimi.ingsw.gc42.model.classes.game;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ChatMessage implements Serializable {
    private String sender;
    private String text;
    private LocalDateTime dateTime;

    /**
     * Constructor Method for ChatMessage
     * @param text the text of the ChatMessage
     * @param player the Player who sent the ChatMessage
     */
    public ChatMessage(String text, String player){
        this.text = text;
        this.sender = player;
        this.dateTime = LocalDateTime.now();
    }

    /**
     * Getter method for the sender
     * @return the sender of the ChatMessage
     */
    public String getSender() {
        return sender;
    }

    /**
     * Setter method for the sender
     * @param sender the sender of the ChatMessage
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Getter method for the text
     * @return the text of the ChatMessage
     */
    public String getText() {
        return text;
    }

    /**
     * Setter method for the text
     * @param text text of the ChatMessage
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Getter method for the dateTime
     * @return the dateTime of the ChatMessage
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Getter method for the dateTime
     * @param dateTime the dateTime of the ChatMessage
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
