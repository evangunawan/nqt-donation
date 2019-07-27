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
                sender.sendMessage("Donate at website.");
                return true;
            }

            if (args.length > 0) {
                target = (Player) sender;
                if (args.length == 2) {
                    target = CommandUtil.getTargetPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(ChatColor.RED + "ERROR: Player not found.");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("give")) {
                        sender.sendMessage(ChatColor.GOLD + "/donate give " + target.getName() + " [tier]");
                        return true;
                    }

                }
                if (args.length > 2) {
                    boolean playerAlreadyDonated = false;
                    String playerGroup;
                    playerGroup = CommandUtil.getTargetSingleGroup(target);
                    target = CommandUtil.getTargetPlayer(args[1]);

                    //Check player's group, already donated/not.
                    for (DonationTier item : CommandUtil.tiers) {
                        if (playerGroup.equalsIgnoreCase(item.getGroupName())) {
                            playerAlreadyDonated = true;
                        }
                    }

                    if (args[0].equalsIgnoreCase("give")) {
                        String groupName = CommandUtil.getTierGroup(args[2]);
                        if (target != null && groupName != null) {

                            //Give the target donation group, update the database.
                            if (!playerAlreadyDonated) {
                                if (DatabaseHandler.addDonation(target.getName(), groupName)) {
                                    //Entry added to database, give user the group.
//                                    PermissionHandler.getPerms().playerRemoveGroup(target,playerGroup);
//                                    PermissionHandler.getPerms().playerAddGroup(target,groupName);
                                    server.dispatchCommand(server.getConsoleSender(), "manuadd " + target.getName() + " " + groupName);
                                    sender.sendMessage(ChatColor.GREEN + "Successfully gave donation to player.");
                                } else {
                                    sender.sendMessage(ChatColor.RED + "ERROR: Database entry already found for the user.");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "ERROR: Player is already donated.");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "ERROR: Player/Group not found.");
                        }
                        return true;
                    }

                    //TODO:ADD DELETE DONATION.
                    if (args[0].equalsIgnoreCase("remove")) {
                        if (playerAlreadyDonated) {
                            //TODO:Add previous group, to rollback group promotion.
                            if(DatabaseHandler.removeDonation(target.getName())){
                                server.dispatchCommand(server.getConsoleSender(), "manuadd " + target.getName() + " player");
                            }else{
                                sender.sendMessage(ChatColor.RED + "ERROR: Sql query error.");
                            }
                        }
                    }
                }

                if (args[0].equalsIgnoreCase("status")) {
                    String[] targetGroups = PermissionHandler.getPerms().getPlayerGroups(target);
                    for (String group : targetGroups) {
                        sender.sendMessage("Player: " + target.getName() + "\nGroups: " + targetGroups.toString());
                    }

                    return true;
                }

            }
        }
        return false;
    }
}
