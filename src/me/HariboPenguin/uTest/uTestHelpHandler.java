package me.HariboPenguin.uTest;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class uTestHelpHandler {

    public uTest plugin;
    public uTestCommandHandler ch;

    public uTestHelpHandler(uTest instance) {
        this.plugin = instance;
    }

    public uTestHelpHandler(uTestCommandHandler instance) {
        this.ch = instance;
    }

    public boolean HelpMenu(CommandSender sender) {

        sender.sendMessage(ChatColor.DARK_PURPLE + "------------------- " + ChatColor.GOLD + "uTest Help Menu" + ChatColor.DARK_PURPLE + " -------------------");
        sender.sendMessage(ChatColor.RED + "/utest start [Player]" + ChatColor.GOLD + " - " + ChatColor.GRAY + "Starts a player's test");
        sender.sendMessage(ChatColor.RED + "/utest status [Player]" + ChatColor.GOLD + " - " + ChatColor.GRAY + "Shows the status of a player's test");
        sender.sendMessage(ChatColor.RED + "/utest end [Player]" + ChatColor.GOLD + " - " + ChatColor.GRAY + "Ends a player's test");
        sender.sendMessage(ChatColor.RED + "/utest view [app/test] [Player]" + ChatColor.GOLD + " - " + ChatColor.GRAY + "View a player's application / test results");
        sender.sendMessage(ChatColor.RED + "/utest latest [apps/tests]" + ChatColor.GOLD + " - " + ChatColor.GRAY + "List recent applications / tests");
        sender.sendMessage(ChatColor.RED + "/utest approve [Player]" + ChatColor.GOLD + " - " + ChatColor.GRAY + "Approves a player");
        sender.sendMessage(ChatColor.RED + "/utest reject [Player] [Reason]" + ChatColor.GOLD + " - " + ChatColor.GRAY + "Reject a player");
        sender.sendMessage(ChatColor.RED + "/utest hold [Player] [Time] [Reason]" + ChatColor.GOLD + " - " + ChatColor.GRAY + "Put a player's application on hold");
        sender.sendMessage(ChatColor.RED + "/utest help" + ChatColor.GOLD + " - " + ChatColor.GRAY + "Display's this help menu");
        
        return true;
    }
}
