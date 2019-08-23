package com.evangunawan.donation.Commands;

import com.evangunawan.donation.Model.DonationTier;
import com.evangunawan.donation.Util.DatabaseHandler;
import com.evangunawan.donation.Util.PermissionHandler;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandDonate implements CommandExecutor {

    private Player target;
    private Server server;

    public CommandDonate(Server server) {
        this.server = server;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.GOLD + "Donate at website.");
                return true;
            }else{
                if (args.length == 1 && args[0].equalsIgnoreCase("status")) {
                    target = (Player) sender;
                    String targetGroups = CommandUtil.getTargetSingleGroup(target);
                    sender.sendMessage("Group: "+ targetGroups);

                    return true;
                }

                //Below is admin commands.
                //Permission check
                if(!sender.hasPermission("donation.admin")){
                    sender.sendMessage(ChatColor.RED + "You are not permitted to use this command.");
                    return true;
                }

                if (args[0].equalsIgnoreCase("give")) {
                    if(args.length < 3){
                        sender.sendMessage(ChatColor.GOLD + "/donate give [player] [tier]");
                        return true;
                    }
                    String groupName = CommandUtil.getTierGroup(args[2]);
                    if (CommandUtil.isPlayerExist(args[1]) && groupName!=null) {
                        if (DatabaseHandler.addDonation(args[1], groupName)) {

                            //Entry added to database, give user the group.
                            server.dispatchCommand(server.getConsoleSender(), "manuadd " + args[1] + " " + groupName);
                            sender.sendMessage(ChatColor.GREEN + "Successfully gave donation to player.");

                        } else {
                            sender.sendMessage(ChatColor.RED + "ERROR: Database entry already found for the user, or SQL error (Check Console)");
                        }

                    } else {
                        sender.sendMessage(ChatColor.RED + "ERROR: Player/Group not found.");
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("remove")){
                    if(args.length == 1){
                        sender.sendMessage(ChatColor.GOLD + "/donate remove [player]");
                        return true;
                    }
                    if(CommandUtil.isPlayerExist(args[1])){
                        if(DatabaseHandler.removeDonation(args[1])){
                            server.dispatchCommand(server.getConsoleSender(), "manuadd " + args[1] + " player");
                            sender.sendMessage(ChatColor.GREEN + "Successfully moved the player to default group.");
                        }else{
                            sender.sendMessage(ChatColor.RED + "ERROR: Sql query error. Check console for more information.");
                        }
                    }else{
                        sender.sendMessage(ChatColor.RED + "ERROR: Player not found.");

                    }

                    return true;
                }
                //TODO: Add /donate extend [user] to extend user's donation with 1 period (1 month)
            }
        }
        return false;
    }
}
