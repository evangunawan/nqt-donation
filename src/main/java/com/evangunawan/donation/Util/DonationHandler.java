package com.evangunawan.donation.Util;

import com.evangunawan.donation.Main;
import com.evangunawan.donation.Model.Donation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;

public class DonationHandler {
    private static ArrayList<Donation> donateList;
    private static Plugin plugin;
    private static DonationCommandExecutor cmdExecutor;
    private static long checkInterval;

    public static void init(JavaPlugin pl, FileConfiguration cf) {
        plugin = pl;
        cmdExecutor = new DonationCommandExecutor(cf);
        checkInterval = cf.getInt("settings.checker-interval");
        if(checkInterval == 0) checkInterval=72000L;

        donateList = new ArrayList<>();
        donateList = DatabaseHandler.getDonationList();
        if (donateList != null) {
            Bukkit.getServer().getLogger().info("Loaded " + donateList.size() + " active donations.");
        } else {
            Bukkit.getServer().getLogger().info("Did not found any active donations.");
        }
        startCheckerThread();

    }

    public static Donation getDonation(String username) {
        for (Donation x : donateList) {
            if (x.getUsername().equalsIgnoreCase(username)) {
                return x;
            }
        }
        return null;
    }

    public static void refreshDonationList(){
        donateList.clear();
        donateList = DatabaseHandler.getDonationList();
        if (donateList != null) {
            Bukkit.getServer().getLogger().info("Refresh - Loaded " + donateList.size() + " active donations.");
        } else {
            Bukkit.getServer().getLogger().info("Refresh - Did not found any active donations.");
        }

    }


    private static void startCheckerThread() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(
                plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (donateList.size() > 0) {
                            long currentTime = java.lang.System.currentTimeMillis();
                            for (Donation donation : donateList) {
//                                Bukkit.getLogger().info(ChatColor.GOLD + "DEBUG: Running donation checker task..." );
                                long endDate = donation.getEndDate();
                                int compareResult = Long.compare(endDate, currentTime);
                                if (compareResult < 0) {
                                    //Remove the donation status.
                                    if (DatabaseHandler.removeDonation(donation.getUsername())) {
                                        cmdExecutor.executeEndCommand(donation.getUsername());
                                        Bukkit.getLogger().info(ChatColor.GREEN + "Player " + donation.getUsername() + " is no longer donated.");
                                    } else {
                                        Bukkit.getLogger().severe(ChatColor.RED + "ERROR: Sql query error. Check console for more information.");
                                    }

                                }
                            }
                        }

                        refreshDonationList();
                    }
                }, 0L, checkInterval
        );
    }

}
