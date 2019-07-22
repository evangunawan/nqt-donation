package com.evangunawan.donation.Commands;

import com.evangunawan.donation.Util.PermissionHandler;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandDonate implements CommandExecutor {

    private Player target;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
            if(args.length == 0){
                sender.sendMessage("Donate at website.");
                return true;
            }

            if(args.length > 0){
                target = (Player) sender;
                if(args.length == 2){
                    target = CommandUtil.getTargetPlayer(args[1]);

                    if(target == null){
                        sender.sendMessage(ChatColor.RED + "ERROR: Player not found.");
                        return true;
                    }

                    if (args[0].equalsIgnoreCase("give")) {
                        target.sendMessage(ChatColor.GOLD + "You have been donated. Thank you.");
                        return true;
                    }
                }

                if (args[0].equalsIgnoreCase("status")) {
                    String[] targetGroups = PermissionHandler.getPerms().getPlayerGroups(target);
                    sender.sendMessage("Player: " + target.getName() + "\nGroups: " + targetGroups + "\nDonate: donation");
                    return true;
                }

            }
        }
        return false;
    }
}
