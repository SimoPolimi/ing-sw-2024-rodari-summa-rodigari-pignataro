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

    /**
     * Gets the ChatMessage at the specified index
     * @param index the index of the ChatMessage
     * @return the ChatMessage at the specified index
     * @throws IllegalArgumentException if the index is out of bounds
     */
    public ChatMessage getChatMessage(int index) throws IllegalArgumentException {
        if (index >= 0 && index < messages.size()) {
            return messages.get(index);
        } else throw new IllegalArgumentException();
    }

    /**
     * Gets the last ChatMessage
     * @return the last ChatMessage
     */
    public ChatMessage getLastChatMessage(){
        return messages.getLast();
    }

    /**
     * Getter Method for the size of the Chat
     * @return the size of the Chat
     */
    public int getChatSize(){
        return messages.size();
    }

    /**
     * Gets the list of all ChatMessages
     * @return a copy of the list of all ChatMessages
     */
    public ArrayList<ChatMessage> getFullChat(){
        // Returns a copy
        return new ArrayList<>(messages);
    }

    // Methods

    /**
     * Sends the specified ChatMessage
     * @param message the ChatMessage to be sent
     */
    public void sendMessage(ChatMessage message){
        addMessage(message);
    }

    /**
     * Adds the specified ChatMessage to the ChatMessage list
     * @param message the specified ChatMessage to be added
     */
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
