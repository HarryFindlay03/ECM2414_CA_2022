import cards.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class testCardGame {
    ImplementCardGameInterface cgInterface = new ImplementCardGameInterface(4, "test.txt");

    @Test
    void testCreatePlayer() {
        assertEquals(1, cgInterface.createPlayer());
    }
    @Test
    void testCreatePack() throws InvalidPackException{
        //for pack size 4
        assertEquals(32, cgInterface.createPack());
    }

}
