import cards.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileNotFoundException;

public class testCardGame {
    CardGame cardGame;

    @BeforeEach
    void setUp() throws InvalidPackException, FileNotFoundException {
        int numPlayers = 4;
        cardGame = new CardGame(numPlayers, "packs/" + numPlayers + ".txt");
        cardGame.gameSetup();
    }

    @AfterEach
    void cleanUp() {
        Player.getPlayerIds().clear();
        Deck.getDeckIds().clear();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1, 2, 4, 5, 49, -999})
    void testGameSetup(int numPlayers) throws InvalidPackException, FileNotFoundException {
        cleanUp();
        String filename = "packs/" + String.valueOf(numPlayers) + ".txt";
        if(numPlayers <= 0) {
            assertThrows(InvalidPackException.class, () -> {
                CardGame cg = new CardGame(numPlayers, filename);
            });
        } else {
            CardGame cg = new CardGame(numPlayers, filename);
            cg.gameSetup();
            assertEquals(numPlayers, Player.getPlayerIds().size());
            assertEquals(numPlayers, Deck.getDeckIds().size());
        }
    }

    @Test
    void testPlayers() {
        assertEquals(4, Player.getPlayerIds().size());
    }

    @Test
    void testDecks() {
        assertEquals(4, Deck.getDeckIds().size());
    }

    @Test
    void testInvalidInput() {
        assertThrows(InvalidPackException.class, () -> {
           CardGame invalidCardGame = new CardGame(-1, "test.txt");
        });
    }

    @Test
    void testPackLength() {
        int expectedPackLength = Player.getPlayerIds().size() * 8 - (4 * Player.getPlayerIds().size()) - (4 * Deck.getDeckIds().size());
        assertEquals(expectedPackLength, cardGame.getPack().size());
    }

    @ParameterizedTest
    @ValueSource(ints = {-99, 10, 5, 4, 6, 7})
    void testPack(int numPlayers) throws InvalidPackException, FileNotFoundException {
        if(numPlayers <= 0) {
            assertThrows(InvalidPackException.class, () -> {
                CardGame invalidCardGame = new CardGame(numPlayers, "test.txt");
            });
        } else {
            String filename = "packs/" + String.valueOf(numPlayers) + ".txt";
            CardGame cg = new CardGame(numPlayers, filename);
            cg.gameSetup();
            int expectedPackLength = Player.getPlayerIds().size() * 8 - (4 * Player.getPlayerIds().size()) - (4 * Deck.getDeckIds().size());
            assertEquals(expectedPackLength, cg.getPack().size());
        }
    }

    @Test
    void testFileNotFound() {
        assertThrows(FileNotFoundException.class, () -> {
            CardGame cg = new CardGame(4, "fff.txt");
        });
    }

    @Test
    void testPlayerHand() {
        for(int i = 0; i < cardGame.getPlayersInGame().size(); i++) {
            assertEquals(4, cardGame.getPlayersInGame().get(i).getPlayerHand().size());
        }
    }

    @Test
    void testDeckCards() {
        for(int i = 0; i < cardGame.getPlayersInGame().size(); i++) {
            assertEquals(4, cardGame.getDecksInGame().get(i).getDeckCards().size());
        }
    }
}
