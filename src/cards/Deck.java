package cards;

import java.util.ArrayList;

public class Deck {
    private int deckId;
    private static ArrayList<Integer> deckIds = new ArrayList<Integer>();
    private int deckLength;

    public Deck(int deckLength) {
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

    public int getDeckLength() {
        return deckLength;
    }

    public static ArrayList<Integer> getDeckIds() {
        return deckIds;
    }
}
