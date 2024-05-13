package it.polimi.ingsw.gc42.model.classes.game;

import java.io.Serializable;
import java.util.ArrayList;

public class Chat implements Serializable {
    private final ArrayList<ChatMessage> messages = new ArrayList<>();

    public void sendMessage(ChatMessage mes){
        this.messages.add(mes);
    }
    public ChatMessage getLastMessage(){
        return messages.getLast();
    }
    public ChatMessage getMessage(int index) throws IllegalArgumentException {
        if (index > 0 && index < messages.size()) {
            return messages.get(index);
        } else throw new IllegalArgumentException();
    }
    public int getChatSize(){
        return messages.size();
    }
}
