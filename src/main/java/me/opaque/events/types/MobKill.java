package me.opaque.events.types;

import me.opaque.MoLevelz;
import me.opaque.player.LPLayerManager;
import me.opaque.player.LPlayer;
import me.opaque.utils.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Random;

public class MobKill implements Listener {

    private final LPLayerManager lpLayerManager;
    private final MoLevelz plugin;

    public MobKill(MoLevelz plugin, LPLayerManager lpLayerManager) {
        this.lpLayerManager = lpLayerManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        Entity killer = event.getEntity().getKiller();
        if (killer != null) {
            Player player = event.getEntity().getKiller();
            if (event.getEntityType() == EntityType.ZOMBIE) {
                if (Math.random() < 0.90) {
                    LPlayer lPlayer = lpLayerManager.getPlayer(player.getUniqueId());
                    int xp = getRandomInt(10);
                    lPlayer.addXp(xp);
                    lpLayerManager.levelUp(player.getUniqueId());
                    Utils.send(player, "&a&l+ " + xp);
                    Utils.send(player, "&fXP&8: &6" + lPlayer.getXp());
                } else {
                    Utils.send(player, "&c&lFAILED :(");
                }
            }
        }
    }

    public static Integer getRandomInt(Integer max) {
        Random ran = new Random();
        if (ran.nextInt(max) == 0) {
            return 1;
        }
        return ran.nextInt(max);
    }
}
