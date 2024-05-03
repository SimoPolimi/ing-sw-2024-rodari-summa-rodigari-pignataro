package it.polimi.ingsw.gc42.controller.network;

import it.polimi.ingsw.gc42.model.classes.game.Game;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client implements Remote {

    private RemoteObject gameController;

    public Client() {

    }

    public void connect(String ip, int port) throws RemoteException {
        try{
            Registry registry = LocateRegistry.getRegistry(ip, port);
            gameController = (RemoteObject) registry.lookup("GameController");
        }catch (NotBoundException e){
            throw new RuntimeException(e);
        }
    }

    public void disconnect() throws RemoteException {
        // TODO: write method
    }

    public static void main(String args[]) throws RemoteException {
        System.out.println("Sono il Client");
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter Server IP Address:");
            String ip = scanner.nextLine();
            System.out.println("Enter Server Port:");
            int port = scanner.nextInt();
            Registry registry = LocateRegistry.getRegistry(ip, port);
            RemoteObject gameController1 = (RemoteObject) registry.lookup("GameController");
            //System.out.println(gameController1.test(1));
            Game game = gameController1.getGame();
        } catch (NotBoundException  e) {
            throw new RuntimeException(e);
        }
    }
}