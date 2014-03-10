package ch.ceilingcat.vanillaplayerlist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

class PlayerEventHandler implements Listener {
    private final VanillaPlayerList plugin;

    public PlayerEventHandler(VanillaPlayerList plugin) {
        this.plugin = plugin;
    }

    @EventHandler()
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.removePlayer(event.getPlayer());
    }

    @EventHandler()
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.addPlayer(event.getPlayer());
    }
}
