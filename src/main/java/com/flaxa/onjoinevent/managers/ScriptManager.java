package com.flaxa.onjoinevent.managers;

import com.flaxa.onjoinevent.OnJoinEvent;
import com.flaxa.onjoinevent.models.Script;
import com.flaxa.onjoinevent.models.ScriptAction;
import com.flaxa.onjoinevent.utils.ColorUtils;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ScriptManager {
    
    private final OnJoinEvent plugin;
    private final Map<String, Script> scripts;
    private final Map<String, Boolean> firstJoinTracker;
    
    public ScriptManager(OnJoinEvent plugin) {
        this.plugin = plugin;
        this.scripts = new ConcurrentHashMap<>();
        this.firstJoinTracker = new ConcurrentHashMap<>();
    }
    
    /**
     * Load all script files from the Configs folder
     */
    public void loadAllScripts() {
        scripts.clear();
        File configsFolder = plugin.getScriptsFolder();
        
        File[] files = configsFolder.listFiles((dir, name) -> name.endsWith(".oje"));
        if (files == null || files.length == 0) {
            plugin.getLogger().info("No script files found.");
            return;
        }
        
        for (File file : files) {
            try {
                loadScript(file);
            } catch (Exception e) {
                plugin.logError("Failed to load script: " + file.getName(), e);
            }
        }
        
        plugin.getLogger().info("Loaded " + scripts.size() + " script(s).");
    }
    
    /**
     * Load a specific script file
     */
    public boolean loadScript(File file) throws IOException {
        String fileName = file.getName().replace(".oje", "");
        
        List<String> lines = Files.readAllLines(file.toPath());
        Script script = parseScript(fileName, lines);
        
        if (script != null) {
            scripts.put(fileName, script);
            plugin.log("Loaded script: " + fileName);
            return true;
        }
        
        return false;
    }
    
    /**
     * Parse script content into a Script object
     */
    private Script parseScript(String fileName, List<String> lines) {
        Script script = null;
        Script.ScriptType currentType = null;
        boolean inEventBlock = false;
        double cumulativeDelay = 0;
        
        for (String line : lines) {
            // Remove comments
            if (line.contains("#")) {
                line = line.substring(0, line.indexOf("#"));
            }
            
            line = line.trim();
            
            if (line.isEmpty()) continue;
            
            // Parse console logging setting
            if (line.toLowerCase().startsWith("console-logging=")) {
                String value = line.substring(line.indexOf("=") + 1).trim();
                if (script != null) {
                    script.setConsoleLogging(value.equalsIgnoreCase("true"));
                }
                continue;
            }
            
            // Parse variables
            if (line.contains("=") && !line.toLowerCase().startsWith("on")) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    
                    if (script != null && !key.equalsIgnoreCase("console-logging")) {
                        script.addVariable(key, value);
                    }
                }
                continue;
            }
            
            // Parse event type
            if (line.toLowerCase().startsWith("onplayerjoin")) {
                currentType = Script.ScriptType.ON_PLAYER_JOIN;
                script = new Script(fileName, currentType);
                inEventBlock = true;
                cumulativeDelay = 0;
                continue;
            } else if (line.toLowerCase().startsWith("onplayerfirstjoin")) {
                currentType = Script.ScriptType.ON_PLAYER_FIRST_JOIN;
                script = new Script(fileName, currentType);
                inEventBlock = true;
                cumulativeDelay = 0;
                continue;
            } else if (line.toLowerCase().matches("on[a-z0-9_]+joins")) {
                // onPlayernameJoins
                String playerName = line.substring(2, line.length() - 5); // Remove "on" and "joins"
                currentType = Script.ScriptType.ON_PLAYERNAME_JOINS;
                script = new Script(fileName, currentType);
                script.setTargetPlayer(playerName);
                inEventBlock = true;
                cumulativeDelay = 0;
                continue;
            } else if (line.toLowerCase().matches("onfirst[a-z0-9_]+joins")) {
                // onFirstPlayernameJoins
                String playerName = line.substring(7, line.length() - 5); // Remove "onFirst" and "joins"
                currentType = Script.ScriptType.ON_FIRST_PLAYERNAME_JOINS;
                script = new Script(fileName, currentType);
                script.setTargetPlayer(playerName);
                inEventBlock = true;
                cumulativeDelay = 0;
                continue;
            }
            
            // Parse actions
            if (inEventBlock && script != null) {
                ScriptAction action = parseAction(line, cumulativeDelay);
                if (action != null) {
                    script.addAction(action);
                    
                    // Update cumulative delay
                    if (action.getType() == ScriptAction.ActionType.WAIT) {
                        cumulativeDelay += action.getDelay();
                    }
                }
            }
        }
        
        return script;
    }
    
    /**
     * Parse a single action line
     */
    private ScriptAction parseAction(String line, double delay) {
        String lowerLine = line.toLowerCase();
        
        if (lowerLine.startsWith("wait ")) {
            try {
                double waitTime = Double.parseDouble(line.substring(5).trim());
                return new ScriptAction(ScriptAction.ActionType.WAIT, "", waitTime);
            } catch (NumberFormatException e) {
                plugin.getLogger().warning("Invalid wait time: " + line);
                return null;
            }
        } else if (lowerLine.startsWith("give ")) {
            return new ScriptAction(ScriptAction.ActionType.GIVE, line.substring(5).trim(), delay);
        } else if (lowerLine.startsWith("tp ") || lowerLine.startsWith("teleport ")) {
            int start = lowerLine.startsWith("tp ") ? 3 : 9;
            return new ScriptAction(ScriptAction.ActionType.TELEPORT, line.substring(start).trim(), delay);
        } else if (lowerLine.startsWith("send message ") || lowerLine.startsWith("message ")) {
            int start = lowerLine.startsWith("send message ") ? 13 : 8;
            return new ScriptAction(ScriptAction.ActionType.MESSAGE, line.substring(start).trim(), delay);
        } else if (lowerLine.startsWith("title ")) {
            return new ScriptAction(ScriptAction.ActionType.TITLE, line.substring(6).trim(), delay);
        } else if (lowerLine.startsWith("actionbar ")) {
            return new ScriptAction(ScriptAction.ActionType.ACTIONBAR, line.substring(10).trim(), delay);
        } else if (lowerLine.startsWith("sound ")) {
            return new ScriptAction(ScriptAction.ActionType.SOUND, line.substring(6).trim(), delay);
        } else if (lowerLine.startsWith("effect ")) {
            return new ScriptAction(ScriptAction.ActionType.EFFECT, line.substring(7).trim(), delay);
        } else if (lowerLine.startsWith("gamemode ")) {
            return new ScriptAction(ScriptAction.ActionType.GAMEMODE, line.substring(9).trim(), delay);
        } else if (lowerLine.startsWith("console ")) {
            return new ScriptAction(ScriptAction.ActionType.CONSOLE_COMMAND, line.substring(8).trim(), delay);
        } else if (lowerLine.startsWith("player command ")) {
            return new ScriptAction(ScriptAction.ActionType.PLAYER_COMMAND, line.substring(15).trim(), delay);
        } else if (lowerLine.startsWith("broadcast ")) {
            return new ScriptAction(ScriptAction.ActionType.BROADCAST, line.substring(10).trim(), delay);
        }
        
        return null;
    }
    
    public Map<String, Script> getLoadedScripts() {
        return new HashMap<>(scripts);
    }
    
    public void clearScripts() {
        scripts.clear();
    }
    
    public boolean isFirstJoin(Player player) {
        return !player.hasPlayedBefore();
    }
    
    public void createExampleFiles() {
        createExampleFile("player-join.example", getPlayerJoinExample());
        createExampleFile("first-join.example", getFirstJoinExample());
        createExampleFile("specific-player.example", getSpecificPlayerExample());
        createExampleFile("first-specific-player.example", getFirstSpecificPlayerExample());
    }
    
    private void createExampleFile(String fileName, String content) {
        File file = new File(plugin.getScriptsFolder(), fileName);
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
                plugin.getLogger().info("Created example file: " + fileName);
            } catch (IOException e) {
                plugin.logError("Failed to create example file: " + fileName, e);
            }
        }
    }
    
    // Example file contents in separate methods (continued in next file)
}