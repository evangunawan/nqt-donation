package com.evangunawan.donation.Util;

import com.evangunawan.donation.Main;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;


public class DatabaseHandler {

    public static String dbHost = "";
    public static String dbName = "";
    public static String dbUser = "";
    public static String dbPass = "";
    public static int dbPort = 3306;
    private static Server sv = null;

    private static Connection dbconn;

    public static void init(FileConfiguration config, Server server) {
        sv = server;
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

    }

    private static synchronized int getUserId(String username){

        try{
            Statement st = dbconn.createStatement();
            String query = "SELECT id,username FROM users WHERE username='" + username + "';";
            ResultSet foundUser = st.executeQuery(query);

            if(foundUser.next() == false){
                return 0;
            }else{
                while(foundUser.next()){
                    int idFound = foundUser.getInt("id");
                    return idFound;
                }
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return 0;
    }

    public static synchronized boolean addDonation(String user, String groupName) {
        try{
            Statement st = dbconn.createStatement();

            //GetUserId
            Integer userId = getUserId(user);

            String sqlQuery = "SELECT users.username, donations.donation_tier FROM donations JOIN users ON donations.user_id=users.id";
            ResultSet userDonation = st.executeQuery(sqlQuery);

            if(userDonation.next() == false){
                String insertNewDonator = "INSERT INTO donations VALUES (" + userId + ",'" + groupName + "',"+ java.lang.System.currentTimeMillis() +");" ;
                st.executeQuery(insertNewDonator);
                return true;
            }else{
                //Do nothing.
                return false;
            }

        }catch (SQLException ex){
            sv.getLogger().warning("SQL ERROR: " + ex.getMessage());
        }
        return false;

    }

    public static synchronized boolean removeDonation(String user){
        try{
            Statement st = dbconn.createStatement();
            Integer userId = getUserId(user);

            String sqlQuery = "DELETE FROM donations WHERE user_id=" + userId + ";";
            st.executeQuery(sqlQuery);

            return true;

        }catch(SQLException ex){

        }


        return false;
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
