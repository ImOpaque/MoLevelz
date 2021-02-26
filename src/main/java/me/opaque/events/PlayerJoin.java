package me.opaque.events;

import me.opaque.MoLevelz;
import me.opaque.player.LPLayerManager;
import me.opaque.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private MoLevelz plugin;

    public PlayerJoin(MoLevelz plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Utils.debug("player joined", plugin, false);
        Player player = event.getPlayer();
        LPLayerManager lpLayerManager = new LPLayerManager(plugin);
        if (!lpLayerManager.hasLevel(player.getUniqueId())) {
            Utils.debug("checked player", plugin, false);
            lpLayerManager.addPlayer(player.getUniqueId());
            Utils.debug("added player", plugin, false);
            lpLayerManager.loadConfig();
            Utils.debug("loaded config", plugin, false);
        }
    }
}
