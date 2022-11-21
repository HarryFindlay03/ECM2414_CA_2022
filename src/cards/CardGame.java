package cards;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.io.FileNotFoundException;

public class CardGame {
    private int numPlayers;

    private ArrayList<Player> playersInGame = new ArrayList<Player>();
    private ArrayList<Deck> decksInGame = new ArrayList<Deck>();

    private volatile Player winningPlayer = null;


    private Stack<Integer> pack;

    /**
     * Creates a new CardGame instance, numPlayers and filename should be validated before a new CardGame instance
     * is created.
     * @param numPlayers The number of players in the game.
     * @param filename The location of the pack file to play with.
     * @throws InvalidPackException
     * @throws FileNotFoundException
     */
    public CardGame(int numPlayers, String filename) throws InvalidPackException, FileNotFoundException{
        if(numPlayers <= 0) {
            throw new InvalidPackException("Number of players needs to be greater than 0");
        }
        this.numPlayers = numPlayers;

        try {
            this.pack = FileHandler.getStack(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Method that sets up the initial game state.
     * This involves clearing the files in the playerfiles and deckfiles and creating n players and n decks,
     * and distributing the pack to each of the players then each of the decks, both respectively in a round-robin format.
     * (n is the inputted number of players)
     */
    public void gameSetup() {
        //clear files in the deck and player files directories.
        FileHandler.clearFiles("./output-files/playerfiles");
        FileHandler.clearFiles("./output-files/deckfiles");

        //Create new players and decks and corresponding files for them
        for(int i = 0; i < numPlayers; i++) {
            int newPlayerId = createPlayer(i + 1); //i+1 as playerIds are 1 indexed
            FileHandler playerFile = new FileHandler(String.format("output-files/playerfiles/player%d_output.txt", newPlayerId));
            playerFile.createFile();

            int newDeckId = createDeck(i + 1); //i+1 as deckIds are 1 indexed
            FileHandler deckFile = new FileHandler(String.format("output-files/deckfiles/deck%d_output.txt", newDeckId));
            deckFile.createFile();

        }
        addToPlayers();
        addToDecks();
    }

    /**
     * Starts the required threads for the CardGame.
     */
    public void gameRun() {
        //Player threads
        for(int i = 0; i < numPlayers; i++) {
            Thread t = new Thread(new PlayerThread(this));
            t.setName(Integer.toString(i));
            t.start();
        }
    }

    /**
     * Deals out cards to n players in the game, as well as writing to relevant player files the intial hand of each player.
     * These files can be found in src/cards/playerfiles/Player[n].txt where n is the playerId.
     */
    public void addToPlayers() {
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < numPlayers; j++) {
                Player player = playersInGame.get(j);
                Card card = new Card(pack.pop());
                player.addToHand(card);
            }
        }

        for(Player player : playersInGame) {
            try {
                FileWriter writer = new FileWriter(String.format("output-files/playerfiles/player%d_output.txt", player.getPlayerId()));
                writer.write(String.format("Player %d initial hand %s\n", player.getPlayerId(), player.getPlayerHandString()));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method deals out the remaining cards in the pack (after being dealt to players)
     * to the n decks that are in the game.
     */
    public void addToDecks() {
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < numPlayers; j++) {
                Deck deck = decksInGame.get(j);
                Card card = new Card(pack.pop());
                deck.addToDeckCards(card);
            }
        }
    }


    /**
     * Method that creates a new Player instance in the card game.
     * This method also adds each new Player created to a list of players in game that is an
     * instance attribute of the CardGame class.
     * @return The newly created Player's deckId.
     */
    public int createPlayer(int playerId) {
        Player player = new Player(playerId);
        playersInGame.add(player);
        return player.getPlayerId();
    }

    /**
     * Method that creates a new Deck instance in the card game.
     * This method also adds each new Deck created to a list of decks in game that is an
     * instance attribute of the CardGame class.
     * @return The newly created Deck's deckId.
     */
    public int createDeck(int deckId) {
        Deck deck = new Deck(deckId);
        decksInGame.add(deck);
        return deck.getDeckId();
    }

    //GAMEPLAY methods

    /**
     * Picks up a card from deck i for player i.
     * @param player The Player instance that is picking up.
     * @return The Card instance that has been picked up.
     */
    public synchronized Card pickUpCard(Player player) {
        //get deck
        Deck deck = decksInGame.get(player.getPlayerId() - 1);
        //remove from deck, add to player hand
        Card card = deck.removeCard();
        player.addToHand(card);
        return card;
    }

    /**
     * Method that removes a card from a players hand
     * @param player Player instance that you want to remove a card from.
     * @return The Card instance that has been removed from the Player instance's hand.
     */
    public synchronized Card discardCard(Player player) {
        //get deck
        Deck deck;
        if(player.getPlayerId() + 1 > numPlayers) {
            deck = decksInGame.get(0);
        }
        else {
            deck = decksInGame.get(player.getPlayerId());
        }

        //find card to discard
        Card cardToDiscard = getCardToDiscard(player);

        //remove from player hand, add to deck;
        player.removeFromHand(cardToDiscard);
        deck.addCard(cardToDiscard);

        return cardToDiscard;
    }

    /**
     * Checks a players preference, then returns the Card object to discard from the Players hand.
     * This method works off rules from the CA strategy that we has been tasked to implement.
     * @param player Player object that you are checking the hand of.
     * @return Card object that should be discarded from player hand.
     */
    public Card getCardToDiscard(Player player) {
        //random num generator
        Random random = new Random();

        //get player hand
        ArrayList<Card> playerHand = player.getPlayerHand();

        //preference being playerId.
        int pref = player.getPlayerId();
        //look through hand, get indexes of cards that do not match preference.
        ArrayList<Integer> notPref = new ArrayList<Integer>();

        //player hand size is always 5 when deciding to discard.
        for(int i = 0; i < 5; i++) {
            if(playerHand.get(i).getCardValue() != pref) {
                notPref.add(i);
            }
        }
        //generate random number
        int randomIndex = random.nextInt(0, notPref.size());
        //notpref is the indexes in the playerHand that are not of preference
        Card cardToDiscard = playerHand.get(notPref.get(randomIndex));
        return cardToDiscard;
    }

    /**
     * Checks an inputted Player objects hand to see if it a winning hand.
     * @param player Player object that you want to check for a winning hand.
     * @return Boolean value depending on whether the hand is a 'winning' hand.
     */
    public Boolean checkWin(Player player) {
        ArrayList<Card> playerHand = player.getPlayerHand();

        if(playerHand.size() != 4) {
            return false;
        }

        for(Card card : playerHand) {
            if (card.getCardValue() != playerHand.get(0).getCardValue()) {
                return false;
            }
        }
        return true;
    }

    //GETTER METHODS
    public Stack<Integer> getPack() {
        return pack;
    }

    public ArrayList<Player> getPlayersInGame() {
        return playersInGame;
    }

    public ArrayList<Deck> getDecksInGame() {
        return decksInGame;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public Player getWinningPlayer() {
        return winningPlayer;
    }

    /**
     * Sets the CardGame object's winning player to the first player that has declared it has
     * won.
     * @param winningPlayer Player that has been passed by PlayerThread.
     */
    public void setWinningPlayer(Player winningPlayer) {
        this.winningPlayer = winningPlayer;
    }

    //MAIN EXECUTABLE METHOD
    public static void main(String[] args) throws InvalidPackException, FileNotFoundException{
        Scanner sc = new Scanner(System.in);
        int numPlayers;
        String packLocation;

        //Checking input for number of players is a valid integer
        System.out.printf("Please enter the number of players: ");

        //While numPlayers input is not valid, keep asking for a valid input.
        while(!sc.hasNextInt()) {
            sc = null;
            sc = new Scanner(System.in);
            System.out.printf("That is not a valid number! Please enter a valid one: ");
        }
        numPlayers = sc.nextInt();

        //This block of code checks the input file is valid.
        sc = new Scanner(System.in);
        System.out.printf("Please enter the location of the pack to load: ");
        while(true) {
            try {
                packLocation = sc.nextLine();
                while (!FileHandler.checkPackFile(packLocation, numPlayers)) {
                    sc = null;
                    sc = new Scanner(System.in);
                    packLocation = sc.nextLine();
                }
                break;
            } catch(FileNotFoundException e) {
                sc = new Scanner(System.in);
                System.out.printf("That is an invalid pack location, the pack cannot be found:(\nPlease enter a valid location: ");
            } catch(InvalidPackException e) {
                sc = new Scanner(System.in);
                System.out.printf("%s\n", e.getMessage());
                System.out.printf("Please enter the location of a valid pack to load: ");
            }
        }

        //Closing the scanner to prevent leaks.
        sc.close();

        CardGame cg = new CardGame(numPlayers, packLocation);
        cg.gameSetup();
        cg.gameRun();
    }
}
