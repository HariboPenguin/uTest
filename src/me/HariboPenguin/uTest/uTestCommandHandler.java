package me.HariboPenguin.uTest;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
                    } else {
                        sender.sendMessage("You can not take a test as console!");
                        sender.sendMessage("To start a test for another player do /ut start [Player]");
                    }
                } else if (args.length == 2) {

                    if (Bukkit.getServer().getPlayer(args[1]) != null) {
                    } else {
                        sender.sendMessage("That player is not online!");
                    }

                } else {
                    sender.sendMessage("Too many arguments! - Correct usage is /utest start [Player]");
                }

            } else if (args[0].equalsIgnoreCase("status")) {

                if (args.length == 1) {
                } else if (args.length == 2) {
                } else {
                    sender.sendMessage("Too many arguments! - Correct usage is /utest status [Player]");
                }

            } else if (args[0].equalsIgnoreCase("end")) {

                if (args.length == 1) {
                } else if (args.length == 2) {
                } else {
                    sender.sendMessage("Too many arguments! - Correct usage is /utest end [Player]");
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
                        } else {
                            sender.sendMessage("Incorrect parameters entered! - Correct usage is /utest view [app/test] [Player]");
                        }

                    } else if (args.length > 3) {
                        sender.sendMessage("Too many arguments! - Correct usage is /utest view [app/test] [Player]");
                    } else {
                        sender.sendMessage("Not enough arguments - Correct usage is /utest view [app/test] [Player]");
                    }

                } else if (args.length == 1) {
                    sender.sendMessage("Not enough arguments! - Correct usage is /utest view [app/test] [Player]");
                } else {
                    sender.sendMessage("Too many arguments! - Correct usage is /utest view [app/test] [Player]");
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
                            // view latest tests
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
