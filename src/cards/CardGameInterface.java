package cards;

import java.util.ArrayList;
import java.util.Stack;

public interface CardGameInterface {
    int createPack() throws InvalidPackException;

    /*return player id*/
    int createPlayer();

    Deck createDeck();




}
