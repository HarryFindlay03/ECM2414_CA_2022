package cards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class FileHandler {
    String filename;

    public FileHandler(String filename) {
        this.filename = filename;
    }

    /**
     * Creates a new file at the location given on the creation of a new FileHandler object.
     */
    public void createFile() {
        File f = new File(filename);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a stack that represents the inputted pack file.
     * @param filename Location of the pack file to be loaded.
     * @return A stack that represents the pack.
     * @throws FileNotFoundException
     */
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

    /**
     * This function removes the files in the deckfiles and playerfiles directories before they are then repopulated
     * with the files required for the new card game being played.
     */
    public static void clearFiles() {
        //clear playerfiles and deck files
        for(File playerFile : new File("output-files/playerfiles").listFiles()) playerFile.delete();

        for(File deckFile : new File("output-files/deckfiles").listFiles()) deckFile.delete();

    }

    /**
     * Overloaded version of clearFiles for testing purposes.
     * @param dirname directory where files live
     */
    public static void clearFiles(String dirname) {
        for(File f : new File(dirname).listFiles()) f.delete();
    }

    /**
     * Returns a boolean value depending on the validity of an input file. In the case for this
     * card game, the length of the file (number of lines) has to equal 8 * the number of players.
     * e.g. If there are 4 players, the length of the pack file should be 32.
     * @param filename Location of the pack file, either full path or path from content root.
     * @param numPlayers The number of players that are playing in the game, this is so the
     *                   correct pack length can be determined.
     * @return A boolean value depending on the validity of the pack
     * @throws FileNotFoundException
     */
    public static boolean checkPackFile(String filename, int numPlayers) throws FileNotFoundException, InvalidPackException {
        int count;
        File packFile = new File(filename);
        Scanner sc = new Scanner(packFile);

        count = 0;
        while(sc.hasNextLine()) {
            sc.nextLine();
            count++;
        }

        if(count == numPlayers * 8) return true;

        throw new InvalidPackException(String.format("Pack Length not correct! File had [%d] lines, expecting [%d] lines.", count, (numPlayers * 8)));
    }

    /**
     * Returns the number of lines in a given file
     * @param filename Location of file, either full path or path from content root
     * @return An integer value representing the number of lines in the file
     * @throws FileNotFoundException
     */
    public static int numLinesInFile(String filename) throws FileNotFoundException {
        int count;
        File f = new File(filename);
        Scanner sc = new Scanner(f);

        count = 0;
        while(sc.hasNextLine()) {
            sc.nextLine();
            count++;
        }
        return count;
    }
}