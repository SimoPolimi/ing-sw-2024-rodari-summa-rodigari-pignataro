package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.model.interfaces.Listener;
import it.polimi.ingsw.gc42.model.interfaces.Observable;

import java.io.Serializable;
import java.util.ArrayList;

public class Chat implements Serializable, Observable {
    // Attributes
    private final ArrayList<Listener> listeners = new ArrayList<>();

    private final ArrayList<ChatMessage> messages = new ArrayList<>();

    // Getters and Setters

    public ChatMessage getChatMessage(int index) throws IllegalArgumentException {
        if (index >= 0 && index < messages.size()) {
            return messages.get(index);
        } else throw new IllegalArgumentException();
    }

    public ChatMessage getLastChatMessage(){
        return messages.getLast();
    }

    public int getChatSize(){
        return messages.size();
    }

    public ArrayList<ChatMessage> getFullChat(){
        // Returns a copy
        return new ArrayList<>(messages);
    }

    // Methods
    public void sendMessage(ChatMessage message){
        addMessage(message);
    }

    private void addMessage(ChatMessage message){
        this.messages.add(message);
        notifyListeners("New Message");
    }

    // Observable Interface Methods
    @Override
    public void setListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners(String context) {
        for (Listener l: listeners) {
            l.onEvent();
        }
    }
}
