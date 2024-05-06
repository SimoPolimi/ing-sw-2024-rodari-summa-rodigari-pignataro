package it.polimi.ingsw.gc42.network.interfaces;


import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.PlayingDeck;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.model.interfaces.Observable;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteObject extends Remote, Observable {


        boolean kickPlayer(Player player) throws RemoteException;

        void nextTurn() throws RemoteException;

        void playCard(PlayableCard card, int x, int y) throws RemoteException;

        void flipCard(Card card) throws RemoteException;

        void drawCard(Player player, PlayingDeck playingDeck) throws RemoteException;

        void grabCard(Player player, PlayingDeck playingDeck, int slot) throws RemoteException;

        void drawSecretObjectives() throws RemoteException;

        Game getGame() throws RemoteException;

        void addView(ViewController viewController) throws RemoteException;

        void addPlayer(Player player) throws RemoteException;

        void setCurrentStatus(GameStatus status) throws RemoteException;
        GameStatus getCurrentStatus() throws RemoteException;

        String getName() throws RemoteException;
        void setName(String name) throws RemoteException;

        //TODO: remove after test
        public String test(int a) throws RemoteException;
}

