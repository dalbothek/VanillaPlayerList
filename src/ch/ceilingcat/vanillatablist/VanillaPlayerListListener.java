package ch.ceilingcat.vanillatablist;

import java.util.List;

import org.bukkit.plugin.Plugin;

public abstract class VanillaPlayerListListener {
    private Plugin plugin;

    /**
     * Invoked every 10 ticks if any player logged in, quit or anyone's ping changed.
     * 
     * The ping is only considered changed if the number of bars the client would display changes.
     * 
     * @param players
     *            A list of all online players and their ping, ordered by the time they logged on.
     */
    public abstract void onPlayerListChange(List<PlayerPing> players);

    void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    Plugin getPlugin() {
        return plugin;
    }
}
