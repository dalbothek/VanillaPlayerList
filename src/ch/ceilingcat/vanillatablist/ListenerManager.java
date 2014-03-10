package ch.ceilingcat.vanillatablist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.plugin.Plugin;

public class ListenerManager {
    private final List<VanillaPlayerListListener> listeners = new ArrayList<>();

    public ListenerManager(VanillaPlayerList plugin) {
        plugin.getServer().getPluginManager().registerEvents(new PluginEventHandler(this), plugin);
    }

    public void addListener(Plugin plugin, VanillaPlayerListListener listener) {
        listener.setPlugin(plugin);
        listeners.add(listener);
    }

    public void removeListener(VanillaPlayerListListener listener) {
        listeners.remove(listener);
    }

    void removeListeners(Plugin plugin) {
        Iterator<VanillaPlayerListListener> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            VanillaPlayerListListener listener = iterator.next();
            if (listener.getPlugin() == plugin) {
                iterator.remove();
            }
        }
    }

    void updatePlayerList(List<PlayerPing> players) {
        players = Collections.unmodifiableList(players);
        for (VanillaPlayerListListener listener : listeners) {
            listener.onPlayerListChange(players);
        }
    }
}
