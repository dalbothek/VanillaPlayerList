package ch.ceilingcat.vanillatablist;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

class PacketHandler extends PacketAdapter {
    private final VanillaPlayerList plugin;

    private final Map<Player, PingRequest> requests = new HashMap<>();
    private final Map<Player, Integer> pings = new HashMap<>();

    public PacketHandler(VanillaPlayerList plugin) {
        super(plugin, ListenerPriority.MONITOR, PacketType.Play.Server.KEEP_ALIVE, PacketType.Play.Client.KEEP_ALIVE);
        this.plugin = plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        requests.put(event.getPlayer(), new PingRequest(event.getPacket().getIntegers().read(0)));
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        PingRequest request = requests.get(player);
        if (request == null || request.id != event.getPacket().getIntegers().read(0)) {
            return;
        }
        int ping = (int) (System.nanoTime() / 1000000 - request.time);
        if (pings.containsKey(player)) {
            ping = ping + 3 * pings.get(player) / 4;
        }
        pings.put(player, ping);
        plugin.updatePing(player, ping);
    }

    public void removePlayer(Player player) {
        requests.remove(player);
        pings.remove(player);
    }

    private static class PingRequest {
        public final int id;
        public final long time;

        public PingRequest(int id) {
            this.id = id;
            time = System.nanoTime() / 1000000;
        }
    }
}
