package cards;

import java.util.ArrayList;

public class Card {
    public int cardId;
    public static ArrayList<Integer> cardIds = new ArrayList<Integer>();
    private int cardValue;

    public Card(int cardValue) {
         this.cardValue = cardValue;
    }

    public int getCardValue() {
        return cardValue;
    }
}
