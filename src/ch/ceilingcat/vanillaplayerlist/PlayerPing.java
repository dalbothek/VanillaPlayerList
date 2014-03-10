package ch.ceilingcat.vanillaplayerlist;

import org.bukkit.entity.Player;

public class PlayerPing {
    private Bars bars;
    private final Player player;
    private int ping;

    PlayerPing(Player player) {
        this(player, Bars.ZERO);
    }

    PlayerPing(Player player, Bars bars) {
        this.player = player;
        this.bars = bars;
    }

    /**
     * Gets the number of bars a client would display for this player's ping.
     * 
     * @return The number of bars
     */
    public Bars getBars() {
        return bars;
    }

    /**
     * Gets the ping of this player.
     * 
     * @return The ping in milliseconds
     */
    public int getPing() {
        return ping;
    }

    /**
     * Gets the player associated with this ping.
     * 
     * @return The player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the name of the player associated with this ping.
     * 
     * @return The player's name
     */
    public String getName() {
        return player.getName();
    }

    boolean setPing(int ping) {
        this.ping = ping;
        Bars previousBars = bars;
        bars = Bars.forPing(ping);
        return previousBars != bars;
    }
}
