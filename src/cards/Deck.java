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
