package cards;

import java.util.ArrayList;

public class Player {
    private int playerId;
    public static ArrayList<Integer> playerIds = new ArrayList<Integer>();
    private ArrayList<Card> playerHand = new ArrayList<>();
    private int preference;


    //TODO
    public Boolean checkCardValue() {
        return false;
    }
    public void discard() {}

    public void pickUpCard(Deck deck) {}

}
