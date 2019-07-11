package com.evangunawan.donation.Commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandUtil {

    public static ArrayList<OfflinePlayer> playerList = new ArrayList<>();

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
