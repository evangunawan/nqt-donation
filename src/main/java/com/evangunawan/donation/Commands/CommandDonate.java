package com.evangunawan.donation.Commands;

import com.evangunawan.donation.Model.Donation;
import com.evangunawan.donation.Model.DonationTier;
import com.evangunawan.donation.Util.DatabaseHandler;
import com.evangunawan.donation.Util.DonationCommandExecutor;
import com.evangunawan.donation.Util.DonationHandler;
import com.evangunawan.donation.Util.PermissionHandler;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommandDonate implements CommandExecutor {

    private Player target;
    private DonationCommandExecutor cmdExecutor;

    public CommandDonate(FileConfiguration config) {
        cmdExecutor = new DonationCommandExecutor(config);
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
                    Donation donation = DonationHandler.getDonation(sender.getName());
                    if(donation!= null){
                        Date end_date = DatabaseHandler.getDonationEnd(sender.getName());
                        DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
                        sender.sendMessage(ChatColor.GOLD + "You are currently donated in " + donation.getTier() + " tier.\n" +
                                "Your donation is available until : " + df.format(end_date));
                    }
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
                            cmdExecutor.executeStartCommand(args[1],groupName);
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
                            cmdExecutor.executeEndCommand(args[1]);
                            sender.sendMessage(ChatColor.GREEN + "Successfully removed player donation.");
                        }else{
                            sender.sendMessage(ChatColor.RED + "ERROR: Sql query error. Check console for more information.");
                        }
                    }else{
                        sender.sendMessage(ChatColor.RED + "ERROR: Player not found.");
                    }

                    return true;
                } else if (args[0].equalsIgnoreCase("extend")){
                    if(args.length == 1){
                        sender.sendMessage(ChatColor.GOLD + "/donate extend [player]");
                        return true;
                    }
                    if(CommandUtil.isPlayerExist(args[1])){
                        if(DatabaseHandler.extendDonation(args[1])){
                            sender.sendMessage(ChatColor.GREEN + "Successfully extends player donation.");
                        }else{
                            sender.sendMessage(ChatColor.RED + "ERROR: Sql query error. Check console for more information.");
                        }
                    }else{
                        sender.sendMessage(ChatColor.RED + "ERROR: Player not found.");
                    }
                }
            }
        }
        return false;
    }
}
