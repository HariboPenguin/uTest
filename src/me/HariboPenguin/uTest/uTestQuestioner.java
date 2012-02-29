package me.HariboPenguin.uTest;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class uTestQuestioner implements CommandExecutor {

    public uTest plugin;
    public uTestCommandHandler ch;

    public uTestQuestioner(uTest instance) {
        this.plugin = instance;
    }

    public uTestQuestioner(uTestCommandHandler instance) {
        this.ch = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (player != null) {
            if (args.length >= 1) {

                String answer = "";
                Integer count = 1;

                while (count <= args.length) {
                    if (count == 1) {
                        answer = args[count - 1];
                    } else {
                        answer = answer + " " + args[count - 1];
                    }
                    count = count + 1;
                }
                
                try {
                    plugin.sqldh.answerQuestion(player, player.getName(), answer);
                    return true;
                } catch (SQLException ex) {
                    Logger.getLogger(uTestQuestioner.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "You haven't given an answer!");
                return true;
            }
        } else {
            sender.sendMessage(plugin.prefix + ChatColor.RED + "You cannot use this command from console!");
            return true;
        }
        return false;
    }
}
