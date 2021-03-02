package me.opaque.events;

import me.opaque.MoLevelz;
import me.opaque.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private final MoLevelz plugin;

    public PlayerJoin(MoLevelz plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Utils.debug("player joined", plugin, false);
        Player player = event.getPlayer();
//        LPLayerManager lpLayerManager = new LPLayerManager(plugin);
//                                        ^^^^^^^^^^^^^^^^^^^^^^^^^^ !??!?!?!? you should only have 1 instance of this!!
        if (!plugin.getLPlayerManager().hasLevel(player.getUniqueId())) {
            Utils.debug("player not loaded", plugin, false);
//            plugin.getLPlayerManager().addPlayer(player.getUniqueId());
//            Utils.debug("added player", plugin, false);
            plugin.getLPlayerManager().loadPlayer(player);
//            Utils.debug("loaded player", plugin, false);
        }
    }
}
