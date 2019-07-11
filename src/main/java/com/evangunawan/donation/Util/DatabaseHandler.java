package com.evangunawan.donation.Util;

import com.evangunawan.donation.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseHandler {

    public static String dbHost = "";
    public static String dbName = "";
    public static String dbUser = "";
    public static String dbPass = "";
    public static int dbPort = 3306;

    private static Connection dbconn;

    public static void init(FileConfiguration config) {

        dbHost = config.getString("database.host");
        dbName = config.getString("database.database");
        dbUser = config.getString("database.user");
        dbPass = config.getString("database.password");
        dbPort = config.getInt("database.port");

        try {
            openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static synchronized void openConnection() throws SQLException, ClassNotFoundException {
        if (dbconn != null && !dbconn.isClosed()) {
            return;
        }

        Class.forName("com.mysql.jdbc.Driver");
        dbconn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName, dbUser, dbPass);
        System.out.println("Donations > Database Connected.");

    }

//    public static void testStatement(Plugin plugin) {
//        BukkitRunnable dbRunnable = new BukkitRunnable() {
//            @Override
//            public void run() {
//                System.out.println("TestStatement called.");
//            }
//        };
//
//        dbRunnable.runTaskAsynchronously(plugin);
//
//    }
}
