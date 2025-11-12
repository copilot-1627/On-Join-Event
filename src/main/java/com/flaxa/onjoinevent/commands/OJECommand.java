package com.flaxa.onjoinevent.commands;

import com.flaxa.onjoinevent.OnJoinEvent;
import com.flaxa.onjoinevent.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OJECommand implements CommandExecutor, TabCompleter {
    
    private final OnJoinEvent plugin;
    
    public OJECommand(OnJoinEvent plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("onjoinevent.admin")) {
            MessageUtils.send(sender, MessageUtils.getMessage("no-permission"));
            return true;
        }
        
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "create":
                if (args.length < 2) {
                    MessageUtils.send(sender, "&cUsage: /oje create <filename>");
                    return true;
                }
                createScript(sender, args[1]);
                break;
                
            case "reload":
                if (args.length == 1) {
                    reloadAll(sender);
                } else {
                    reloadScript(sender, args[1]);
                }
                break;
                
            case "list":
                listScripts(sender);
                break;
                
            case "help":
                sendHelp(sender);
                break;
                
            default:
                MessageUtils.send(sender, MessageUtils.getMessage("invalid-usage"));
                break;
        }
        
        return true;
    }
    
    private void createScript(CommandSender sender, String fileName) {
        if (!fileName.endsWith(".oje")) {
            fileName += ".oje";
        }
        
        File scriptFile = new File(plugin.getScriptsFolder(), fileName);
        
        if (scriptFile.exists()) {
            MessageUtils.send(sender, "&cScript file already exists: &e" + fileName);
            return;
        }
        
        try (FileWriter writer = new FileWriter(scriptFile)) {
            writer.write(getTemplateContent());
            MessageUtils.send(sender, MessageUtils.replacePlaceholders(
                MessageUtils.getMessage("script-created"),
                "{filename}", fileName
            ));
        } catch (IOException e) {
            MessageUtils.send(sender, "&cFailed to create script file. Check console for errors.");
            plugin.logError("Failed to create script: " + fileName, e);
        }
    }
    
    private void reloadAll(CommandSender sender) {
        plugin.reloadPlugin();
        MessageUtils.send(sender, MessageUtils.getMessage("reload-success"));
    }
    
    private void reloadScript(CommandSender sender, String scriptName) {
        if (!scriptName.endsWith(".oje")) {
            scriptName += ".oje";
        }
        
        File scriptFile = new File(plugin.getScriptsFolder(), scriptName);
        
        if (!scriptFile.exists()) {
            MessageUtils.send(sender, MessageUtils.replacePlaceholders(
                MessageUtils.getMessage("script-not-found"),
                "{script}", scriptName
            ));
            return;
        }
        
        try {
            plugin.getScriptManager().loadScript(scriptFile);
            MessageUtils.send(sender, MessageUtils.replacePlaceholders(
                MessageUtils.getMessage("reload-script-success"),
                "{script}", scriptName
            ));
        } catch (IOException e) {
            MessageUtils.send(sender, "&cFailed to reload script. Check console for errors.");
            plugin.logError("Failed to reload script: " + scriptName, e);
        }
    }
    
    private void listScripts(CommandSender sender) {
        MessageUtils.send(sender, "&6&lLoaded Scripts:");
        
        if (plugin.getScriptManager().getLoadedScripts().isEmpty()) {
            MessageUtils.send(sender, "&7No scripts loaded.");
            return;
        }
        
        plugin.getScriptManager().getLoadedScripts().forEach((name, script) -> {
            MessageUtils.send(sender, "&e" + name + " &7(" + script.getType().name() + ")");
        });
    }
    
    private void sendHelp(CommandSender sender) {
        MessageUtils.send(sender, "&6&lOnJoinEvent Commands:");
        MessageUtils.send(sender, "&e/oje create <filename> &7- Create a new script");
        MessageUtils.send(sender, "&e/oje reload [script] &7- Reload all or specific script");
        MessageUtils.send(sender, "&e/oje list &7- List all loaded scripts");
        MessageUtils.send(sender, "&e/oje help &7- Show this help menu");
    }
    
    private String getTemplateContent() {
        return "# OnJoinEvent Script Template\n" +
               "# Created with /oje create command\n" +
               "# \n" +
               "# Available event types:\n" +
               "# - onPlayerJoin (all player joins)\n" +
               "# - onPlayerFirstJoin (new players only)\n" +
               "# - on<PlayerName>Joins (specific player, e.g., onMagic_PlaysJoins)\n" +
               "# - onFirst<PlayerName>Joins (specific player first join)\n" +
               "#\n" +
               "# Script Settings\n" +
               "console-logging=false\n" +
               "\n" +
               "# Variables (use these in your actions)\n" +
               "server-name=Flaxa MC\n" +
               "welcome-message=Welcome to server-name!\n" +
               "\n" +
               "# Event Handler - Choose ONE event type below:\n" +
               "onPlayerJoin\n" +
               "# Actions to execute:\n" +
               "send message &eHi &e&l%player%!\n" +
               "send message &7welcome-message\n" +
               "wait 0.5\n" +
               "title &6Welcome!|&eEnjoy your stay\n" +
               "sound ENTITY_PLAYER_LEVELUP 1.0 1.0\n" +
               "# give %player% DIAMOND 1\n" +
               "# tp %player% 0 100 0 world\n" +
               "# gamemode SURVIVAL\n";
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("onjoinevent.admin")) {
            return new ArrayList<>();
        }
        
        if (args.length == 1) {
            return Arrays.asList("create", "reload", "list", "help")
                .stream()
                .filter(s -> s.startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
            File[] files = plugin.getScriptsFolder().listFiles((dir, name) -> name.endsWith(".oje"));
            if (files != null) {
                return Arrays.stream(files)
                    .map(File::getName)
                    .filter(s -> s.startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
            }
        }
        
        return new ArrayList<>();
    }
}