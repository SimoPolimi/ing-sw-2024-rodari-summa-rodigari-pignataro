package it.polimi.ingsw.gc42.model.classes.game;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ChatTest {

    @Test
    void sendMessage() {
        Chat chat = new Chat();
        Player player = new Player(Token.BLUE);
        ChatMessage message = new ChatMessage("a", null);
        // For coverage
        message.setSender(player);
        message.setText("test");
        message.setDateTime(LocalDateTime.now());
        chat.sendMessage(message);

        assertEquals(chat.getLastChatMessage().getText(), "test");
        assertEquals(chat.getLastChatMessage().getSender(), player);
        assertEquals(chat.getLastChatMessage().getDateTime(), LocalDateTime.now());
        assertDoesNotThrow(() -> chat.getChatMessage(chat.getChatSize() - 1));
    }
}