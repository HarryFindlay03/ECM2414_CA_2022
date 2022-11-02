import cards.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;

public class testCardGame {
    CardGame cardGame = new CardGame(4, "test.txt");

    public testCardGame() throws InvalidPackException {
    }

    @BeforeEach
    void setUp() {
        cardGame.gameSetup();
    }

    @AfterEach
    void cleanUp() {
        Player.getPlayerIds().clear();
        Deck.getDeckIds().clear();
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
//        assertEquals(expectedPackLength, );
//    }
}
