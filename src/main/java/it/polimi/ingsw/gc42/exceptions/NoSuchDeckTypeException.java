package it.polimi.ingsw.gc42.exceptions;

/**
 * Custom Exception
 * It signals that a non-ResourceCard and non-GoldCard Deck tried to notify its empty status
 */
public class NoSuchDeckTypeException extends Exception{
    /**
     * Constructor Message
     * @param message: String to explain the context of the Exception
     */
    public NoSuchDeckTypeException(String message) {
        super(message);
    }
}
