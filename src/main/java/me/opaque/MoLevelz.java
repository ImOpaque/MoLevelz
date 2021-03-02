package me.opaque;

import me.opaque.commands.LevelCommand;
import me.opaque.events.PlayerJoin;
import me.opaque.events.PlayerLeave;
import me.opaque.events.types.MobKill;
import me.opaque.player.LPLayerManager;
import me.opaque.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;

public final class MoLevelz extends JavaPlugin {

    private LPLayerManager lPlayerManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        lPlayerManager = new LPLayerManager(this);
        Utils.debug("&4&l<!> &cMoLevelz &4&l<!> &a&oEnabling...", this, false);
        registerEvents();
        registerCommands();
        generateConfig();
        generatePlayersConfig();
        for (Player player : Bukkit.getOnlinePlayers()) {
            lPlayerManager.loadPlayer(player);
        }
        new BukkitRunnable() {
            public void run() {
                lPlayerManager.saveToDisk();
            }

        }.runTaskTimer(this, 1000, 1000);
    }

    @Override
    public void onDisable() {
       lPlayerManager.saveToDisk();
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerLeave(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(this), this);
        Bukkit.getPluginManager().registerEvents(new MobKill(this, lPlayerManager), this);
    }

    public void registerCommands() {
        this.getCommand("level").setExecutor(new LevelCommand(lPlayerManager, this));
    }

    public LPLayerManager getLPlayerManager() {
        return lPlayerManager;
    }

    public void generateConfig() {
        File directory = new File(String.valueOf(this.getDataFolder()));
        if (!directory.exists() && !directory.isDirectory()) {
            directory.mkdir();
        }

        File config = new File(this.getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            try {
                config.createNewFile();
                try (InputStream in = MoLevelz.class.getClassLoader().getResourceAsStream("config.yml")) {
                    OutputStream out = new FileOutputStream(config);
                    byte[] buffer = new byte[1024];
                    int length = in.read(buffer);
                    while (length != -1) {
                        out.write(buffer, 0, length);
                        length = in.read(buffer);
                    }
                } catch (IOException e) {
                    super.getLogger().severe("Failed to create config.");
                    e.printStackTrace();
                    super.getLogger().severe("...please delete the Drugs directory and try RESTARTING (not reloading).");
                }
            } catch (IOException e) {
                super.getLogger().severe("Failed to create config.");
                e.printStackTrace();
                super.getLogger().severe(Utils.color("&c...please delete the MoLevelz directory and try RESTARTING (not reloading)."));
            }
        }
    }

    public void generatePlayersConfig() {
        File directory = new File(String.valueOf(this.getDataFolder()));
        if (!directory.exists() && !directory.isDirectory()) {
            directory.mkdir();
        }

        File config = new File(this.getDataFolder() + File.separator + "players.yml");
        if (!config.exists()) {
            try {
                config.createNewFile();
                try (InputStream in = MoLevelz.class.getClassLoader().getResourceAsStream("players.yml")) {
                    OutputStream out = new FileOutputStream(config);
                    byte[] buffer = new byte[1024];
                    int length = in.read(buffer);
                    while (length != -1) {
                        out.write(buffer, 0, length);
                        length = in.read(buffer);
                    }
                } catch (IOException e) {
                    super.getLogger().severe("Failed to create players file.");
                    e.printStackTrace();
                    super.getLogger().severe("...please delete the Drugs directory and try RESTARTING (not reloading).");
                }
            } catch (IOException e) {
                super.getLogger().severe("Failed to create config.");
                e.printStackTrace();
                super.getLogger().severe(Utils.color("&c...please delete the MoLevelz directory and try RESTARTING (not reloading)."));
            }
        }
    }
}

