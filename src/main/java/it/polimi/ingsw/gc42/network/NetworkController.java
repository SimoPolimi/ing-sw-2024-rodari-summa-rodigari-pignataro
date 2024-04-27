package it.polimi.ingsw.gc42.network;

import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.PlayingDeck;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface NetworkController {
    public void connect() throws RemoteException, NotBoundException;
    public void disconnect();
    public boolean isConnected();

    public Game getGame();

    public boolean kickPlayer(Player player);

    public void nextTurn();

    public void playCard(PlayableCard card, int x, int y);

    public void flipCard(Card card);

    public void drawCard(Player player, PlayingDeck playingDeck);

    public void grabCard(Player player, PlayingDeck playingDeck, int slot);

    public void drawSecretObjectives();

    public void addView(ViewController viewController);

    public void addPlayer(Player player);

    public void setCurrentStatus(GameStatus status);


}
