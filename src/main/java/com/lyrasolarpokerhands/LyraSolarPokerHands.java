package com.lyrasolarpokerhands;



import java.io.IOException;
import java.io.InputStream;


public class LyraSolarPokerHands {
    public static void main(String[] args) throws IOException {


        InputStream stream = loadDataFileStream();
        FileReader reader = new FileReader(stream);

        int counter = countPlayerOneWinners(reader);

        System.out.println(counter);

        reader.close();
    }

    private static int countPlayerOneWinners(FileReader p) throws IOException {
        int counter = 0;
        while(p.hasNext()) {
            Round r = p.getNextRound();
            if(r.playerOneWins()) counter++;
        }
        return counter;
    }

    private static InputStream loadDataFileStream() {
        return LyraSolarPokerHands.class.getResourceAsStream("/p054_poker.txt");
    }
}
