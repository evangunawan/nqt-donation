package com.evangunawan.donation.Util;

import com.evangunawan.donation.Model.Donation;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class DonationHandler {
    private static ArrayList<Donation> donateList;
    public static void init(){
        donateList = new ArrayList<>();
        donateList = DatabaseHandler.getDonationList();
        if(donateList!=null){
            Bukkit.getServer().getLogger().info("Loaded " + donateList.size() + " active donations.");
        }else{
            Bukkit.getServer().getLogger().info("Did not found any active donations.");
        }

        //TODO: finish this.

    }

}
