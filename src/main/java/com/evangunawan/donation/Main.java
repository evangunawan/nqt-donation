package com.evangunawan.donation;

import com.evangunawan.donation.Commands.CommandDonate;
import com.evangunawan.donation.Commands.CommandUtil;
import com.evangunawan.donation.Util.DatabaseHandler;
import com.evangunawan.donation.Util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private FileConfiguration mainConfig;

    @Override
    public void onEnable() {
        getLogger().info("Donation plugin enabled.");
        this.getCommand("donate").setExecutor(new CommandDonate());


        //Configs Initialization
        initConfigs();
        mainConfig = new YamlConfiguration();
        mainConfig = getConfig();

        //Initializations
        initPlayerList();
        PermissionHandler.setupPermissions();
        DatabaseHandler.init(mainConfig);

    }

    private void initPlayerList() {
        if (Bukkit.getServer().getOfflinePlayers() != null) {
            for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
                CommandUtil.playerList.add(player);

            }
        }
    }

    private void initConfigs() {
        saveDefaultConfig();
        saveConfig();
    }

}
