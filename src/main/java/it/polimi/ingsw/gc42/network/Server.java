package it.polimi.ingsw.gc42.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Server {

    private String ip;
    private int port;
    private RmiController rmiController;
    // private SocketController socketController;

    public Server() {
        try{
            System.out.println("Constructing Server Implementation...");
            rmiController = new RmiController();
            System.out.println("Binding Server Implementation to registry...");
            // Temporarily creates a Socket with port 0 because it gets assigned a random available port
            ServerSocket serverSocket = new ServerSocket(0);
            // Use that port in the RMI connection
            int port = serverSocket.getLocalPort();
            // Closes the socket because it's not needed anymore
            serverSocket.close();
            // Uses that port in combination with the current IP Address to create an RMI Registry
            Registry registry = LocateRegistry.createRegistry(port);
            System.out.println("Open for connections at: " + InetAddress.getLocalHost().getHostAddress()
                    + ", " + port);

            System.out.println("Waiting for invocations from clients...");

            UnicastRemoteObject.exportObject(rmiController.getGameController(), port);
            registry.bind("gameController", rmiController.getGameController());
        } catch (IOException | AlreadyBoundException e){
            e.printStackTrace();
        }
        this.ip = ip;
        this.port = port;
    }


    public static void main(String[] args) throws IOException, AlreadyBoundException {        //try{
            System.out.println("Constructing Server Implementation...");
            try {
                RmiController rmiController = new RmiController();


            System.out.println("Binding Server Implementation to registry...");
            // Temporarily creates a Socket with port 0 because it gets assigned a random available port
            ServerSocket serverSocket = new ServerSocket(0);
            // Use that port in the RMI connection
            int port = serverSocket.getLocalPort();
            // Closes the socket because it's not needed anymore
            serverSocket.close();
            // Uses that port in combination with the current IP Address to create an RMI Registry
            Registry registry = LocateRegistry.createRegistry(port);
            System.out.println("Open for connections at: " + InetAddress.getLocalHost().getHostAddress()
                    + ", " + port);

            System.out.println("Waiting for invocations from clients...");

            //UnicastRemoteObject.exportObject(rmiController.getGameController(), port);
            registry.bind("gameController", rmiController.getGameController());
        /*} catch (IOException | AlreadyBoundException e){
            e.printStackTrace();
        }*/
        Scanner scanner = new Scanner(System.in);
        String a = scanner.nextLine();
            }catch (RemoteException e){
                e.printStackTrace();
            }

    }
}
