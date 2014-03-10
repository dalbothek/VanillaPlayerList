package ch.ceilingcat.vanillatablist;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.mcsg.double0negative.tabapi.TabAPI;

import com.comphenix.protocol.ProtocolLibrary;

public class VanillaPlayerList extends JavaPlugin {
    private final List<PlayerPing> pingList = new LinkedList<>();
    private final Map<Player, PlayerPing> pingMap = new HashMap<>();
    private boolean dirty = false;

    private UpdateTabList updateTask;
    private PacketHandler packetHandler;
    private ListenerManager listenerManager;

    private static VanillaPlayerList instance = null;

    @Override
    public void onEnable() {
        listenerManager = new ListenerManager(this);
        packetHandler = new PacketHandler(this);

        getServer().getPluginManager().registerEvents(new PlayerEventHandler(this), this);
        ProtocolLibrary.getProtocolManager().addPacketListener(packetHandler);

        for (World world : getServer().getWorlds()) {
            for (Player player : world.getPlayers()) {
                addPlayer(player);
            }
        }

        updateTask = new UpdateTabList();
        updateTask.runTaskTimer(this, 5, 10);

        instance = this;
    }

    @Override
    public void onDisable() {
        updateTask.cancel();
        instance = null;
    }

    /**
     * Adds a listener for player list updates.
     * 
     * The listener is automatically removed once the plugin which registered it is disabled.
     * 
     * @param plugin
     *            The plugin registering the listener
     * @param listener
     *            The listener to register
     */
    public static void addListener(Plugin plugin, VanillaPlayerListListener listener) {
        instance.listenerManager.addListener(plugin, listener);
    }

    /**
     * Removes a registered listener.
     * 
     * If the listener was not registered this method has no effect.
     * 
     * @param listener
     *            The listener to unregister
     */
    public static void removeListener(VanillaPlayerListListener listener) {
        instance.listenerManager.removeListener(listener);
    }

    /**
     * Gets a list of all currently online players and their ping.
     * 
     * The list is ordered by the time the players logged on to the server.
     * 
     * @return The list of online players and their ping
     */
    public static List<PlayerPing> getPlayersPings() {
        return Collections.unmodifiableList(instance.pingList);
    }

    void addPlayer(Player player) {
        PlayerPing ping = new PlayerPing(player);
        pingList.add(ping);
        pingMap.put(player, ping);

        TabAPI.setPriority(this, player, -1);
        dirty = true;
    }

    void removePlayer(Player player) {
        pingList.remove(pingMap.remove(player));
        packetHandler.removePlayer(player);

        dirty = true;
    }

    void updatePing(Player player, int ping) {
        PlayerPing playerPing = pingMap.get(player);
        if (playerPing == null) {
            return;
        }
        if (playerPing.setPing(ping)) {
            dirty = true;
        }
    }

    private class UpdateTabList extends BukkitRunnable {
        @Override
        public void run() {
            if (!dirty) {
                return;
            }

            for (Player player : pingMap.keySet()) {
                if (!player.isOnline()) {
                    removePlayer(player);
                    continue;
                }
                int pos = 0;
                for (PlayerPing ping : pingList) {
                    TabAPI.setTabString(VanillaPlayerList.this, player, pos / 3, pos % 3, ping.getName(), ping.getPing());
                    if (++pos >= 20 * 3) {
                        break;
                    }
                }
                while (pos % 3 != 0) {
                    TabAPI.setTabString(VanillaPlayerList.this, player, pos / 3, pos % 3, TabAPI.nextNull(), Bars.ONE.getPing());
                    pos++;
                }
                TabAPI.updatePlayer(player);
            }

            listenerManager.updatePlayerList(pingList);

            dirty = false;
        }
    }
}
