package com.lyrasolarpokerhands;

import java.util.*;

public class Hand implements Comparable<Hand> {

    private final List<Card> cards;
    private final List<Card> cardsSentinel;
    private final ValueRanking ranking;

    public Hand(List<Card> cards) {
        this.cards = Collections.unmodifiableList(new ArrayList<>(cards));

        List<Card> temp = new ArrayList<>(cards);
        temp.add(new Card(Value.NULL, Suit.NULL));
        Collections.sort(temp);
        this.cardsSentinel = Collections.unmodifiableList(temp);

        ValueRanking straightRanking = straightRanking();

        ranking = Objects.requireNonNullElseGet(straightRanking, this::cardMatchRanking);
    }

    @Override
    public int compareTo(Hand o) {
        return ranking.compareTo(o.ranking);
    }

    private ValueRanking straightRanking() {
        boolean straight = isStraight();
        boolean flush = isFlush();
        if(straight && flush) {
            return makeRanking(Ranking.STRAIGHT_FLUSH);
        } else if(flush) {
            return makeRanking(Ranking.FLUSH);
        } else if(straight) {
            return makeRanking(Ranking.STRAIGHT);
        } else {
            return null;
        }
    }

    private ValueRanking cardMatchRanking() {
        CardMatchRankingBuilder builder = new CardMatchRankingBuilder();

        for(Card c : cardsSentinel) {
            builder.addCard(c.getValue());
        }

        return builder.build();
    }

    private boolean isFlush() {
        for(int i = 0; i < 4; i++) {
            if(cardSuit(i) != cardSuit(i+1)) {
                return false;
            }
        }
        return true;
    }

    private boolean isStraight() {
        for(int i = 0; i < 4; i++) {
            if(cardRank(i) + 1 != cardRank(i+1)) {
                return isWheel(i);
            }
        }
        return true;
    }

    private boolean isWheel(int cardIndex) {
        return cardIndex == 3
                && cardValue(cardIndex) == Value.FIVE
                && cardValue(cardIndex + 1) == Value.ACE;
    }

    private Value cardValue(int cardIndex) {
        return cardsSentinel.get(cardIndex).getValue();
    }

    private Suit cardSuit(int cardIndex) {
        return cardsSentinel.get(cardIndex).getSuit();
    }

    private int cardRank(int cardIndex) {
        return cardValue(cardIndex).ordinal();
    }

    private ValueRanking makeRanking(Ranking type) {
        return new ValueRanking(type, cardValue(4), null, getKickers());
    }

    private List<Value> getKickers() {
        List<Value> kickers = new LinkedList<>();
        for(int i = 0; i < 4; i++) {
            kickers.add(cardValue(i));
        }
        return kickers;
    }

    private static class CardMatchRankingBuilder {
        private Value previous = null;
        private int counter = 0;
        private int pair = 0;
        private boolean trips, quads;

        private Value primary = null;
        private Value secondary = null;
        private List<Value> kicker = new ArrayList<>(5);

        public void addCard(Value newCard) {
            if(pairComplete(newCard)) {
                handleCardGroup(newCard);
                resetCounter(newCard);
            } else {
                counter++;
            }
        }

        private void handleCardGroup(Value newCard) {
            switch (counter) {
                case 2 -> handlePair(newCard);
                case 3 -> handleTrips(newCard);
                case 4 -> handleQuads(newCard);
                default -> addKicker();
            }
        }

        private boolean pairComplete(Value newCard) {
            return previous != newCard;
        }

        private void resetCounter(Value newCard) {
            previous = newCard;
            counter = 1;
        }

        private void handlePair(Value current) {
            pair++;
            if(primary == null) {
                primary = previous;
            } else if(pairHasLowerRanking(current)) {
                secondary = current;
            } else {
                demotePrimary();
            }
        }

        private boolean pairHasLowerRanking(Value current) {
            return trips || primary.compareTo(current) > 0;
        }

        private void demotePrimary() {
            secondary = primary;
            primary = previous;
        }

        private void handleTrips(Value current) {
            trips = true;
            demotePrimary();
        }

        private void handleQuads(Value current) {
            quads = true;
            primary = previous;
        }

        private void addKicker() {
            if(previous != null) kicker.add(previous);
        }

        public ValueRanking build() {
            if(quads) {
                return makeRanking(Ranking.QUADS);
            } else if(trips) {
                if(pair > 0) {
                    return makeRanking(Ranking.FULL_HOUSE);
                } else {
                    return makeRanking(Ranking.TRIPS);
                }
            } else if(pair == 2) {
                return makeRanking(Ranking.TWO_PAIR);
            } else if(pair == 1) {
                return makeRanking(Ranking.PAIR);
            } else {
                return makeRanking(Ranking.HIGH_CARD);
            }
        }

        private ValueRanking makeRanking(Ranking type) {
            return new ValueRanking(type, primary, secondary, kicker);
        }
    }
}
