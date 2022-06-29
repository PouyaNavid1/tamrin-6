package com.company.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final ArrayList<ClientHandler> clientHandlers;
    private ServerStatus status;
    int numberOfPlayers = 4;
    Game game;

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public Server(ArrayList<ClientHandler> clientHandlers) {
        this.clientHandlers = clientHandlers;
        status = ServerStatus.WAITING;
    }
    public Server() {
        this.clientHandlers = new ArrayList<>();
        status = ServerStatus.WAITING;
    }
    public void init() {
        System.out.println("Server is running...");
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            while (true) {
                System.out.println("Waiting for a connection...");
                Socket socket = serverSocket.accept();
                System.out.println("server : " + socket);

                addNewClientHandler(socket);
                System.out.println("====> There are " + clientHandlers.size() + " clients on the server!");

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addNewClientHandler(Socket socket) throws IOException, InterruptedException {
        ClientHandler clientHandler = new ClientHandler(socket, clientHandlers.size(), game, this);
        if (status == ServerStatus.WAITING) {
            System.out.println("New connection accepted!");
            if (clientHandlers.size() == 0) {
                clientHandler.isHost = true;
                clientHandler.sendMessage("Welcome, You are host!");
                clientHandler.sendMessage("Enter the number of players : ");
                clientHandler.sendMessage("Enter number 1 to start the game!");

            }
            else {
                clientHandler.sendMessage("Welcome, You are ID is : " + clientHandlers.size());
            }
            clientHandlers.add(clientHandler);
            new Thread(clientHandler).start();

           // if (clientHandlers.size() == numberOfPlayers) {

        } else {
            //clientHandler.sendMessage("Server is full!");
            //clientHandler.kill();
        }

    }

    public void startGame() throws InterruptedException {
        System.out.println("Game is started!");
        status = ServerStatus.STARTED;
        numberOfPlayers = clientHandlers.get(0).number;
        game = new Game(numberOfPlayers, clientHandlers.size(), clientHandlers, this);
        game.Start();
        game.print();
    }
    public void sendToAll(String print) {
        for (ClientHandler clientHandler : clientHandlers)
            clientHandler.sendMessage(print);
    }
}
enum ServerStatus {
    WAITING, STARTED
}