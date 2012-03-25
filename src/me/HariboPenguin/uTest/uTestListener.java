package me.HariboPenguin.uTest;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

public class uTestListener implements Listener {

    public uTest plugin;

    public uTestListener(uTest plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        
        Player playerPlacingSign = event.getPlayer();

        String signLine1 = event.getLine(0);
        String signLine2 = event.getLine(1);
        String signLine3 = event.getLine(2);
        String signLine4 = event.getLine(3);

        playerPlacingSign.sendMessage("Line 1: " + signLine1);
        playerPlacingSign.sendMessage("Line 2: " + signLine2);
        playerPlacingSign.sendMessage("Line 3: " + signLine3);
        playerPlacingSign.sendMessage("Line 4: " + signLine4);
        
        if (event.getLine(0).contains("[uTest Q1]")) {
            event.setLine(0, "§4[uTest Q1]");
            event.setLine(1, "§b" + plugin.getConfig().getList("quiz-questions").get(0));
            playerPlacingSign.sendMessage(ChatColor.BLUE + "uTest sign successfully created!");
        }

    }
}
