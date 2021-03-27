package me.opaque.player;

import me.opaque.MoLevelz;
import me.opaque.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LPLayerManager {

    private final MoLevelz plugin;

    public HashMap<UUID, LPlayer> players = new HashMap<>();

    public LPLayerManager(MoLevelz plugin) {
        this.plugin = plugin;
    }

    public LPlayer getPlayer(UUID uuid) {
        LPlayer lPlayer = players.get(uuid);
        if (lPlayer == null) {
            Utils.debug("LPlayer of " + uuid + " is null, but was requested:", plugin, false);
            Thread.dumpStack();
        }
        return lPlayer;
    }

    public void removePlayer(UUID uuid) {
        saveToDisk();
        players.remove(uuid);
    }

    public boolean hasLevel(UUID player) {
        return players.containsKey(player);
    }

    public void saveToDisk() {
        Utils.debug("&4&l[&cMoLevelz&4&l] &f&oSaving data...", plugin, false);
        File file = new File(plugin.getDataFolder() + File.separator + "players.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
        if (!(Bukkit.getServer().getOnlinePlayers().isEmpty())) {
            for (Map.Entry<UUID, LPlayer> playerLevels : players.entrySet()) {
                data.set("players." + playerLevels.getKey() + ".name", Bukkit.getPlayer(playerLevels.getKey()).getName());
                data.set("players." + playerLevels.getKey() + ".level", playerLevels.getValue().getLevel());
                data.set("players." + playerLevels.getKey() + ".xp", playerLevels.getValue().getXp());
            }
            try {
                data.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadPlayer(Player player) {
        Utils.debug("loading player " + player.getName() + " <" + player.getUniqueId().toString() + ">", plugin, false);
        File file = new File(plugin.getDataFolder() + File.separator + "players.yml");
        YamlConfiguration data;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create players.yml");
                e.printStackTrace();
                return;
            }
        }
        data = YamlConfiguration.loadConfiguration(file);
        String uuid = player.getUniqueId().toString();
        if (data.contains("players." + uuid)) {
            Utils.debug("player " + player.getName() + " exists in players.yml, using that data", plugin, false);
            // only attempt to load player if they exist in players.yml
            int level = data.getInt("players." + uuid + ".level");
            int xp = data.getInt("players." + uuid + ".xp");

            LPlayer lPlayer = new LPlayer(level, xp);
            this.players.put(player.getUniqueId(), lPlayer);
        } else {
            Utils.debug("player " + player.getName() + " does NOT exist in players.yml, using default values", plugin, false);
            // player does not exist in players.yml, so create new one instead
            this.players.put(player.getUniqueId(), new LPlayer(1, 0));
        }
    }

    public void levelUp(UUID player) {
        LPlayer lPlayer = getPlayer(player);
        int pLevel = lPlayer.getLevel();
        int pXp = lPlayer.getXp();
        //for (String levels : plugin.getConfig().getConfigurationSection("levels.").getKeys(false)) {
        int size = plugin.getConfig().getConfigurationSection("levels.").getKeys(false).size() + 1;
        int cost = getCost(player);
        if (pXp >= cost) {
            Utils.debug("cost is greater", plugin, true);
            if (size <= pLevel) {
                Utils.debug("size is less than player level   " + size + "   " + pLevel, plugin, true);
                return;
            }
            lPlayer.setLevel(pLevel + 1);
            Utils.debug("set level", plugin, true);
            lPlayer.setXp(0);
            Utils.debug("set xp to 0", plugin, true);
            Utils.debug("s: " + size + " l: " + pLevel, plugin, true);
            if (Bukkit.getPlayer(player) != null) {
                Player pl = Bukkit.getPlayer(player);
                Utils.send(pl, "&4&l<!> &f LEVELED UP TO &6" + lPlayer.getLevel());
            }
        } else {
            Utils.debug("cost is less", plugin, true);
            return;
        }
    }

    public Integer getCost(UUID player) {
        LPlayer lPlayer = getPlayer(player);
        int pLevel = lPlayer.getLevel();
        int cost = 500;
        for (String levels : plugin.getConfig().getConfigurationSection("levels.").getKeys(false)) {
            if (Integer.parseInt(levels) == pLevel) {
                return plugin.getConfig().getInt("levels." + pLevel + ".cost");
            }
        }
        return cost;
    }
}
