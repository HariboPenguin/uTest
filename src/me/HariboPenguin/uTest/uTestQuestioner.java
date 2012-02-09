package me.HariboPenguin.uTest;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class uTestQuestioner implements CommandExecutor {
    
    public uTest plugin;
    public uTestCommandHandler ch;
    
    public uTestQuestioner(uTest instance) {
        this.plugin = instance;
    }
    
    public uTestQuestioner(uTestCommandHandler instance) {
        this.ch =  instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
        if (args.length >= 1) {
            
            
            
        } else {
            
        }
        
        return false;
    }
    
    
    
}
