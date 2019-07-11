package com.evangunawan.donation.Commands;

import com.evangunawan.donation.Util.PermissionHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandDonate implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
            if (args.length < 2) {
                if (args.length==0) {
                    sender.sendMessage("Donate at website.");

                } else {
                    if (args[0].equalsIgnoreCase("list")) {
                        String list = "";
                        for (OfflinePlayer player : CommandUtil.playerList) {
                            list += player.getName() + ", ";
                        }
                        sender.sendMessage("All Registered Players: " + list);
                    }
                }

            } else if (args.length == 2) {
                String playerName = args[1];
                Player target = CommandUtil.getTargetPlayer(playerName);

                if (args[0].equalsIgnoreCase("give")) {
                    target.sendMessage("You have been donated. Thank you.");

                } else if (args[0].equalsIgnoreCase("status")) {
                    String[] targetGroups = PermissionHandler.getPerms().getPlayerGroups(target);
                    sender.sendMessage("Groups: " + targetGroups + "\nDonate: donation");
                }
            }
            return true;
        }
        return false;
    }
}
