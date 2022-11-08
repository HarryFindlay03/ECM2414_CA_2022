package cards;

import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;

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
        public void run() {
            //checkwin
            Player player = playersInGame.get(Integer.parseInt(Thread.currentThread().getName()));
            String threadName = Thread.currentThread().getName();

            while (!checkWin(player)) {
                //file output win
                //pickup hand
                Card card = pickUpCard(player);
                System.out.printf("Player %d has picked up card with value %d on Thread %s\n",player.getPlayerId() ,card.getCardValue(), threadName);
                Card discardedCard = discardCard(player);
                System.out.printf("Player %d has discarded card with value %d on Thread %s\n",player.getPlayerId() ,discardedCard.getCardValue(), threadName);
                System.out.println("Player " + player.getPlayerId() + "Hand: " + player.getPlayerHand());
                //discard

                //file outputs check CA spec
            }
            // When player has won, we need to stop the other threads, this is by using a flag.
            System.out.printf("Player %d has won!\n", player.getPlayerId());
            notifyAll();
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
     * Checks a players preference, then returns a card to discard based off of CA game strategy
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
        int pref = player.getPreference();

        //look through hand, get indexes of cards that do not match preference.
        ArrayList<Integer> notPref = new ArrayList<Integer>();

        for(int i = 0; i < playerHand.size(); i++) {
            if(playerHand.get(i).getCardValue() != pref) {
                notPref.add(i);
            }
        }

        //generate random number
        int randomIndex = random.nextInt(0, notPref.size());

        Card cardToDiscard = playerHand.get(notPref.get(randomIndex));

        return cardToDiscard;
    }

    public Boolean checkWin(Player player) {
        ArrayList<Card> playerHand = player.getPlayerHand();

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
