package it.polimi.ingsw.gc42.model.classes.game;

import java.util.ArrayList;

public class Chat {
    private final ArrayList<Message> messages = new ArrayList<>();

    public void sendMessage(Message mes){
        this.messages.add(mes);
    }
    public Message getLastMessage(){
        return messages.getLast();
    }
    public Message getMessage(int index) throws IllegalArgumentException {
        if (index > 0 && index < messages.size()) {
            return messages.get(index);
        } else throw new IllegalArgumentException();
    }
    public int getChatSize(){
        return messages.size();
    }
}
