package com.company.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class Player extends Thread {
    TreeSet<Integer> cards = new TreeSet<>();
    private int id, speed;
    Game game;

    public Set<Integer> getCards() {
        return cards;
    }

    public void setCards(Set<Integer> cards) {
        this.cards = new TreeSet<>();
    }

    public int getID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Player(int id, int speed, Game game) {
        this.cards = new TreeSet<>();
        this.id = id;
        this.speed = speed;
        this.game = game;
    }

    @Override
    public void run() {
        int top = game.topCard;
        int min = 0;
        int current = 0;
        if (cards.size() > 0) current = this.cards.first();
        try {
            if (current > 0 && current > top) {
                Thread.sleep(speed * (current - top));
                cards.remove(current);
                game.play(this, current);
            }else {
                if (game.isEnd())
                    game.nextRound();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addCard(Integer x) {
        cards.add(x);
    }

    public int getSize() {
        return cards.size();
    }

    public int getFirst() {
        return cards.first();
    }
    public void removeFirst() {
        cards.remove(cards.first());
    }

    public void forceRun() throws InterruptedException {
        if (this.getSize() > 0) {
            int card = cards.first();
            cards.remove(card);
            game.play(this, card);
        }
    }
}
