package cards;

import java.util.ArrayList;

public interface CardGameInterface {
    ArrayList<Integer> createPack();

    Player createPlayer();

    Deck createDeck();

    
}
