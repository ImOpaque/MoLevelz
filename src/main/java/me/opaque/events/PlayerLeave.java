package me.opaque.events;

import me.opaque.MoLevelz;
import me.opaque.player.LPLayerManager;
import me.opaque.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    private final MoLevelz plugin;

    public PlayerLeave(MoLevelz plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Utils.debug("player left", plugin, false);
        Player player = event.getPlayer();
//        LPLayerManager lpLayerManager = new LPLayerManager(plugin);
//        use the same manager each time - don't create a new one because it wont contain the player!!!!!!
        if (plugin.getLPlayerManager().hasLevel(player.getUniqueId())) {
            Utils.debug("checked player", plugin, false);
            plugin.getLPlayerManager().removePlayer(player.getUniqueId());
            Utils.debug("removed player", plugin, false);
            plugin.getLPlayerManager().saveToDisk();
            Utils.debug("saved to disk", plugin, false);
        }
    }
}
