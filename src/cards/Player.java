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
            playerId = 0;
        } else {
            playerId = playerIds.get(playerIds.size() - 1) + 1;
        }
        playerIds.add(playerId);

        this.preference = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public static ArrayList<Integer> getPlayerIds() {
        return playerIds;
    }

    /**
     * addToHand method populates the player hand during the game setup phase,
     * this is not to be used during gameplay.
     * @param card
     */
    public void addToHand(Card card) {
        playerHand.add(card);
    }

    public void removeFromHand(Card card) {
        playerHand.remove(card);
    }

    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }

    public int getPreference() {
        return preference;
    }

    //TODO
    public Boolean checkCardValue() {
        return false;
    }
    public void discard() {}
    public void pickUpCard(Deck deck) {}


}
