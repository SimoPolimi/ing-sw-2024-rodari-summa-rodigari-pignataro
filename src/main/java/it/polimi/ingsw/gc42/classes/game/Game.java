package it.polimi.ingsw.gc42.classes.game;

import it.polimi.ingsw.gc42.exceptions.NoSuchDeckTypeException;
import it.polimi.ingsw.gc42.classes.cards.CardType;
import it.polimi.ingsw.gc42.interfaces.DeckListener;

import java.util.ArrayList;

public class Game implements DeckListener {

    private boolean isResourceDeckEmpty;
    private boolean isGoldDeckEmpty;

    private ArrayList<Player> players;

    public Game() {
        this.isResourceDeckEmpty = false;
        this.isGoldDeckEmpty = false;
    }

    public void startGame(){

    }

    public void endGame(){

    }

    public boolean addPlayers(Player player) {
        return false;
    }

    public boolean kickPlayers(Player player) {
        return false;
    }

    public boolean isResourceDeckEmpty() {
        return isResourceDeckEmpty;
    }

    public void setResourceDeckEmpty(boolean resourceDeckEmpty) {
        isResourceDeckEmpty = resourceDeckEmpty;
    }

    public boolean isGoldDeckEmpty() {
        return isGoldDeckEmpty;
    }

    public void setGoldDeckEmpty(boolean goldDeckEmpty) {
        isGoldDeckEmpty = goldDeckEmpty;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    @Override
    public void onDeckEmpty(CardType type) throws NoSuchDeckTypeException {
        switch (type) {
            case RESOURCECARD: setResourceDeckEmpty(true);
                break;
            case GOLDCARD: setGoldDeckEmpty(true);
                break;
            default: throw new NoSuchDeckTypeException("Incorrect Deck Type");
        }
    }
}
