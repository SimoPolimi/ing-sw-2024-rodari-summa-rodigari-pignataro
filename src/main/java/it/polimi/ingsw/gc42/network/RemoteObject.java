package it.polimi.ingsw.gc42.network;


import it.polimi.ingsw.gc42.controller.GameStatus;
import it.polimi.ingsw.gc42.model.classes.PlayingDeck;
import it.polimi.ingsw.gc42.model.classes.cards.Card;
import it.polimi.ingsw.gc42.model.classes.cards.PlayableCard;
import it.polimi.ingsw.gc42.model.classes.game.Game;
import it.polimi.ingsw.gc42.model.classes.game.Player;
import it.polimi.ingsw.gc42.view.Interfaces.ViewController;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RemoteObject extends Remote {


      /*  public boolean kickPlayer(Player player) throws RemoteException;

        public void nextTurn() throws RemoteException;

        public void playCard(PlayableCard card, int x, int y) throws RemoteException;

        public void flipCard(Card card) throws RemoteException;

        public void drawCard(Player player, PlayingDeck playingDeck) throws RemoteException;

        public void grabCard(Player player, PlayingDeck playingDeck, int slot) throws RemoteException;

        public void drawSecretObjectives() throws RemoteException;*/

        //TODO: remove after test
        public String test(int a) throws RemoteException;
}

