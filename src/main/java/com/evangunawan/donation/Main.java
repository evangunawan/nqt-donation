package com.evangunawan.donation;

import com.evangunawan.donation.Commands.CommandDonate;
import com.evangunawan.donation.Commands.CommandUtil;
import com.evangunawan.donation.Util.DatabaseHandler;
import com.evangunawan.donation.Util.DonationHandler;
import com.evangunawan.donation.Util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private FileConfiguration mainConfig;

    @Override
    public void onEnable() {
        getLogger().info("Donation plugin enabled.");

        //Configs Initialization
        saveDefaultConfig();
        mainConfig = new YamlConfiguration();
        mainConfig = getConfig();

        //Commands
        this.getCommand("donate").setExecutor(new CommandDonate(getServer(), mainConfig));

        //Initializations
        initPlayerList();
        PermissionHandler.setupPermissions();
        DatabaseHandler.init(mainConfig,this.getServer());
        CommandUtil.initDonationTiers(mainConfig);
        DonationHandler.init();

        getLogger().info("Loaded " + CommandUtil.tiers.size() + " donation tiers.");
        getLogger().info("Loaded " + CommandUtil.playerList.size() + " offline players");
    }

    private void initPlayerList() {
        if (Bukkit.getServer().getOfflinePlayers() != null) {
            getLogger().info("Loading Offline Players...");
            CommandUtil.playerList.clear();
            for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
                CommandUtil.playerList.add(player);

            }
        }
    }

    //Test Commands
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("offplayers") && sender instanceof ConsoleCommandSender){
            initPlayerList();
            sender.sendMessage("Offline Player List: " + CommandUtil.playerList.size());
            if(CommandUtil.playerList.size()>0){
                String plList = "";
                for (OfflinePlayer item : CommandUtil.playerList){
                    plList += item.getName() + ", ";
                }
                sender.sendMessage(ChatColor.GOLD + "List: " + plList);
            }
        }
        return true;
    }
}
