package it.polimi.ingsw.gc42.model.classes.game;

import java.io.Serializable;
import java.util.ArrayList;

public class Chat implements Serializable {
    private final ArrayList<ChatMessage> messages = new ArrayList<>();

    public void sendMessage(ChatMessage message){
        this.messages.add(message);
    }
    public ChatMessage getLastChatMessage(){
        return messages.getLast();
    }
    public ChatMessage getChatMessage(int index) throws IllegalArgumentException {
        if (index >= 0 && index < messages.size()) {
            return messages.get(index);
        } else throw new IllegalArgumentException();
    }
    public int getChatSize(){
        return messages.size();
    }
}
