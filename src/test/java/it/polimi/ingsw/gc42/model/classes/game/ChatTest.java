package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.network.messages.Message;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ChatTest {

    @Test
    void sendMessage() {
        Chat chat = new Chat();
        Player player = new Player(Token.BLUE);
        ChatMessage message = new ChatMessage("a", null);
        // For coverage
        message.setSender(player.getNickname());
        message.setText("test");
        LocalDateTime date = LocalDateTime.now();
        message.setDateTime(date);
        chat.sendMessage(message);

        assertEquals(chat.getLastChatMessage().getText(), "test");
        assertEquals(chat.getLastChatMessage().getSender(), player.getNickname());
        assertEquals(chat.getLastChatMessage().getDateTime(), date);
        assertDoesNotThrow(() -> chat.getChatMessage(chat.getChatSize() - 1));
    }

    @Test
    void getFullChat(){
        Chat chat = new Chat();
        Player player = new Player(Token.BLUE);
        ChatMessage message = new ChatMessage("a", player.getNickname());
        chat.sendMessage(message);
        ChatMessage message2 = new ChatMessage("b", player.getNickname());
        chat.sendMessage(message2);

        ArrayList<ChatMessage>fullChat = new ArrayList<>(chat.getFullChat());
        assert(fullChat.contains(message));
        assert(fullChat.contains(message2));
    }
}