package ch.ceilingcat.vanillatablist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

class PluginEventHandler implements Listener {
    private final ListenerManager manager;

    public PluginEventHandler(ListenerManager manager) {
        this.manager = manager;
    }

    @EventHandler()
    public void onPluginDisable(PluginDisableEvent event) {
        manager.removeListeners(event.getPlugin());
    }
}
