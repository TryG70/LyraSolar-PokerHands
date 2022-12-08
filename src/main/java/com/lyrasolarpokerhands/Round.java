package com.lyrasolarpokerhands;

import java.util.List;

public class Round {
    public final Hand playerOne;
    public final Hand playerTwo;

    public Round(List<Card> firstCards, List<Card> secondCards) {
        this.playerOne = new Hand(firstCards);
        this.playerTwo = new Hand(secondCards);
    }

    public boolean playerOneWins() {
        return playerOne.compareTo(playerTwo) > 0;
    }
}
