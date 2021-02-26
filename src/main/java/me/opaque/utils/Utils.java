package me.opaque.utils;

import me.opaque.MoLevelz;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {

    public static String color(String msg) {
        return msg == null ? null : ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void debug(String message, MoLevelz plugin, boolean local) {
        if (plugin.getConfig().getBoolean("debug")) {
            if (local) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().equalsIgnoreCase("ImOpaque")) {
                        send(p, message);
                    }
                }
            } else {
                Bukkit.getLogger().info(color(message));
            }
        }
    }

    public static void send(Player player, String msg) {
        player.sendMessage(color(msg));
    }
}
