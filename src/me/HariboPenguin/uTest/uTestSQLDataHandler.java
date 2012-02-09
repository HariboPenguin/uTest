package me.HariboPenguin.uTest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class uTestSQLDataHandler {

    public uTest plugin;

    public uTestSQLDataHandler(uTest instance) {
        this.plugin = instance;
    }

    public boolean getPlayerApplication(CommandSender sender, String lookupPlayer) throws SQLException {

        String database = plugin.getConfig().getString("mysql.database");

        String query = "SELECT * FROM `" + database + "`.`applications` WHERE `applicant` LIKE '" + lookupPlayer + "'";
        ResultSet results = plugin.dbManageMySQL.query(query);

        while (results.next()) {

            Integer id = results.getInt("id");
            Date applicationDate = results.getDate("applicationdate");
            Time applicationTime = results.getTime("applicationtime");
            String applicant = results.getString("applicant");
            String playtime = results.getString("TimePlayed");
            Integer bannedcheck = results.getInt("bannedcheck");
            Integer singleplayercheck = results.getInt("singleplayercheck");
            Integer multiplayercheck = results.getInt("multiplayercheck");
            String referrer = results.getString("referrer");
            String message = results.getString("message");

//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String textDate = dateFormat.format(applicationDate);

            sender.sendMessage(ChatColor.DARK_PURPLE + "----------" + ChatColor.GOLD + " uTest " + ChatColor.GOLD + "- " + applicant + "'s Application " + ChatColor.DARK_PURPLE + " ----------");
            sender.sendMessage(ChatColor.RED + "Application Date" + ChatColor.GOLD + " - " + ChatColor.GRAY + applicationDate + " " + applicationTime);
            sender.sendMessage(ChatColor.RED + "Time Played" + ChatColor.GOLD + " - " + ChatColor.GRAY + playtime);
            sender.sendMessage(ChatColor.RED + "Banned before" + ChatColor.GOLD + " - " + ChatColor.GRAY + bannedcheck);
            sender.sendMessage(ChatColor.RED + "Played single player" + ChatColor.GOLD + " - " + ChatColor.GRAY + singleplayercheck);
            sender.sendMessage(ChatColor.RED + "Played multiplayer" + ChatColor.GOLD + " - " + ChatColor.GRAY + multiplayercheck);
            sender.sendMessage(ChatColor.RED + "Referrer" + ChatColor.GOLD + " - " + ChatColor.GRAY + referrer);
            sender.sendMessage(ChatColor.RED + "Application message" + ChatColor.GOLD + " - " + ChatColor.GRAY + message);

        }

        results.close();

        return true;
    }

    public boolean getOpenApplications(CommandSender sender, int pageNumber) throws SQLException {

        String database = plugin.getConfig().getString("mysql.database");

        String query = "SELECT * FROM `" + database + "`.`applications` WHERE `applicant` IS NOT NULL";
        String getNumberOfResults = "SELECT COUNT(*) AS count FROM `" + database + "`.`applications` WHERE `applicant` IS NOT NULL";
        ResultSet results = plugin.dbManageMySQL.query(query);
        ResultSet resultCount = plugin.dbManageMySQL.query(getNumberOfResults);
        
        resultCount.first();

        int numberOfResults = resultCount.getInt("count");
        
        sender.sendMessage("Result Count: " + numberOfResults);

        int resultsPerPage = plugin.getConfig().getInt("results-per-page");
        
        sender.sendMessage("Results per page: " + resultsPerPage);
        
        int pageCount = Math.round((int) Math.ceil((numberOfResults - 1) / resultsPerPage)) + 1;
        
        sender.sendMessage("Page Count: " + pageCount);
        
        if (pageNumber > pageCount) {
            sender.sendMessage(plugin.prefix + ChatColor.GOLD + " - " + ChatColor.RED + "That page does not exist!");
            return true;
        }

        if (numberOfResults == 0) {
            sender.sendMessage(plugin.prefix + ChatColor.GOLD + " - " + ChatColor.RED + "No Reports Found!");
            return true;
        }

        sender.sendMessage(ChatColor.DARK_PURPLE + "--------------" + ChatColor.GOLD + " uTest " + ChatColor.GOLD + "- Latest Applications " + ChatColor.DARK_PURPLE + " --------------");
        sender.sendMessage(ChatColor.DARK_PURPLE + "-------------------- " + ChatColor.GOLD + "Page " + pageNumber + " / " + pageCount + ChatColor.DARK_PURPLE + " ---------------------");
        
        
        if (pageNumber > 1) {
            int startRow = pageNumber * resultsPerPage - resultsPerPage;

            for (int x = 0; x < startRow; x++) {
                results.next();
            }
        }
        
        int counter = 0;

        while (results.next() && counter < resultsPerPage) {

            Integer id = results.getInt("id");
            Date applicationDate = results.getDate("applicationdate");
            Time applicationTime = results.getTime("applicationtime");
            String applicant = results.getString("applicant");
            String playtime = results.getString("TimePlayed");
            Integer bannedcheck = results.getInt("bannedcheck");
            Integer singleplayercheck = results.getInt("singleplayercheck");
            Integer multiplayercheck = results.getInt("multiplayercheck");
            String referrer = results.getString("referrer");
            String message = results.getString("message");
            
            if (plugin.getServer().getPlayer(applicant) != null) {
                sender.sendMessage(ChatColor.GOLD + "#" + id + " " + applicationDate + " " + applicationTime + " - " + ChatColor.GREEN + applicant);
            } else {
                sender.sendMessage(ChatColor.GOLD + "#" + id + " " + applicationDate + " " + applicationTime + " - " + ChatColor.RED + applicant);
            }
            
            counter++;

        }

        results.close();
        resultCount.close();

        return true;
    }
}
