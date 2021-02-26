package me.opaque.commands;

import me.opaque.MoLevelz;
import me.opaque.player.LPLayerManager;
import me.opaque.player.LPlayer;
import me.opaque.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelCommand implements CommandExecutor {

    private LPLayerManager lpLayerManager;
    private MoLevelz plugin;

    public LevelCommand(LPLayerManager lpLayerManager, MoLevelz plugin) {
        this.lpLayerManager = lpLayerManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("level")) {
            if (sender.hasPermission("molevelz.admin")) {
                if (args.length == 0) {
                    showStats(sender);
                    return true;
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("help")) {
                        showHelp(sender);
                    }
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("set")) {
                        if (!checkValid(args[1], args[2])) {
                            return true;
                        }

                        Player player = plugin.getServer().getPlayer(args[1]);
                        int level = Integer.parseInt(args[2]);

                        LPlayer lPlayer = lpLayerManager.getPlayer(player.getUniqueId());
                        lPlayer.setLevel(level);

                        sender.sendMessage(Utils.color("&4&l<!> &fSet &6" + player.getName() + "&6's &flevel to &c" + level));
                        sender.sendMessage(Utils.color("&fLevel: &c" + lPlayer.getLevel()));
                    } else if (args[0].equalsIgnoreCase("setxp")) {
                        if (!checkValid(args[1], args[2])) {
                            return true;
                        }

                        Player player = plugin.getServer().getPlayer(args[1]);
                        int xp = Integer.parseInt(args[2]);

                        LPlayer lPlayer = lpLayerManager.getPlayer(player.getUniqueId());
                        lPlayer.setXp(xp);

                        sender.sendMessage(Utils.color("&4&l<!> &fSet &6" + player.getName() + "&6's &fXp to &c" + xp));
                        sender.sendMessage(Utils.color("&fXp: &c" + lPlayer.getXp()));
                    }
                }
            }
        }
        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(Utils.color("&4&l<!> &cMoLevelz &4&l<!>"));
        sender.sendMessage(Utils.color(""));
        sender.sendMessage(Utils.color("&f/level &8| &7&oShows level"));
        sender.sendMessage(Utils.color("&f/level set <player> <level> &8| &7&oSets players level"));
        sender.sendMessage(Utils.color("&f/level setxp <player> <xp> &8| &7&oSets players xp"));
        sender.sendMessage(Utils.color("&f/level remove <player> <amount> &8| &7&oTakes away desired amount of lvls"));
        sender.sendMessage(Utils.color("&f/level removexp <player> <amount> &8| &7&oTakes away desired amount of xp"));
        sender.sendMessage(Utils.color("&f/level add <player> <amount> &8| &7&oAdds desired amount of levels to player"));
        sender.sendMessage(Utils.color("&f/level addxp <player> <amount> &8| &7&oAdds desired amount of xp to player"));
    }

    private void showStats(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            LPlayer lPlayer = lpLayerManager.getPlayer(player.getUniqueId());

            player.sendMessage(Utils.color("&4&l<!> &cStats &4&l<!>"));
            player.sendMessage(Utils.color(""));
            player.sendMessage(Utils.color("&7Name&8: &6" + player.getName()));
            player.sendMessage(Utils.color("&7Level&8: &4" + lPlayer.getLevel()));
            player.sendMessage(Utils.color("&7XP&8: &c" + lPlayer.getXp()));
        } else {
            sender.sendMessage(Utils.color("&cYOU ARE NOT A PLAYER THEREFORE YOU CANNOT SEE YOUR LEVEL"));
        }
    }

    protected Boolean checkValid(String player, String value) {
        if (plugin.getServer().getPlayerExact(player) == null) {
            return false;
        }

        try {
            Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }
}
