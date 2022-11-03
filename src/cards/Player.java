package cards;

import java.util.ArrayList;

public class Player {
    private int playerId;
    public static ArrayList<Integer> playerIds = new ArrayList<Integer>();
    private ArrayList<Card> playerHand = new ArrayList<>();
    private int preference;

    //Constructor
    public Player() {
        if(playerIds.size() == 0) {
            playerId = 1;
        } else {
            playerId = playerIds.get(playerIds.size() - 1) + 1;
        }
        playerIds.add(playerId);
    }

    public int getPlayerId() {
        return playerId;
    }

    public static ArrayList<Integer> getPlayerIds() {
        return playerIds;
    }

    public void addToHand(Card card) {
        playerHand.add(card);
    }

    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }

    //TODO
    public Boolean checkCardValue() {
        return false;
    }
    public void discard() {}
    public void pickUpCard(Deck deck) {}


}
