package it.polimi.ingsw.gc42.model.classes.game;

import java.util.ArrayList;

public class Chat {
    private final ArrayList<Message> message = new ArrayList<>();

    public void sendMessage(Message mes){
        this.message.add(mes);
    }
    public Message getLastMessage(){
        return message.getLast();
    }
    public Message getMessage(int index) throws IllegalArgumentException {
        if (index > 0 && index < message.size()) {
            return message.get(index);
        } else throw new IllegalArgumentException();
    }
    public int getChatSize(){
        return message.size();
    }
}
