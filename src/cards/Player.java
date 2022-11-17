package cards;

import java.util.ArrayList;

public class Player {
    private int playerId;
    private ArrayList<Card> playerHand = new ArrayList<>();
    private int preference;

    //Constructor
    public Player(int playerId) {
        this.playerId = playerId;
        this.preference = playerId;
    }

    public int getPlayerId() {
        return playerId;
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

    public String getPlayerHandString() {
        String strPlayerHand = "";
        for(Card c : playerHand) {
            strPlayerHand += String.format(" %d", c.getCardValue());
        }

        return strPlayerHand;
    }

    public int getPreference() {
        return preference;
    }
    public void setWinningPlayerHand() {
        playerHand.clear();
        for( int i = 0; i < 4; i++ ) {
            playerHand.add(new Card(1));
        }
    }



    //TODO
    public Boolean checkCardValue() {
        return false;
    }
    public void discard() {}
    public void pickUpCard(Deck deck) {}


}
