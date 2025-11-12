package com.flaxa.onjoinevent.listeners;

import com.flaxa.onjoinevent.OnJoinEvent;
import com.flaxa.onjoinevent.managers.ScriptExecutor;
import com.flaxa.onjoinevent.models.Script;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;

public class PlayerJoinListener implements Listener {
    
    private final OnJoinEvent plugin;
    private final ScriptExecutor executor;
    
    public PlayerJoinListener(OnJoinEvent plugin) {
        this.plugin = plugin;
        this.executor = new ScriptExecutor(plugin);
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean isFirstJoin = !player.hasPlayedBefore();
        
        plugin.log("Player joined: " + player.getName() + " (First join: " + isFirstJoin + ")");
        
        // Execute scripts based on join type
        for (Map.Entry<String, Script> entry : plugin.getScriptManager().getLoadedScripts().entrySet()) {
            Script script = entry.getValue();
            
            switch (script.getType()) {
                case ON_PLAYER_JOIN:
                    // Execute for all player joins
                    executor.executeScript(script, player);
                    break;
                    
                case ON_PLAYER_FIRST_JOIN:
                    // Execute only for first-time joins
                    if (isFirstJoin) {
                        executor.executeScript(script, player);
                    }
                    break;
                    
                case ON_PLAYERNAME_JOINS:
                    // Execute for specific player
                    if (player.getName().equalsIgnoreCase(script.getTargetPlayer())) {
                        executor.executeScript(script, player);
                    }
                    break;
                    
                case ON_FIRST_PLAYERNAME_JOINS:
                    // Execute for specific player's first join
                    if (player.getName().equalsIgnoreCase(script.getTargetPlayer()) && isFirstJoin) {
                        executor.executeScript(script, player);
                    }
                    break;
            }
        }
    }
}