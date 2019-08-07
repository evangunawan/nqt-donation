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
                int idFound = foundUser.getInt("id");
                sv.getLogger().info("DEBUG: Found userId: " + idFound);
                return idFound;
//                while(foundUser.next()){
//                    int idFound = foundUser.getInt("id");
//                    return idFound;
//                }
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return 0;
    }

    public static synchronized boolean isDonated(String user){
        try {
            Statement st = dbconn.createStatement();
            Integer userId = getUserId(user);

            String sqlQuery = "SELECT * FROM donations WHERE user_id=" + userId + ";";
            ResultSet userDonation = st.executeQuery(sqlQuery);

            if(userDonation.next() == false){
                return false;
            }else{
                return true;
            }

        } catch (SQLException e) {
            sv.getLogger().warning("SQL ERROR: " + e.getMessage());
        }
        return true;
    }

    public static synchronized boolean addDonation(String user, String groupName) {
        try{
            Statement st = dbconn.createStatement();

            //GetUserId
            Integer userId = getUserId(user);

            if(!isDonated(user)){
                String insertNewDonator = "INSERT INTO donations VALUES (" + userId + ",'" + groupName + "',"+ java.lang.System.currentTimeMillis() +");" ;
                st.executeUpdate(insertNewDonator);
                return true;
            }else{
                //User is already donated. Do nothing.
                throw new Exception("User is already donated.");
//                return false;
            }

        }catch (Exception ex){
            sv.getLogger().warning("SQL ERROR: " + ex.getMessage());
        }
        return false;

    }

    public static synchronized boolean removeDonation(String user){
        try{
            Statement st = dbconn.createStatement();
            Integer userId = getUserId(user);

            if(isDonated(user)){
                String sqlQuery = "DELETE FROM donations WHERE user_id=" + userId + ";";
                st.executeUpdate(sqlQuery);

                return true;
            }

        }catch(SQLException ex){
            sv.getLogger().warning("SQL ERROR: " + ex.getMessage());
        }
        return false;
    }

}
