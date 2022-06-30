package com.company.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class ClientHandler implements Runnable{
    private final Socket socket;
    private final PrintWriter out;
    private final int id;
    boolean isHost = false;
    int number;
    TreeSet<Integer> cards = new TreeSet<>();
    Game game;
    Server server;

    public ClientHandler(Socket socket, int id, Game game, Server server) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream());
        this.id = id;
        this.game = game;
        this.server = server;
    }
    public Set<Integer> getCards() {
        return cards;
    }

    public void setCards(Set<Integer> cards) {
        this.cards = new TreeSet<>();
    }

    public int getId() {
        return id;
    }

    public void addCard(Integer x) {
        cards.add(x);
    }

    public int getSize() {
        return cards.size();
    }

    @Override
    public void run() {
        System.out.println("New ClientHandler is running...");
        try {
            Scanner in = new Scanner(socket.getInputStream());
            number = Integer.parseInt(in.nextLine());
            System.out.println("6");
            int start = Integer.parseInt(in.nextLine());
            sendMessage("1.play");
            System.out.println("54");
            sendMessage("2.ninga");
            if (start == 1 && isHost)
                server.startGame();
            while (true) {
                //System.out.println("ClientHandler is waiting for a message...");
                String messageFromClient = in.nextLine();
                if (messageFromClient.equals("1"))
                    go();
                if (messageFromClient.equals("2"))
                    game.ninga();
                //System.out.println("Message from Client: " + messageFromClient);
                //sendMessage("You are client number " + id);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void forceRun() throws InterruptedException {
        if (this.getSize() > 0) {
            int card = cards.first();
            cards.remove(card);
            game.play(this, card);
        }
    }
    public int getFirst() {
        if (getSize() == 0) return 0;
        return cards.first();
    }
    public void removeFirst() {
        if (getSize() > 0 ) cards.remove(cards.first());
    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }
    public void go() throws InterruptedException {
        System.out.println("l : " + game.ninga);
        if (getSize() > 0) {
            cards.remove(getFirst());
            game.play(this, getFirst());
        }
    }
}
