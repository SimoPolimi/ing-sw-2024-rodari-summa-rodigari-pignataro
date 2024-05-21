package it.polimi.ingsw.gc42.model.classes.game;

import it.polimi.ingsw.gc42.model.exceptions.IllegalActionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class ChatTest {

    @Test
    void sendMessage() {
        Chat chat = new Chat();
        Player player = new Player(Token.BLUE);
        chat.sendMessage(new ChatMessage("test", player));

        assertEquals(chat.getLastChatMessage().getText(), "test");
        assertEquals(chat.getLastChatMessage().getSender(), player);
        assertDoesNotThrow(() -> chat.getChatMessage(chat.getChatSize() - 1));
    }
}