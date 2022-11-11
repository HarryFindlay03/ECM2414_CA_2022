package cards;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Deck {
    private int deckId;
    private static ArrayList<Integer> deckIds = new ArrayList<Integer>();
    private ArrayList<Card> deckCards = new ArrayList<Card>();

    public Deck() {
        if(deckIds.size() == 0) {
            deckId = 1;
        } else {
            deckId = deckIds.get(deckIds.size() - 1) + 1;
        }
        deckIds.add(deckId);
    }

    /**
     * Removes the first card from the deck for a player to pickup.
     * @return
     */
    public Card removeCard() {
        return deckCards.remove(0);
    }

    /**
     * Adds card to deck that has been removed from player hand
     * @param card Card that has been discarded from player hand.
     */
    public void addCard(Card card) {
        deckCards.add(card);
    }

    public int getDeckId() {
        return deckId;
    }

    public ArrayList<Card> getDeckCards() {
        return deckCards;
    }

    public static ArrayList<Integer> getDeckIds() {
        return deckIds;
    }

    public void addToDeckCards(Card card) {
        deckCards.add(card);
    }
}
