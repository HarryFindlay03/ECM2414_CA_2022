package cards;

import java.io.File;
import java.io.FileNotFoundException;
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
}

class ReadFile {
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
}