package com.flaxa.onjoinevent.managers;

import com.flaxa.onjoinevent.OnJoinEvent;
import com.flaxa.onjoinevent.models.Script;
import com.flaxa.onjoinevent.models.ScriptAction;
import com.flaxa.onjoinevent.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class ScriptExecutor {
    
    private final OnJoinEvent plugin;
    
    public ScriptExecutor(OnJoinEvent plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Execute a script for a player
     */
    public void executeScript(Script script, Player player) {
        if (script == null || player == null) return;
        
        if (script.isConsoleLogging()) {
            plugin.getLogger().info("Executing script '" + script.getFileName() + "' for player: " + player.getName());
        }
        
        List<ScriptAction> actions = script.getActions();
        
        // Execute actions with proper delays
        new BukkitRunnable() {
            int actionIndex = 0;
            
            @Override
            public void run() {
                if (actionIndex >= actions.size()) {
                    this.cancel();
                    return;
                }
                
                ScriptAction action = actions.get(actionIndex);
                executeAction(action, player, script);
                actionIndex++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
    
    /**
     * Execute a single action
     */
    private void executeAction(ScriptAction action, Player player, Script script) {
        if (action.getType() == ScriptAction.ActionType.WAIT) {
            return; // Wait is handled by delay
        }
        
        long delay = action.getDelayTicks();
        
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            String command = replaceVariables(action.getCommand(), player, script);
            
            try {
                switch (action.getType()) {
                    case GIVE:
                        handleGive(player, command, script);
                        break;
                    case TELEPORT:
                        handleTeleport(player, command, script);
                        break;
                    case MESSAGE:
                        handleMessage(player, command, script);
                        break;
                    case TITLE:
                        handleTitle(player, command, script);
                        break;
                    case ACTIONBAR:
                        handleActionbar(player, command, script);
                        break;
                    case SOUND:
                        handleSound(player, command, script);
                        break;
                    case EFFECT:
                        handleEffect(player, command, script);
                        break;
                    case GAMEMODE:
                        handleGamemode(player, command, script);
                        break;
                    case CONSOLE_COMMAND:
                        handleConsoleCommand(command, script);
                        break;
                    case PLAYER_COMMAND:
                        handlePlayerCommand(player, command, script);
                        break;
                    case BROADCAST:
                        handleBroadcast(command, script);
                        break;
                }
            } catch (Exception e) {
                plugin.logError("Error executing action in script '" + script.getFileName() + "': " + action.getType(), e);
            }
        }, delay);
    }
    
    private void handleGive(Player player, String command, Script script) {
        String[] parts = command.split(" ", 2);
        if (parts.length < 2) return;
        
        Material material = Material.getMaterial(parts[1].toUpperCase());
        if (material != null) {
            int amount = 1;
            if (parts.length > 2) {
                try {
                    amount = Integer.parseInt(parts[2]);
                } catch (NumberFormatException ignored) {}
            }
            
            player.getInventory().addItem(new ItemStack(material, amount));
            logAction(script, "Gave " + player.getName() + " " + amount + " " + material.name());
        }
    }
    
    private void handleTeleport(Player player, String command, Script script) {
        String[] parts = command.split(" ");
        
        if (parts.length >= 3) {
            try {
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                double z = Double.parseDouble(parts[2]);
                
                World world = parts.length >= 4 ? Bukkit.getWorld(parts[3]) : player.getWorld();
                if (world != null) {
                    Location loc = new Location(world, x, y, z);
                    player.teleport(loc);
                    logAction(script, "Teleported " + player.getName() + " to " + x + ", " + y + ", " + z);
                }
            } catch (NumberFormatException ignored) {}
        } else {
            // Teleport to another player
            Player target = Bukkit.getPlayer(parts[0]);
            if (target != null) {
                player.teleport(target.getLocation());
                logAction(script, "Teleported " + player.getName() + " to " + target.getName());
            }
        }
    }
    
    private void handleMessage(Player player, String message, Script script) {
        player.sendMessage(ColorUtils.toComponent(message));
        logAction(script, "Sent message to " + player.getName() + ": " + ColorUtils.strip(message));
    }
    
    private void handleTitle(Player player, String command, Script script) {
        String[] parts = command.split("\\|");
        String title = parts.length > 0 ? parts[0] : "";
        String subtitle = parts.length > 1 ? parts[1] : "";
        
        player.showTitle(Title.title(
            ColorUtils.toComponent(title),
            ColorUtils.toComponent(subtitle),
            Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(500))
        ));
        logAction(script, "Showed title to " + player.getName());
    }
    
    private void handleActionbar(Player player, String message, Script script) {
        player.sendActionBar(ColorUtils.toComponent(message));
        logAction(script, "Sent actionbar to " + player.getName());
    }
    
    private void handleSound(Player player, String command, Script script) {
        String[] parts = command.split(" ");
        try {
            Sound sound = Sound.valueOf(parts[0].toUpperCase());
            float volume = parts.length > 1 ? Float.parseFloat(parts[1]) : 1.0f;
            float pitch = parts.length > 2 ? Float.parseFloat(parts[2]) : 1.0f;
            
            player.playSound(player.getLocation(), sound, volume, pitch);
            logAction(script, "Played sound " + sound.name() + " for " + player.getName());
        } catch (Exception ignored) {}
    }
    
    private void handleEffect(Player player, String command, Script script) {
        String[] parts = command.split(" ");
        try {
            PotionEffectType effect = PotionEffectType.getByName(parts[0].toUpperCase());
            if (effect != null) {
                int duration = parts.length > 1 ? Integer.parseInt(parts[1]) * 20 : 60 * 20;
                int amplifier = parts.length > 2 ? Integer.parseInt(parts[2]) - 1 : 0;
                
                player.addPotionEffect(new PotionEffect(effect, duration, amplifier));
                logAction(script, "Applied effect " + effect.getName() + " to " + player.getName());
            }
        } catch (Exception ignored) {}
    }
    
    private void handleGamemode(Player player, String gamemode, Script script) {
        try {
            GameMode gm = GameMode.valueOf(gamemode.toUpperCase());
            player.setGameMode(gm);
            logAction(script, "Set gamemode of " + player.getName() + " to " + gm.name());
        } catch (Exception ignored) {}
    }
    
    private void handleConsoleCommand(String command, Script script) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        logAction(script, "Executed console command: " + command);
    }
    
    private void handlePlayerCommand(Player player, String command, Script script) {
        player.performCommand(command);
        logAction(script, "Executed player command for " + player.getName() + ": " + command);
    }
    
    private void handleBroadcast(String message, Script script) {
        Bukkit.broadcast(ColorUtils.toComponent(message));
        logAction(script, "Broadcasted: " + ColorUtils.strip(message));
    }
    
    private String replaceVariables(String text, Player player, Script script) {
        // Replace player variables
        text = text.replace("%player%", player.getName())
                   .replace("%PLAYER%", player.getName())
                   .replace("%uuid%", player.getUniqueId().toString())
                   .replace("%world%", player.getWorld().getName());
        
        // Replace script variables
        for (Map.Entry<String, String> entry : script.getVariables().entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }
        
        // Replace global variables from config
        if (plugin.getConfig().contains("global-variables")) {
            for (String key : plugin.getConfig().getConfigurationSection("global-variables").getKeys(false)) {
                String value = plugin.getConfig().getString("global-variables." + key);
                text = text.replace(key, value);
            }
        }
        
        return text;
    }
    
    private void logAction(Script script, String message) {
        if (script.isConsoleLogging()) {
            plugin.getLogger().info("[" + script.getFileName() + "] " + message);
        }
    }
}