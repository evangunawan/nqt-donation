package com.evangunawan.donation.Util;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class DonationCommandExecutor {

    private FileConfiguration config;
    private List<String> commandOnStart;
    private List<String> commandOnEnd;

    public DonationCommandExecutor(FileConfiguration config){
        this.config = config;
        commandOnStart = new ArrayList<>();
        commandOnEnd = new ArrayList<>();
    }
    public void executeStartCommand(String playerName, String groupName){
        commandOnStart = config.getStringList("donations." + groupName + ".commands-start");
        if(commandOnStart.size()>0){
            for (String cmd : commandOnStart){
                cmd = cmd.replace("{player}", playerName);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),cmd);
            }
        }
    }

    public void executeEndCommand(String playerName){
        commandOnEnd = config.getStringList("settings.global-command-end");
        Bukkit.getLogger().info("CommandExecutor Called. Executing " + commandOnEnd.size() + " commands.");
        if(commandOnEnd.size()>0){
            for(String cmd : commandOnEnd){
                cmd = cmd.replace("{player}", playerName);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),cmd);
            }
        }
    }

}
