package ch.ceilingcat.vanillatablist;

public enum Bars {
    ZERO(0),
    FIVE(150),
    FOUR(300),
    THREE(600),
    TWO(1000),
    ONE(2000);

    private int maxPing;

    Bars(int maxPing) {
        this.maxPing = maxPing;
    }

    int getPing() {
        return maxPing - 1;
    }

    static Bars forPing(int ping) {
        for (Bars bars : values()) {
            if (ping < bars.maxPing) {
                return bars;
            }
        }
        return ONE;
    }
}
