package cards;

import com.sun.jdi.ObjectReference;

import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CardGame {
    private int numPlayers;

    private ArrayList<Player> playersInGame = new ArrayList<Player>();
    private ArrayList<Deck> decksInGame = new ArrayList<Deck>();

    //potentially each card game has a list of playerids and deck ids associated with it.

    private Stack<Integer> pack;


    public CardGame(int numPlayers, String filename) throws InvalidPackException, FileNotFoundException{
        if(numPlayers <= 0) {
            throw new InvalidPackException("Number of players needs to be greater than 0");
        }
        this.numPlayers = numPlayers;

        //getting the pack
        //checking the pack
        try {
            this.pack = ReadFile.getStack(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
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

    class PlayerThread implements Runnable {
        private static volatile boolean won = false;

        public void run() {
            String threadName = Thread.currentThread().getName();
            Player player = playersInGame.get(Integer.parseInt(threadName));
            Deck pickupDeck = decksInGame.get(player.getPlayerId());

            Deck discardDeck;
            if(player.getPlayerId() + 1 == numPlayers) {
                discardDeck = decksInGame.get(0);
            } else {
                discardDeck = decksInGame.get(player.getPlayerId() + 1);
            }


            while(!checkWin(player)) {
                if(pickupDeck.getDeckCards().isEmpty()) {
                    synchronized (this) {
                        try {
                            System.out.printf("Player %d is WAITING in thread %s\n", player.getPlayerId(), threadName);
                            wait(1000);
                        } catch (InterruptedException e) {
                            /*DO nothing*/
                        }
                    }
                } else {
                    System.out.printf("Player %d is running!\n", player.getPlayerId());
                    Card pickedUp = pickUpCard(player);
                    //System.out.printf("Player %d has picked up: %d\n", player.getPlayerId(), pickedUp.getCardValue());
                    Card discarded = discardCard(player);
                    //System.out.printf("Player %d has discarded : %d\n", player.getPlayerId(), discarded.getCardValue());
                    //notify thread that is waiting, this thread will be the one that picks up from the deck just discarded to
                    Player canPlay;
                    if(player.getPlayerId() + 1 == numPlayers) {
                        canPlay = playersInGame.get(0);
                    } else {
                        canPlay = playersInGame.get(player.getPlayerId() + 1);
                    }
                }
            }
            //A player has won
            ArrayList<Integer> winningHand = new ArrayList<>();
            for(Card c : player.getPlayerHand()) {
                winningHand.add(c.getCardValue());
            }
            System.out.printf("Player %d has won!\n", player.getPlayerId());
            System.out.println("Player " + player.getPlayerId() + "'s Hand: " + winningHand);
            System.exit(100);
        }

    }

    //setup game
    public void gameSetup() {
        for(int i = 0; i < numPlayers; i++) {
            createPlayer();
            createDeck();
        }
        addToPlayers();
        addToDecks();
    }

    public void gameRun() {
        //Player threads
        for(int i = 0; i < numPlayers; i++) {

            Thread t = new Thread(new PlayerThread());
            t.setName(Integer.toString(i));
            t.start();
        }
    }

    public void addToPlayers() {
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < numPlayers; j++) {
                Player player = playersInGame.get(j);
                Card card = new Card(pack.pop());
                player.addToHand(card);
            }
        }
    }

    public void addToDecks() {
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < numPlayers; j++) {
                Deck deck = decksInGame.get(j);
                Card card = new Card(pack.pop());
                deck.addToDeckCards(card);
            }
        }
    }



    //create players
    public int createPlayer() {
        Player player = new Player();
        playersInGame.add(player);
        return player.getPlayerId();
    }

    //create decks
    public int createDeck() {
        Deck deck = new Deck();
        decksInGame.add(deck);
        return deck.getDeckId();
    }

    //GAMEPLAY methods
    public synchronized Card pickUpCard(Player player) {
        //get deck
        Deck deck = decksInGame.get(player.getPlayerId());
        //remove from deck, add to player hand
        Card card = deck.removeCard();
        player.addToHand(card);
        return card;
    }

    public synchronized Card discardCard(Player player) {
        //get deck
        Deck deck;
        if(player.getPlayerId() + 1 == numPlayers) {
            deck = decksInGame.get(0);
        }
        else {
            deck = decksInGame.get(player.getPlayerId() + 1);
        }

        //find card to discard
        Card cardToDiscard = getCardToDiscard(player);

        //remove from player hand, add to deck;
        player.removeFromHand(cardToDiscard);
        deck.addCard(cardToDiscard);

        return cardToDiscard;
    }

    /**
     * Checks a players preference, then returns the index of the card to discard based off of CA game strategy
     * we have been tasked to implement.
     * @param player
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

    //MAIN EXECUTABLE METHOD
    public static void main(String[] args) throws InvalidPackException, FileNotFoundException{
        CardGame cg = new CardGame(4, "packs/4.txt");
        cg.gameSetup();
        cg.gameRun();
    }
}
