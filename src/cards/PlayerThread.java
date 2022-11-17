package cards;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class that implements multi-threading into the CardGame. There is a PlayerThread instance
 * linked to each player in the game. E.g. with a 5 player game, there will be 5 threads running.
 */
public class PlayerThread implements Runnable {
    CardGame cg; //CardGame that thread is linked to.
    int numPlayers;
    ArrayList<Player> playersInGame;
    ArrayList<Deck> decksInGame;

    private volatile boolean gameComplete = false; //used to kill the threads once all output has completed.

    public PlayerThread(CardGame cg) {
        this.cg = cg;
        this.numPlayers = cg.getNumPlayers();
        this.playersInGame = cg.getPlayersInGame();
        this.decksInGame = cg.getDecksInGame();
    }

    /**
     * Run method in the PlayerThread class
     */
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        Player player = cg.getPlayersInGame().get(Integer.parseInt(threadName));

        String playerFileName = String.format("src/cards/playerfiles/Player%d.txt", player.getPlayerId());

        Deck pickupDeck = cg.getDecksInGame().get(player.getPlayerId() - 1);
        Deck discardDeck;
        if (player.getPlayerId() + 1 > numPlayers) {
            discardDeck = decksInGame.get(0);
        } else {
            discardDeck = decksInGame.get(player.getPlayerId());
        }

        while (!cg.checkWin(player) && cg.getWinningPlayer() == null) {
            //IF the deck the current thread is trying to pick up from is empty, wait the thread.
            //ELSE play a pickup and discard operation.
            if (pickupDeck.getDeckCards().isEmpty()) {
                synchronized (player) {
                    try {
                        player.wait();
                    } catch (InterruptedException e) {/*DO nothing*/}
                }
            } else {
                try {
                    FileWriter playerFileWriter = new FileWriter(playerFileName, true);

                    Card pickedUp = cg.pickUpCard(player);
                    playerFileWriter.write(String.format("Player %d draws a %d from deck %d\n", player.getPlayerId(), pickedUp.getCardValue(), pickupDeck.getDeckId()));

                    Card discarded = cg.discardCard(player);
                    playerFileWriter.write(String.format("Player %d discards a %d to deck %d\n", player.getPlayerId(), discarded.getCardValue(), discardDeck.getDeckId()));

                    playerFileWriter.write(String.format("Player %d current hand %s\n", player.getPlayerId() ,player.getPlayerHandString()));
                    playerFileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Player canPlay; //Computing the deck that can now play.
                if (player.getPlayerId() + 1 > numPlayers) {
                    canPlay = playersInGame.get(0);
                } else {
                    canPlay = playersInGame.get(player.getPlayerId());
                }
                synchronized (canPlay) {
                    /* Notifying the thread that plays with the deck that has just been discarded to
                     * that it can now play as this deck has been populated with a card*/
                    canPlay.notify();
                }
            }
        }
        //A player has won
        gameEnd(player);
    }

    /**
     * This method is called when a thread has won a game, all other threads shall be notified.
     * Then each player file has to output which player has won, that the player has exited the game and the final hand of the player.
     * Furthermore, the contents of each deck should be outputted to their respective deck file
     */
    void gameEnd(Player player) {
        while (!gameComplete) {
            if (cg.getWinningPlayer() == null) {
                cg.setWinningPlayer(player);
            }

            //Notifying waiting threads that a thread has won the game
            for(Deck d : cg.getDecksInGame()) {
                if(d.getDeckCards().size() == 0) {
                    Player locked = cg.getPlayersInGame().get(d.getDeckId() - 1);
                    synchronized (locked) {
                        locked.notify();
                    }
                }
            }

            try {
                FileWriter writer = new FileWriter(String.format("src/cards/playerfiles/Player%d.txt", player.getPlayerId()), true);
                if (player != cg.getWinningPlayer()) {
                    writer.write(String.format("Player %d has informed Player %d that Player %d has won\n", cg.getWinningPlayer().getPlayerId(), player.getPlayerId(), cg.getWinningPlayer().getPlayerId()));
                    writer.write(String.format("Player %d exits\n", player.getPlayerId()));

                } else {
                    System.out.printf("Player %d wins 😎\n", player.getPlayerId());
                    writer.write(String.format("Player %d wins\n", player.getPlayerId()));
                    writer.write(String.format("Player %d exits\n", player.getPlayerId()));
                }
                writer.write(String.format("Player %d final hand: %s\n", player.getPlayerId(), player.getPlayerHandString()));
                writer.close();
            } catch (IOException e) {/*NOT HANDLING*/}

            gameComplete = true;
        }
    }
}
