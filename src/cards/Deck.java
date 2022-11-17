package cards;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Deck {
    private int deckId;
    private ArrayList<Card> deckCards = new ArrayList<Card>();

    public Deck(int deckId) {
        this.deckId = deckId;
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

    public String getDeckCardsString() {
        String returnStr = "";
        for(Card c : deckCards) {
            returnStr += String.format(" %d", c.getCardValue());
        }

        return returnStr;
    }

    public void addToDeckCards(Card card) {
        deckCards.add(card);
    }
}
