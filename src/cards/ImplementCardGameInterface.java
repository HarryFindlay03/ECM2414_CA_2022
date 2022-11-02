package cards;

import java.util.Stack;

public class ImplementCardGameInterface implements CardGameInterface {
    int numPlayers;
    String filename;

    public ImplementCardGameInterface(int numPlayers, String filename) {
        this.numPlayers = numPlayers;
        this.filename = filename;
    }
    @Override
    public int createPack() throws InvalidPackException {
        Pack pack = new Pack(numPlayers, filename);
        return pack.getPackLength();
    }

    @Override
    public int createPlayer() {
        Player player = new Player();
        return player.getPlayerId();
    }

    @Override
    public Deck createDeck() {
        return new Deck();
    }
}
