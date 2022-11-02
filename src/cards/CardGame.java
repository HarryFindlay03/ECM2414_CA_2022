package cards;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class CardGame {
    private int numPlayers;
    private int deckLength;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("hello, world!");
    }

    public CardGame(int numPlayers, String filename) throws InvalidPackException{
        if(numPlayers <= 0) {
            throw new InvalidPackException("Number of players needs to be greater than 0");
        }
        this.numPlayers = numPlayers;
        this.deckLength = ((8 * numPlayers) - (4 * numPlayers)) / 4;
    }

    //setup game
    public void gameSetup() {
        for(int i = 0; i < numPlayers; i++) {
            createPlayer();
            createDeck();
        }
    }

    //create players
    public int createPlayer() {
        Player player = new Player();
        return player.getPlayerId();
    }

    //create decks
    public int createDeck() {
        Deck deck = new Deck(deckLength);
        return deck.getDeckId();
    }
}
