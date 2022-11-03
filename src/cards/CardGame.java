package cards;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;

public class CardGame {
    private int numPlayers;
    private int deckLength;

    //potentially each card game has a list of playerids and deck ids associated with it.

    private Stack<Integer> pack;

    public CardGame(int numPlayers, String filename) throws InvalidPackException, FileNotFoundException{
        if(numPlayers <= 0) {
            throw new InvalidPackException("Number of players needs to be greater than 0");
        }
        this.numPlayers = numPlayers;
        this.deckLength = ((8 * numPlayers) - (4 * numPlayers)) / 4;

        //getting the pack
        //checking the pack
        try {
            this.pack = ReadFile.getStack(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FileNotFoundException("File not found!");
        }
    }

    //Inner class readfile
    class ReadFile {
        public static Stack<Integer> getStack(String filename) throws FileNotFoundException {
            Stack<Integer> pack = new Stack<Integer>();
            File f = new File(filename);
            Scanner sc = new Scanner(f);

            while(sc.hasNextLine()) {
                String data = sc.nextLine();
                pack.push(Integer.parseInt(data));
            }

            sc.close();
            return pack;

        }
    }

    //setup game
    public void gameSetup() {
        for(int i = 0; i < numPlayers; i++) {
            createPlayer();
            createDeck();
        }

        //Generate a Pack and populate decks
        /*
        Pack class:
            Read file into a stack
        - Distribute cards from the pack into decks in a round robin fashion
         */
    }

    //create players
    public int createPlayer() {
        Player player = new Player();
        return player.getPlayerId();
    }

    //create decks
    public int createDeck() {
        Deck deck = new Deck(deckLength);
        return deck.getDeckId();
    }

    //GETTER METHODS
    public Stack<Integer> getPack() {
        return pack;
    }

    //MAIN EXECUTABLE METHOD
    public static void main(String[] args) throws InvalidPackException, FileNotFoundException{
        CardGame cg = new CardGame(4, "packs/4.txt");
    }
}
