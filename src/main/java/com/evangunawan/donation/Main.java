package com.evangunawan.donation;

import com.evangunawan.donation.Commands.CommandDonate;
import com.evangunawan.donation.Commands.CommandUtil;
import com.evangunawan.donation.Util.PermissionHandler;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {
    private File myConfig;
    private YamlConfiguration mainConfig;

    @Override
    public void onEnable() {
        getLogger().info("Donation plugin enabled.");
        this.getCommand("donate").setExecutor(new CommandDonate());
        PermissionHandler.setupPermissions();
        initPlayerList();
        initConfigs();
    }

    private void initPlayerList(){
        if(Bukkit.getServer().getOfflinePlayers() != null){
            for(OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()){
                CommandUtil.playerList.add(player);

            }
        }
    }

    private void initConfigs(){
        saveDefaultConfig();

        saveConfig();
    }

}
