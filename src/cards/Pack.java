package cards;

import java.util.ArrayList;
import java.util.Stack;

public class Pack {
    private int packId;
    private Stack<Integer> pack;
    private int numPlayers;
    private int packLength;

    public Pack(int numPlayer, String filename) throws InvalidPackException {
        this.packId = 1;
        this.numPlayers = numPlayer;
        this.packLength = 8 * numPlayer;

        //readPackFile()
        pack = readPackFile(numPlayers, filename);
    }

    private Stack<Integer> readPackFile(int numPlayer, String filename) throws InvalidPackException {
        if(packLength <= 0) {
            throw new InvalidPackException("Pack Length needs to be greater than 0");
        }

        return new Stack<Integer>();
    }

    public Stack<Integer> getPack() {
        return pack;
    }

    public int getPackLength() {
        return packLength;
    }
}
