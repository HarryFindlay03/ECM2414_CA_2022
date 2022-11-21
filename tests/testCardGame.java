import cards.*;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class testCardGame {
    CardGame cardGame;
    int numPlayers;

    @BeforeEach
    void setUp() throws InvalidPackException, FileNotFoundException {
        //Creating file-output directories
        try {
            Files.createDirectory(Paths.get("output-files"));
            Files.createDirectory(Paths.get("output-files/playerfiles"));
            Files.createDirectory(Paths.get("output-files/deckfiles"));
        } catch (IOException e) {
            //do nothing
        }
        numPlayers = 4;
        cardGame = new CardGame(numPlayers, "packs/" + numPlayers + ".txt");
        cardGame.gameSetup();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1, 2, 4, 5, 49, -999})
    void testGameSetup(int numPlayers) throws InvalidPackException, FileNotFoundException {
        String filename = "packs/" + numPlayers + ".txt";
        if (numPlayers < 2) {
            assertThrows(InvalidPackException.class, () -> {
                CardGame cg = new CardGame(numPlayers, filename);
            });
        } else {
            CardGame cg = new CardGame(numPlayers, filename);
            cg.gameSetup();
            assertEquals(numPlayers, cg.getPlayersInGame().size());
            assertEquals(numPlayers, cg.getDecksInGame().size());
        }
    }

    @Test
    void testPlayers() {
        assertEquals(numPlayers, cardGame.getPlayersInGame().size());
    }

    @Test
    void testDecks() {
        assertEquals(numPlayers, cardGame.getDecksInGame().size());
    }

    @Test
    void testInvalidPackException() {
        assertThrows(InvalidPackException.class, () -> {
            new CardGame(-1, "1.txt");
        });
    }

    @Test
    void testPackLength() {
        int expectedPackLength = cardGame.getPlayersInGame().size() * 8 - (4 * cardGame.getPlayersInGame().size()) - (4 * cardGame.getDecksInGame().size());
        assertEquals(expectedPackLength, cardGame.getPack().size());
    }

    @ParameterizedTest
    @ValueSource(ints = {-99, 10, 5, 4, 6, 7})
    void testPack(int numPlayers) throws InvalidPackException, FileNotFoundException {
        if (numPlayers <= 0) {
            assertThrows(InvalidPackException.class, () -> {
                CardGame invalidCardGame = new CardGame(numPlayers, "test.txt");
            });
        } else {
            String filename = "packs/" + String.valueOf(numPlayers) + ".txt";
            CardGame cg = new CardGame(numPlayers, filename);
            cg.gameSetup();
            int expectedPackLength = cardGame.getPlayersInGame().size() * 8 - (4 * cardGame.getPlayersInGame().size()) - (4 * cardGame.getDecksInGame().size());
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
        for (int i = 0; i < cardGame.getPlayersInGame().size(); i++) {
            assertEquals(4, cardGame.getPlayersInGame().get(i).getPlayerHand().size());
        }
    }

    @Test
    void testDeckCards() {
        for (int i = 0; i < cardGame.getPlayersInGame().size(); i++) {
            assertEquals(4, cardGame.getDecksInGame().get(i).getDeckCards().size());
        }
    }

    @Test
    void testInitialHand() throws Exception {
        CardGame cg = new CardGame(4, "tests/res/4_testInitHand.txt");
        cg.gameSetup();
        assertEquals(" 4 4 4 4", cg.getPlayersInGame().get(0).getPlayerHandString());
    }

    @Test
    void testHands() {
        cardGame.gameRun();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}

        for(Player p : cardGame.getPlayersInGame()) {
            assertEquals(4, p.getPlayerHand().size());
        }
    }

    @Nested
    class gameplayTests {
        ArrayList<Player> playersInGame;
        ArrayList<Deck> decksInGame;

        @BeforeEach
        void setUp() {
            playersInGame = cardGame.getPlayersInGame();
            decksInGame = cardGame.getDecksInGame();
        }

        @AfterEach
        void tearDown() {
            playersInGame.clear();
            decksInGame.clear();
        }

        @Test
        void testPickUpCard() {
            for (int i = 0; i < playersInGame.size(); i++) {
                cardGame.pickUpCard(playersInGame.get(i));
                assertEquals(5, playersInGame.get(i).getPlayerHand().size());
                assertEquals(3, decksInGame.get(i).getDeckCards().size());
            }
        }

        @Test
        void testDiscardCard() {
            for (int i = 0; i < playersInGame.size(); i++) {
                cardGame.pickUpCard(playersInGame.get(i));
                cardGame.discardCard(playersInGame.get(i));
                assertEquals(4, playersInGame.get(i).getPlayerHand().size());
            }
        }

        @Test
        void testGetCardToDiscard() {
            for (int i = 0; i < playersInGame.size(); i++) {
                Player player = playersInGame.get(i);
                cardGame.pickUpCard(playersInGame.get(i));
                Card card = cardGame.getCardToDiscard(player);
                assertNotEquals(player.getPreference(), card.getCardValue());
            }
        }

        @Test
        void testCheckWin() {
            playersInGame.get(0).setWinningPlayerHand();
            assertEquals(true, cardGame.checkWin(playersInGame.get(0)));
        }


        @Test
        void testGameAlot() throws InvalidPackException, FileNotFoundException {
            CardGame cg;
            for(int i = 2; i < 101; i++) {
                System.out.printf("Game run: %d\tTesting with [%d] players.\n", i, i);
                cg = new CardGame(i, String.format("packs/%d.txt", i));
                cg.gameSetup();
                cg.gameRun();
            }
            try {
                Thread.sleep(1000);
            }catch(InterruptedException e) {}
        }

        @Nested
        class FileHandlerTests {
            FileHandler fh;

            @AfterEach
            void tearDown() throws IOException {
                FileHandler.clearFiles("tests/res/FileHandlerTestFiles");
            }

            @Test
            void testFileCreation() throws Exception {
                for(int i = 0; i < 5; i++) {
                    FileHandler f = new FileHandler(String.format("tests/res/FileHandlerTestFiles/%d.txt", i));
                    f.createFile();
                }

                Set<String> fileSet = new HashSet<>();

                try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("tests/res/FileHandlerTestFiles"))) {
                    for (Path path : stream) {
                        fileSet.add(path.getFileName().toString());
                    }
                } catch (IOException e) {}

                for(int j = 0; j < 5; j++) {
                    assertEquals(String.valueOf(j), String.valueOf(j));
                }
            }

            @Test
            void testRemoveFiles() throws Exception {
                for(int i = 0; i < 5; i++) {
                    FileHandler f = new FileHandler(String.format("tests/res/FileHandlerTestFiles/%d.txt", i));
                    f.createFile();
                }

                FileHandler.clearFiles("tests/res/FileHandlerTestFiles");

                Set<String> fileSet = new HashSet<>();

                try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("tests/res/FileHandlerTestFiles"))) {
                    for (Path path : stream) {
                        fileSet.add(path.getFileName().toString());
                    }
                } catch (IOException e) {}

                assertEquals(0, fileSet.size());
            }

            @Test
            void testNumLines() throws Exception {
                assertEquals(16, FileHandler.numLinesInFile("tests/res/2_pl1wins.txt"));
            }
        }

    }
}