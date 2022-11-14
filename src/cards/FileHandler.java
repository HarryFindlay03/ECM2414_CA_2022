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

    public void createFile() {
        File f = new File(filename);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stack<Integer> getStack(String filename) throws FileNotFoundException {
        Random random = new Random();
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

    public static void clearFiles() {
        //clear playerfiles and deck files
        for(File playerFile : new File("src/cards/playerfiles").listFiles()) playerFile.delete();

        for(File deckFile : new File("src/cards/deckfiles").listFiles()) deckFile.delete();

    }

    public static boolean checkPackFile(String filename, int numPlayers) throws FileNotFoundException {
        int count;
        File packFile = new File(filename);
        Scanner sc = new Scanner(packFile);

        count = 0;
        while(sc.hasNextLine()) {
            sc.nextLine();
            count++;
        }

        if(count == numPlayers * 8) return true;

        return false;
    }

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