package com.lyrasolarpokerhands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValueRanking implements Comparable<ValueRanking>{
    private final Ranking rank;
    private final Value primary;
    private final Value secondary;
    private final List<Value> kicker;

    public ValueRanking(Ranking rank, Value primary, Value secondary, List<Value> kicker) {
        this.rank = rank;
        this.primary = primary == null ? Value.NULL : primary;
        this.secondary = secondary == null ? Value.NULL : secondary;
        List<Value> kickerTemp = new ArrayList<>(kicker);
        Collections.sort(kickerTemp);
        Collections.reverse(kickerTemp);
        this.kicker = Collections.unmodifiableList(kickerTemp);
    }

    @Override
    public int compareTo(ValueRanking o) {
        int compareTo = rank.compareTo(o.rank);
        if(compareTo != 0) return compareTo;

        compareTo = primary.compareTo(o.primary);
        if(compareTo != 0) return compareTo;

        compareTo = secondary.compareTo(o.secondary);
        if(compareTo != 0) return compareTo;

        for(int i = 0; i < kicker.size(); i++) {
            compareTo = kicker.get(i).compareTo(o.kicker.get(i));
            if(compareTo != 0) return compareTo;
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.format(
                "[%s,%s,%s,%s]",
                rank, primary, secondary, kicker);
    }
}
