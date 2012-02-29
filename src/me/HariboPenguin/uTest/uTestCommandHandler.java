package me.HariboPenguin.uTest;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class uTestCommandHandler implements CommandExecutor {

    private uTest plugin;
    private uTestHelpHandler helpHandler = new uTestHelpHandler(this);
    private uTestQuestioner questioner = new uTestQuestioner(this);

    public uTestCommandHandler(uTest plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("start")) {

                if (args.length == 1) {

                    if (player != null) {
                        // Start test for person entering the command if not being done from console

                        if (plugin.getConfig().getBoolean("manage-applications")) {
                            try {
                                if (plugin.sqldh.checkIfPlayerApplied(player.getName())) {

                                    if (!plugin.sqldh.checkIfQuizHasBeenStarted(player.getName())) {

                                        if (plugin.sqldh.checkQuizAttemptAmount(player.getName())) {
                                            String q1 = (String) plugin.getConfig().getList("quiz-questions").get(0);
                                            player.sendMessage("You have applied!");
                                            player.sendMessage("Your test has started!");
                                            player.sendMessage("Please answer all questions by doing /answer <answer>");
                                            player.sendMessage(plugin.prefix + ChatColor.GOLD + "Q1 - " + q1);
                                            plugin.sqldh.startTest(player.getName());
                                            plugin.sqldh.setAppStatus("Q1", player.getName());
                                            return true;
                                        } else if (!plugin.sqldh.checkQuizAttemptAmount(player.getName())) {
                                            player.sendMessage(plugin.prefix + ChatColor.RED + "You have reached the max number of attempts!");
                                            player.sendMessage(plugin.prefix + ChatColor.RED + "Contact a staff member if you would still like to take the test");
                                            return true;
                                        }

                                    } else if (plugin.sqldh.checkIfQuizHasBeenStarted(player.getName())) {
                                        player.sendMessage(plugin.prefix + ChatColor.RED + "You have already started your quiz!");
                                        return true;
                                    }



                                } else if (!plugin.sqldh.checkIfPlayerApplied(player.getName())) {
                                    player.sendMessage(plugin.prefix + ChatColor.RED + "You have not applied!");
                                    return true;
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(uTestCommandHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else {
                        }

                    } else {
                        sender.sendMessage(plugin.prefix + ChatColor.RED + "You cannot take a test as console!");
                        sender.sendMessage(plugin.prefix + ChatColor.RED + "To start a test for another player do /ut start [Player]");
                        return true;
                    }
                } else if (args.length == 2) {

                    if (Bukkit.getServer().getPlayer(args[1]) != null) {
                        String applicant = Bukkit.getServer().getPlayer(args[1]).getName();

                        if (plugin.getConfig().getBoolean("manage-applications")) {
                            try {
                                if (plugin.sqldh.checkIfPlayerApplied(applicant)) {
                                    sender.sendMessage("That player has applied!");
                                } else if (!plugin.sqldh.checkIfPlayerApplied(applicant)) {
                                    sender.sendMessage(plugin.prefix + ChatColor.RED + "That player has not applied!");
                                    return true;
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(uTestCommandHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                        }

                    } else {
                        sender.sendMessage(plugin.prefix + ChatColor.RED + "That player is not online!");
                        return true;
                    }

                } else {
                    sender.sendMessage("Too many arguments! - Correct usage is /utest start [Player]");
                    return true;
                }

            } else if (args[0].equalsIgnoreCase("status")) {

                if (args.length == 1) {

                    if (player != null) {
                        try {
                            plugin.sqldh.getStatus(sender, player.getName());
                            return true;
                        } catch (SQLException ex) {
                            Logger.getLogger(uTestCommandHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } else {
                        sender.sendMessage(plugin.prefix + ChatColor.RED + "You must specify a player when using this command from console!");
                        return true;
                    }

                } else if (args.length == 2) {

                    if (plugin.getServer().getPlayer(args[1]) != null) {
                        String applicant = plugin.getServer().getPlayer(args[1]).getName();
                        try {
                            plugin.sqldh.getStatus(sender, applicant);
                            return true;
                        } catch (SQLException ex) {
                            Logger.getLogger(uTestCommandHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } else {
                        String applicant = args[1];
                        try {
                            plugin.sqldh.getStatus(sender, applicant);
                            return true;
                        } catch (SQLException ex) {
                            Logger.getLogger(uTestCommandHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    sender.sendMessage(plugin.prefix + ChatColor.RED + "Too many arguments! - Correct usage is /utest status [Player]");
                    return true;
                }

            } else if (args[0].equalsIgnoreCase("end")) {

                if (args.length == 1) {
                } else if (args.length == 2) {
                } else {
                    sender.sendMessage("Too many arguments! - Correct usage is /utest end [Player]");
                    return true;
                }

            } else if (args[0].equalsIgnoreCase("view")) {

                if (args.length >= 1) {

                    if (args.length == 2 || args.length == 3) {

                        if (args[1].equalsIgnoreCase("app")) {

                            if (plugin.getConfig().getBoolean("manage-applications")) {
                                if (args.length == 3) {

                                    String applicant = args[2];

                                    try {
                                        plugin.sqldh.getPlayerApplication(sender, applicant);
                                    } catch (SQLException ex) {
                                        Logger.getLogger(uTestCommandHandler.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    return true;
                                }
                            } else {
                                sender.sendMessage(plugin.prefix + ChatColor.RED + "Application management is not enabled in config");
                                return true;
                            }

                        } else if (args[1].equalsIgnoreCase("test")) {

                            if (args.length == 3) {

                                String applicant = args[2];
                                try {
                                    plugin.sqldh.getPlayerTest(sender, applicant);
                                } catch (SQLException ex) {
                                    Logger.getLogger(uTestCommandHandler.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                return true;
                            } else {
                                sender.sendMessage(plugin.prefix + ChatColor.RED + "You haven't specified a player! - Correcy usage is /utest view test [Player]");
                                return true;
                            }

                        } else {
                            sender.sendMessage(plugin.prefix + ChatColor.RED + "Incorrect parameters entered! - Correct usage is /utest view [app/test] [Player]");
                            return true;
                        }

                    } else if (args.length > 3) {
                        sender.sendMessage(plugin.prefix + ChatColor.RED + "Too many arguments! - Correct usage is /utest view [app/test] [Player]");
                        return true;
                    } else {
                        sender.sendMessage(plugin.prefix + ChatColor.RED + "Not enough arguments - Correct usage is /utest view [app/test] [Player]");
                        return true;
                    }

                } else if (args.length == 1) {
                    sender.sendMessage(plugin.prefix + ChatColor.RED + "Not enough arguments! - Correct usage is /utest view [app/test] [Player]");
                } else {
                    sender.sendMessage(plugin.prefix + ChatColor.RED + "Too many arguments! - Correct usage is /utest view [app/test] [Player]");
                }

            } else if (args[0].equalsIgnoreCase("latest")) {

                if (args.length > 1 && args.length <= 3) {

                    if (args.length >= 2) {

                        if (args[1].equalsIgnoreCase("apps")) {

                            if (plugin.getConfig().getBoolean("manage-applications")) {

                                if (args.length == 2) {
                                    int pageNumber = 1;
                                    try {
                                        plugin.sqldh.getOpenApplications(sender, pageNumber);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (args.length == 3) {
                                    int pageNumber;
                                    try {
                                        pageNumber = Integer.parseInt(args[2]);
                                    } catch (NumberFormatException e1) {
                                        sender.sendMessage(plugin.prefix + ChatColor.GOLD + " - " + ChatColor.RED + "Page number can't be a word!");
                                        return true;
                                    }
                                    try {
                                        plugin.sqldh.getOpenApplications(sender, pageNumber);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } else {
                                sender.sendMessage(plugin.prefix + ChatColor.RED + "Application management is not enabled in config");
                                return true;
                            }


                            return true;

                        } else if (args[1].equalsIgnoreCase("tests")) {

                            if (args.length == 2) {
                                int pageNumber = 1;
                                try {
                                    plugin.sqldh.getOpenTests(sender, pageNumber);
                                    return true;
                                } catch (SQLException ex) {
                                    Logger.getLogger(uTestCommandHandler.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            }

                            if (args.length == 3) {
                                int pageNumber;
                                try {
                                    pageNumber = Integer.parseInt(args[2]);
                                    try {
                                        plugin.sqldh.getOpenTests(sender, pageNumber);
                                        return true;
                                    } catch (SQLException ex) {
                                        Logger.getLogger(uTestCommandHandler.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } catch (NumberFormatException e1) {
                                    sender.sendMessage(plugin.prefix + ChatColor.GOLD + " - " + ChatColor.RED + "Page number can't be a word!");
                                    return true;
                                }

                            }

                        } else {
                            sender.sendMessage(plugin.prefix + ChatColor.RED + "Incorrect usage! - Correct usage is /utest latest [apps/tests] [page]");
                            return true;
                        }

                    }

                } else {
                    sender.sendMessage(plugin.prefix + ChatColor.RED + "Please specify what you want to view - Correct usage: /utest latest [apps/tests]");
                    return true;
                }

            } else if (args[0].equalsIgnoreCase("approve")) {

                if (args.length == 2) {

                    if (Bukkit.getServer().getPlayer(args[1]) != null) {

                        Player playerToApprove = plugin.getServer().getPlayer(args[1]);
                        try {
                            if (plugin.sqldh.checkIfPlayerCompletedTest(playerToApprove.getName())) {
                                
                                String memberGroup = plugin.getConfig().getString("when-test-approved.member-group");
                                
                                plugin.getPerms().playerAddGroup(playerToApprove, memberGroup);
                                
                                plugin.getPerms().playerRemoveGroup(playerToApprove, "default");

                                sender.sendMessage(plugin.prefix + ChatColor.GREEN + playerToApprove.getName() + " has been promoted to member!");
                                playerToApprove.sendMessage(plugin.prefix + ChatColor.GREEN + "Congratulations! Your member application has been accepted and you have been promoted!");
                                
                                if (plugin.getConfig().getBoolean("when-test-approved.broadcast")) {
                                    
                                    String message = plugin.getConfig().getString("when-test-approved.broadcast-message");
                                    
                                    message.replaceAll("<player>", playerToApprove.getName());
                                    
                                    plugin.getServer().broadcastMessage(message);
                                }
                                
                                return true;
                            } else if (!plugin.sqldh.checkIfPlayerCompletedTest(playerToApprove.getName())) {
                                sender.sendMessage(plugin.prefix + ChatColor.RED + playerToApprove + " has not completed the test!");
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(uTestCommandHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                } else if (args.length == 1) {
                    sender.sendMessage(plugin.prefix + ChatColor.RED + "Not enough arguments! - Correct usage is /utest approve [Player]");
                    return true;
                } else {
                    sender.sendMessage(plugin.prefix + ChatColor.RED + "Too many aguments! - Correct usage is /utest approve [Player]");
                    return true;
                }
                return true;
            } else if (args[0].equalsIgnoreCase("reject")) {
            } else if (args[0].equalsIgnoreCase("hold")) {
            } else if (args[0].equalsIgnoreCase("help")) {
                helpHandler.HelpMenu(sender);
                return true;
            }
        } else {
            sender.sendMessage("uTest main help menu here");
        }
        return false;
    }
}
