package me.HariboPenguin.uTest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class uTestSQLDataHandler {

    public uTest plugin;

    public uTestSQLDataHandler(uTest instance) {
        this.plugin = instance;
    }

    // Application Query's
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
            sender.sendMessage(plugin.prefix + ChatColor.GOLD + " - " + ChatColor.RED + "No Applications Found!");
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

    public boolean checkIfPlayerApplied(String playername) throws SQLException {

        String database = plugin.getConfig().getString("mysql.database");

        String query = "SELECT COUNT(*) AS count FROM `" + database + "`.`applications` WHERE applicant LIKE '" + playername + "'";
        ResultSet numberOfApplications = plugin.dbManageMySQL.query(query);

        numberOfApplications.first();

        int numOfApps = numberOfApplications.getInt("count");

        if (numOfApps >= 1) {
            return true;
        } else {
            return false;
        }
    }

    // Test query's
    public boolean getPlayerTest(CommandSender sender, String lookupPlayer) throws SQLException {

        String database = plugin.getConfig().getString("mysql.database");
        Integer numberOfQuestions = plugin.getConfig().getList("quiz-questions").size();

        String query = "SELECT * FROM `" + database + "`.`quizzes` WHERE `applicant` LIKE '" + lookupPlayer + "'";
        String getNumberOfResults = "SELECT COUNT(*) AS count FROM `" + database + "`.`quizzes` WHERE `applicant` LIKE '" + lookupPlayer + "'";
        ResultSet results = plugin.dbManageMySQL.query(query);
        ResultSet numberOfResultsCount = plugin.dbManageMySQL.query(getNumberOfResults);

        numberOfResultsCount.first();

        int numberOfResults = numberOfResultsCount.getInt("count");

        if (numberOfResults == 0) {
            sender.sendMessage(plugin.prefix + ChatColor.RED + "No test results were found for that player!");
            return false;
        }

        results.first();

        Integer ID = results.getInt("id");
        Date testDate = results.getDate("testdate");
        Time testTime = results.getTime("testtime");
        String applicant = results.getString("applicant");

        int questionCounter = 1;

        sender.sendMessage(ChatColor.DARK_PURPLE + "-------- " + ChatColor.GOLD + "uTest - Test Results for " + applicant + ChatColor.DARK_PURPLE + " --------");
//        sender.sendMessage(ChatColor.RED + "Test ID" + ChatColor.GOLD + " - " + ChatColor.GRAY + ID);
//        sender.sendMessage(ChatColor.RED + "Test Date" + ChatColor.GOLD + " - " + ChatColor.GRAY + testDate);
//        sender.sendMessage(ChatColor.RED + "Test Time" + ChatColor.GOLD + " - " + ChatColor.GRAY + testTime);
//        sender.sendMessage(ChatColor.DARK_PURPLE + "--------------" + ChatColor.GOLD + "Results" + ChatColor.DARK_PURPLE + "---------------");

        while (questionCounter <= numberOfQuestions) {

            String question = (String) plugin.getConfig().getList("quiz-questions").get(questionCounter - 1);

            sender.sendMessage(ChatColor.RED + question + ChatColor.GOLD + " - " + ChatColor.GRAY + results.getString("question" + questionCounter + "answer"));

            questionCounter++;

        }


        return true;
    }

    public boolean getOpenTests(CommandSender sender, int pageNumber) throws SQLException {

        String database = plugin.getConfig().getString("mysql.database");
        Integer numberOfQuestions = plugin.getConfig().getList("quiz-questions").size();

        String query = "SELECT * FROM `" + database + "`.`quizzes` WHERE `applicant` IS NOT NULL";
        String getNumberOfResults = "SELECT COUNT(*) AS count FROM `" + database + "`.`quizzes` WHERE `applicant` IS NOT NULL";
        ResultSet results = plugin.dbManageMySQL.query(query);
        ResultSet numberOfResultsCount = plugin.dbManageMySQL.query(getNumberOfResults);
        
        numberOfResultsCount.first();
        
        int numberOfResults = numberOfResultsCount.getInt("count");
        
        int resultsPerPage = plugin.getConfig().getInt("results-per-page");
        
        int pageCount = Math.round((int) Math.ceil((numberOfResults - 1) / resultsPerPage)) + 1;
        
        if (pageNumber > pageCount) {
            sender.sendMessage(plugin.prefix + ChatColor.GOLD + " - " + ChatColor.RED + "That page does not exist!");
            return true;
        }

        if (numberOfResults == 0) {
            sender.sendMessage(plugin.prefix + ChatColor.GOLD + " - " + ChatColor.RED + "No Tests Found!");
            return true;
        }
        
        sender.sendMessage(ChatColor.DARK_PURPLE + "----------------- " + ChatColor.GOLD + "uTest - Latest Tests" + ChatColor.DARK_PURPLE + " -----------------");
        sender.sendMessage(ChatColor.DARK_PURPLE + "-------------------- " + ChatColor.GOLD + "Page " + pageNumber + " / " + pageCount + ChatColor.DARK_PURPLE + " ---------------------");

        if (pageNumber > 1) {
            int startRow = pageNumber * resultsPerPage - resultsPerPage;

            for (int x = 0; x < startRow; x++) {
                results.next();
            }
        }
        
        int counter = 0;

        while (results.next() && counter < resultsPerPage) {

            Integer ID = results.getInt("id");
            Date testDate = results.getDate("testdate");
            Time testTime = results.getTime("testtime");
            String applicant = results.getString("applicant");

            if (plugin.getServer().getPlayer(applicant) != null) {
                sender.sendMessage(ChatColor.GOLD + "#" + ID + " " + testDate + " " + testTime + " - " + ChatColor.GREEN + applicant);
            } else {
                sender.sendMessage(ChatColor.GOLD + "#" + ID + " " + testDate + " " + testTime + " - " + ChatColor.RED + applicant);
            }
            
            counter++;
        }

        return true;
    }

    public boolean setAppStatus(String status, String playername) throws SQLException {

        String database = plugin.getConfig().getString("mysql.database");

        String query = "UPDATE `" + database + "`.tracker SET `status` = '" + status + "' WHERE `applicant` LIKE '" + playername + "'";
        plugin.dbManageMySQL.query(query);

        return true;
    }

    public boolean startTest(String playername) throws SQLException {

        String database = plugin.getConfig().getString("mysql.database");

        String getID = "SELECT * FROM `" + database + "`.`tracker` WHERE `applicant` LIKE '" + playername + "'";
        ResultSet results = plugin.dbManageMySQL.query(getID);

        results.first();

        Integer ID = results.getInt("id");
        String applicant = results.getString("applicant");

        String insertTestInfo = "INSERT INTO `" + database + "`.`quizzes` (`id` ,`applicant`) VALUES ('" + ID + "' ,'" + applicant + "')";
        plugin.dbManageMySQL.query(insertTestInfo);

        String query = "SELECT * FROM `" + database + "`.`tracker` WHERE `applicant` = '" + applicant + "'";
        ResultSet result = plugin.dbManageMySQL.query(query);

        result.first();

        Integer attemptAmount = result.getInt("quizattempt");

        String setAttempt = "UPDATE `" + database + "`.`tracker` SET `quizattempt`='" + (attemptAmount + 1) + "' WHERE `applicant`='" + applicant + "'";
        plugin.dbManageMySQL.query(setAttempt);

        return true;
    }

    public boolean answerQuestion(Player player, String applicant, String answer) throws SQLException {

        String database = plugin.getConfig().getString("mysql.database");
        int numberOfQuestions = plugin.getConfig().getList("quiz-questions").size();

        String getQuestionNumber = "SELECT * FROM `" + database + "`.`tracker` WHERE `applicant` = '" + applicant + "'";
        ResultSet result = plugin.dbManageMySQL.query(getQuestionNumber);

        result.first();

        String qNumberString = result.getString("status");

        if (qNumberString.equalsIgnoreCase("TestCompleted")) {
            player.sendMessage(plugin.prefix + ChatColor.RED + "You have already completed the test!");
            return false;
        }

        if (qNumberString.equalsIgnoreCase("Applied")) {
            player.sendMessage(plugin.prefix + ChatColor.RED + "You have applied but have not started the test!");
            return false;
        }

        int qNumber = Integer.parseInt(qNumberString.replaceAll("[\\D]", ""));

        String query = "UPDATE `" + database + "`.`quizzes` SET `question" + qNumber + "answer` = '" + answer + "' WHERE `applicant` = '" + applicant + "'";
        plugin.dbManageMySQL.query(query);

        if ((qNumber + 1) <= numberOfQuestions) {
            String updateStatus = "UPDATE `" + database + "`.`tracker` SET `status` = 'Q" + (qNumber + 1) + "' WHERE `applicant` = '" + applicant + "'";
            plugin.dbManageMySQL.query(updateStatus);
            player.sendMessage(plugin.prefix + ChatColor.GREEN + "Your answer has been submitted!");

            String nextQuestion = (String) plugin.getConfig().getList("quiz-questions").get(qNumber);

            player.sendMessage(plugin.prefix + ChatColor.GOLD + "Q" + (qNumber + 1) + " - " + nextQuestion);

        } else if (qNumber == numberOfQuestions) {

            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String textTime = timeFormat.format(date);
            String textDate = dateFormat.format(date);

            String updateTestDateTime = "UPDATE `" + database + "`.`quizzes` SET `testdate` = '" + textDate + "', `testtime` = '" + textTime + "' WHERE `applicant` = '" + applicant + "'";
            String updateStatus = "UPDATE `" + database + "`.`tracker` SET `status` = 'TestCompleted' WHERE `applicant` = '" + applicant + "'";
            plugin.dbManageMySQL.query(updateTestDateTime);
            plugin.dbManageMySQL.query(updateStatus);
            player.sendMessage(plugin.prefix + ChatColor.GREEN + "You have completed the test!");
            plugin.getServer().broadcastMessage(plugin.prefix + ChatColor.GREEN + "A new member test has been completed! - Do /utest latest tests to view");
        }
        return true;
    }

    public boolean getStatus(CommandSender sender, String playername) throws SQLException {

        String database = plugin.getConfig().getString("mysql.database");

        String query = "SELECT * FROM `" + database + "`.`tracker` WHERE `applicant` LIKE '" + playername + "'";
        ResultSet result = plugin.dbManageMySQL.query(query);

        result.first();

        Integer id = result.getInt("id");
        String applicant = result.getString("applicant");
        String status = result.getString("status");

        sender.sendMessage("Application ID: " + id);
        sender.sendMessage("Applicant: " + applicant);
        sender.sendMessage("Status: " + status);

        return true;
    }

    public boolean checkIfQuizHasBeenStarted(String applicant) throws SQLException {

        String database = plugin.getConfig().getString("mysql.database");

        String query = "SELECT * FROM `" + database + "`.`tracker` WHERE `applicant` = '" + applicant + "'";
        ResultSet result = plugin.dbManageMySQL.query(query);

        result.first();

        String status = result.getString("status");

        if (status.matches("\\A[Q-Qq-q][0-9]+\\z")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkQuizAttemptAmount(String applicant) throws SQLException {

        String database = plugin.getConfig().getString("mysql.database");
        int maxAttempts = plugin.getConfig().getInt("quiz-settings.max-attempts");

        String query = "SELECT * FROM `" + database + "`.`tracker` WHERE `applicant` = '" + applicant + "'";
        ResultSet result = plugin.dbManageMySQL.query(query);

        result.first();

        Integer attemptAmount = result.getInt("quizattempt");

        if (attemptAmount >= maxAttempts) {
            return false;
        } else {
            return true;
        }
    }
    
    public boolean checkIfPlayerCompletedTest(String applicant) throws SQLException {
        
        String database = plugin.getConfig().getString("mysql.database");
        
        String query = "SELECT * FROM `" + database + "`.`tracker` WHERE `applicant` = '" + applicant + "'";
        ResultSet result = plugin.dbManageMySQL.query(query);
        
        result.first();
        
        String status = result.getString("status");
        
        if (status.equalsIgnoreCase("TestCompleted")) {
            return true;
        } else {
            return false;
        }
    }
}
