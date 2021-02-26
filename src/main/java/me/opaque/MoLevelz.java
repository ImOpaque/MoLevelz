package me.opaque;

import me.opaque.commands.LevelCommand;
import me.opaque.events.PlayerJoin;
import me.opaque.events.PlayerLeave;
import me.opaque.events.types.MobKill;
import me.opaque.player.LPLayerManager;
import me.opaque.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;

public final class MoLevelz extends JavaPlugin {

    public LPLayerManager lpLayerManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        lpLayerManager = new LPLayerManager(this);
        Utils.debug("&4&l<!> &cMoLevelz &4&l<!> &a&oEnabling...", this, false);
        registerEvents();
        registerCommands();
        generateConfig();
        generatePlayersConfig();
        lpLayerManager.loadConfig();
        new BukkitRunnable() {
            public void run() {
                lpLayerManager.saveToDisk();
            }

        }.runTaskTimer(this, 1000, 1000);
    }

    @Override
    public void onDisable() {
       lpLayerManager.saveToDisk();
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerLeave(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(this), this);
        Bukkit.getPluginManager().registerEvents(new MobKill(this, lpLayerManager), this);
    }

    public void registerCommands() {
        this.getCommand("level").setExecutor(new LevelCommand(lpLayerManager, this));
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

