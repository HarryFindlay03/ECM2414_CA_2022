import cards.CardGame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class testCardGame {
    private final CardGame cardGame = new CardGame();

    @Test
    void testReturn10() {
        assertEquals(10, cardGame.return10());
    }
}
