package com.lyrasolarpokerhands;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileReader implements Closeable {

    private final BufferedReader reader;

    public FileReader(InputStream in) {
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    public Round getNextRound() throws IOException {
        String nextLine = reader.readLine();
        String[] cardStrings = nextLine.split(" ");

        List<Card> first = makeCardList(cardStrings, 0, 5);
        List<Card> second = makeCardList(cardStrings, 5, 10);

        return new Round(first, second);
    }

    public static List<Card> makeCardList(String[] cardStrings, int start, int stop) {
        List<Card> cardList = new ArrayList<>(stop - start);
        for(int i = start; i < stop; i++) {
            cardList.add(new Card(cardStrings[i]));
        }
        return cardList;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    public boolean hasNext() throws IOException {
        return reader.ready();
    }
}
