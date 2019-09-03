package com.evangunawan.donation.Util;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DonationCheckThread extends BukkitRunnable {
    private final JavaPlugin plugin;

    public DonationCheckThread(JavaPlugin pl){
        this.plugin = pl;
    }

    @Override
    public void run() {

    }
}
