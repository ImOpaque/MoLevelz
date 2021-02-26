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
        return getPlayer(uuid, false);
    }

    public LPlayer getPlayer(UUID uuid, boolean loadIfNull) {
        LPlayer lPlayer = players.get(uuid);
        if (lPlayer == null) {
            Utils.debug("LPlayer of " + uuid + " is null, but was requested:", plugin, false);
            Thread.dumpStack();
        }
        return lPlayer;
    }

    public void addPlayer(UUID player) {
        players.put(player, new LPlayer(1, 0));
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
        File file = new File(plugin.getDataFolder() + File.separator + "players" + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
        //data.set("quest-progress", null);
        if (!(Bukkit.getServer().getOnlinePlayers().size() < 1)) {
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

    public void loadConfig() {
        File file = new File(plugin.getDataFolder() + File.separator + "players" + ".yml");
        YamlConfiguration data = null;
        if (file.exists()) {
            data = YamlConfiguration.loadConfiguration(file);
        }
        players.clear();
        //assert data != null;
        for (String s : data.getConfigurationSection("players").getKeys(false)) {

            int level = data.getInt("players." + s + ".level");
            int xp = data.getInt("players." + s + ".xp");

            LPlayer lPlayer = new LPlayer(level, xp);


            this.players.put(UUID.fromString(s), lPlayer);

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
        int pXp = lPlayer.getXp();
        int cost = 500;
        for (String levels : plugin.getConfig().getConfigurationSection("levels.").getKeys(false)) {
            int size = plugin.getConfig().getConfigurationSection("levels.").getKeys(false).size() + 1;
            if (Integer.parseInt(levels) == pLevel) {
                return cost = plugin.getConfig().getInt("levels." + pLevel + ".cost");
            }
        }
        return cost;
    }
}