package com.evangunawan.donation.Commands;

import com.evangunawan.donation.Model.DonationTier;
import com.evangunawan.donation.Util.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandUtil {

    public static ArrayList<OfflinePlayer> playerList = new ArrayList<>();
    public static ArrayList<DonationTier> tiers = new ArrayList<>();

    public static void initDonationTiers(FileConfiguration config){
        ConfigurationSection donationTiers = config.getConfigurationSection("donations");
        for (String x : donationTiers.getKeys(false)){
            DonationTier dt = new DonationTier();
            dt.setGroup_name(donationTiers.getString(x + ".group"));

            tiers.add(dt);
        }

    }

    public static String getTargetSingleGroup(Player user){
        String[] targetGroups;
        targetGroups = PermissionHandler.getPerms().getPlayerGroups(user);
        return Arrays.asList(targetGroups).get(0);

    }

    public static String getTierGroup(String targetGroup){
        for(DonationTier tier : tiers){
            if(tier.getGroupName().equalsIgnoreCase(targetGroup)){
                return tier.getGroupName();
            }
        }
        return null;
    }

    public static Player getTargetPlayer(String name){
        Player target = Bukkit.getPlayer(name);

        if(target==null){
            return null;
        }else{
            if(target.isOnline() || playerList.contains(target) ){
                return target;
            }else{
                return null;
            }
        }
    }
}
