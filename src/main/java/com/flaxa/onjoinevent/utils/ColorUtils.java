package com.flaxa.onjoinevent.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

public class ColorUtils {
    
    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.legacyAmpersand();
    
    /**
     * Translates color codes using & symbol
     */
    public static String translate(String message) {
        if (message == null) return "";
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    /**
     * Strips color codes from message
     */
    public static String strip(String message) {
        if (message == null) return "";
        return ChatColor.stripColor(translate(message));
    }
    
    /**
     * Converts legacy string to Adventure Component
     */
    public static Component toComponent(String message) {
        if (message == null) return Component.empty();
        return SERIALIZER.deserialize(message);
    }
    
    /**
     * Replaces variables in a message
     */
    public static String replaceVariables(String message, String playerName) {
        if (message == null) return "";
        return message.replace("%player%", playerName)
                     .replace("%PLAYER%", playerName);
    }
}