package com.flaxa.onjoinevent.utils;

import com.flaxa.onjoinevent.OnJoinEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtils {
    
    private static final OnJoinEvent plugin = OnJoinEvent.getInstance();
    
    /**
     * Sends a message to a player or console with plugin prefix
     */
    public static void send(CommandSender sender, String message) {
        String prefix = plugin.getConfig().getString("settings.prefix", "&8[&6OJE&8]&r");
        String fullMessage = prefix + " " + message;
        
        if (sender instanceof Player) {
            sender.sendMessage(ColorUtils.toComponent(fullMessage));
        } else {
            sender.sendMessage(ColorUtils.translate(fullMessage));
        }
    }
    
    /**
     * Sends a raw message without prefix
     */
    public static void sendRaw(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage(ColorUtils.toComponent(message));
        } else {
            sender.sendMessage(ColorUtils.translate(message));
        }
    }
    
    /**
     * Gets a message from config
     */
    public static String getMessage(String path) {
        return plugin.getConfig().getString("messages." + path, "&cMessage not found: " + path);
    }
    
    /**
     * Replaces placeholders in a message
     */
    public static String replacePlaceholders(String message, String... replacements) {
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(replacements[i], replacements[i + 1]);
            }
        }
        return message;
    }
}