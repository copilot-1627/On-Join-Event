package com.flaxa.onjoinevent;

import com.flaxa.onjoinevent.commands.OJECommand;
import com.flaxa.onjoinevent.listeners.PlayerJoinListener;
import com.flaxa.onjoinevent.managers.ScriptManager;
import com.flaxa.onjoinevent.utils.ColorUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class OnJoinEvent extends JavaPlugin {
    
    private static OnJoinEvent instance;
    private ScriptManager scriptManager;
    private File scriptsFolder;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Save default config
        saveDefaultConfig();
        
        // Create plugin folders
        createPluginFolders();
        
        // Initialize managers
        scriptManager = new ScriptManager(this);
        
        // Register commands
        getCommand("oje").setExecutor(new OJECommand(this));
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        
        // Load all scripts
        scriptManager.loadAllScripts();
        
        // Create example files if enabled
        if (getConfig().getBoolean("settings.create-examples", true)) {
            scriptManager.createExampleFiles();
        }
        
        getLogger().info(ColorUtils.strip("&a" + getDescription().getName() + " v" + getDescription().getVersion() + " has been enabled!"));
        getLogger().info(ColorUtils.strip("&aLoaded " + scriptManager.getLoadedScripts().size() + " scripts."));
    }
    
    @Override
    public void onDisable() {
        if (scriptManager != null) {
            scriptManager.clearScripts();
        }
        getLogger().info(ColorUtils.strip("&c" + getDescription().getName() + " has been disabled!"));
    }
    
    private void createPluginFolders() {
        // Create main plugin folder
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        
        // Create configs folder
        scriptsFolder = new File(getDataFolder(), "Configs");
        if (!scriptsFolder.exists()) {
            scriptsFolder.mkdirs();
            getLogger().info("Created Configs folder.");
        }
    }
    
    public void reloadPlugin() {
        reloadConfig();
        scriptManager.loadAllScripts();
    }
    
    public static OnJoinEvent getInstance() {
        return instance;
    }
    
    public ScriptManager getScriptManager() {
        return scriptManager;
    }
    
    public File getScriptsFolder() {
        return scriptsFolder;
    }
    
    public void log(String message) {
        if (getConfig().getBoolean("settings.debug", false)) {
            getLogger().info(ColorUtils.strip(message));
        }
    }
    
    public void logError(String message, Exception e) {
        getLogger().log(Level.SEVERE, ColorUtils.strip(message), e);
    }
}