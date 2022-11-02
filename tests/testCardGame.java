import cards.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class testCardGame {
    CardGame cardGame;

    @BeforeEach
    void setUp() throws InvalidPackException {
        cardGame = new CardGame(4, "test.txt");
        cardGame.gameSetup();
    }

    @AfterEach
    void cleanUp() {
        Player.getPlayerIds().clear();
        Deck.getDeckIds().clear();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1, 2, 4, 5, 50, -999, 999})
    void testGameSetup(int numPlayers) throws InvalidPackException {
        cleanUp();
        if(numPlayers <= 0) {
            assertThrows(InvalidPackException.class, () -> {
                CardGame invalidCardGame = new CardGame(-1, "test.txt");
            });
        } else {
            CardGame cg = new CardGame(numPlayers, "test.txt");
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

//    @Test
//    void testPackLength() {
//        int expectedPackLength = Player.getPlayerIds().size() * 8;
//        assertEquals(expectedPackLength, cardGame.getPackLength());
//    }
}
