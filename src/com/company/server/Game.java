package com.company.server;

import com.company.client.Client;

import java.util.*;

public class Game {
    ArrayList<Player> players;
    ArrayList<ClientHandler> clientHandlers;
    ArrayList<Integer> cards;
    int turn, ninga, heart, numbers, numberOfClient;
    int topCard;
    Server server;

    public void check(int current) {
        boolean isSmaller = false;
        for (Player p : players) {
            while (p.getSize() > 0 && p.getFirst() < current) {
                p.removeFirst();
                isSmaller = true;
            }
        }
        for (ClientHandler p : clientHandlers) {
            while (p.getSize() > 0 && p.getFirst() < current) {
                p.removeFirst();
                isSmaller = true;
            }
        }
        if (isSmaller)
            heart --;
    }
    public synchronized void play(Player player, int current) throws InterruptedException {

        check(current);

        topCard = current;
        server.sendToAll(print());
        if (isEnd()) {
            server.sendToAll(print());
        }
    }
    public void play(ClientHandler clientHandler, int current) throws InterruptedException {
        check(current);
        topCard = current;
        /*
        for (Player p : players)
            p.interrupt();
        topCard = current;
        for (Player p : players)
            p.start();

         */
        server.sendToAll(print());
        if (isEnd()) {
            server.sendToAll(print());
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<Integer> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Integer> cards) {
        this.cards = cards;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getNinga() {
        return ninga;
    }

    public void setNinga(int ninga) {
        this.ninga = ninga;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public int getTopCard() {
        return topCard;
    }

    public void setTopCard(int topCard) {
        this.topCard = topCard;
    }

    public Game(int numbers, int numberOfClient, ArrayList<ClientHandler> clientHandlers, Server server) throws InterruptedException {
        this.turn = 0;
        this.ninga = 2;
        this.numbers = numbers;
        this.heart = numbers;
        this.numberOfClient = clientHandlers.size();
        this.server = server;
        players = new ArrayList<>();
        this.clientHandlers = clientHandlers;
        for (int i = 0; i < numbers - numberOfClient; i ++)
            players.add(new Player(i, i * 10 + 20 , this));
        this.cards = new ArrayList<>();
        for (int i = 1; i <= 100; i ++)
            cards.add(i);
        Collections.shuffle(cards);
        nextRound();
        for (Player player : players)
            new Thread(player).start();

    }
    public void Start() throws InterruptedException {
        server.sendToAll(print());
        /*
        while (turn <= 12) {
            while (!isEnd()) {
                int xx = 0;
                xx ++;
            }
            nextRound();
        }

         */
    }
    public boolean isEnd() {
        System.out.println("*");
        boolean flag = true;
        for (Player player : players)
            if (player.getSize() > 0)
                flag = false;
        for (ClientHandler player : clientHandlers)
            if (player.getSize() > 0)
                flag = false;
        System.out.println(flag);
        return flag;
    }
    public void nextRound() throws InterruptedException {
        server.sendToAll(print());
        turn ++;
        if (heart == 0) lose();
        if (turn > 12) win();
        if (turn % 3 == 0) heart ++;
        if (turn % 3 == 2 && turn < 11) ninga ++;
        topCard = 0;
        for (Player player : players)
            player.cards.clear();
        for (ClientHandler clientHandler : clientHandlers)
            clientHandler.cards.clear();
        Collections.shuffle(cards);
        int id = 0;
        for (int i = 0; i < turn; i ++)
            for (Player player : players)
                player.addCard(cards.get(id++));
        for (int i = 0; i < turn; i ++)
            for (ClientHandler clientHandler : clientHandlers)
                clientHandler.addCard(cards.get(id++));
        server.sendToAll(print());

    }
    public void ninga() throws InterruptedException {
        for (Player p : players)
            p.forceRun();
        for (ClientHandler p : clientHandlers)
            p.forceRun();
    }
    /*
    public void print() {
        System.out.println("turn : " + turn + " | heart : " + heart + " | ninga : " + ninga);
        for (Player player : players)
            System.out.println("player " + player.getId() + " : " + "number of cards : " + player.getSize());
        System.out.print("Your Cards : ");
        for (Player player : players)
            if (player.getId() == 0)
                for (Integer x : player.getCards())
                    System.out.print(x + ", ");
        System.out.println();
        System.out.println("top card : " + topCard);
        System.out.println("--------------------");
    }

     */
    public String print() {
        String out = "";
        out += ("turn : " + turn + " | heart : " + heart + " | ninga : " + ninga);
        out += " / ";
        for (Player player : players)
            out += ("player " + player.getId() + " : " + "number of cards : " + player.getSize()) + " / ";
        out += ("Your Cards : ");
        for (ClientHandler clientHandler : clientHandlers)
            for (Integer x : clientHandler.getCards())
                    out += (x + ", ");
        out += " / ";
        out += ("top card : " + topCard) + " / ";
        return out;
    }
    public void lose() {
        server.sendToAll("You Lose!");
        System.out.println("You Lose!");
    }
    public void win() {
        server.sendToAll("You Win!");
        System.out.println("You Win!");
    }
}
