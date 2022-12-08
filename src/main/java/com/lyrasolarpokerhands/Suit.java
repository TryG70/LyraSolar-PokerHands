package com.lyrasolarpokerhands;

import java.util.HashMap;
import java.util.Map;

public enum Suit {
    HEART ('H'),
    DIAMOND ('D'),
    SPADE ('S'),
    CLUB ('C'),
    NULL ('x');

    private final char suitChar;

    private static final Map<Character, Suit> valueMap = new HashMap<>();

    static {
        for(Suit c : Suit.values()) {
            valueMap.put(c.getSuitChar(), c);
        }
    }

    Suit(char suitChar) {
        this.suitChar = suitChar;
    }

    public char getSuitChar() {
        return suitChar;
    }

    public static Suit of(char value) {
        Suit s = valueMap.get(value);
        return s == null ? NULL: s;
    }
}


