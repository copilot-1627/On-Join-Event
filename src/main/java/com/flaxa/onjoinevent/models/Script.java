package com.flaxa.onjoinevent.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Script {
    
    private final String fileName;
    private final ScriptType type;
    private boolean consoleLogging;
    private Map<String, String> variables;
    private List<ScriptAction> actions;
    private String targetPlayer; // For specific player join events
    
    public Script(String fileName, ScriptType type) {
        this.fileName = fileName;
        this.type = type;
        this.consoleLogging = false;
        this.variables = new HashMap<>();
        this.actions = new ArrayList<>();
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public ScriptType getType() {
        return type;
    }
    
    public boolean isConsoleLogging() {
        return consoleLogging;
    }
    
    public void setConsoleLogging(boolean consoleLogging) {
        this.consoleLogging = consoleLogging;
    }
    
    public Map<String, String> getVariables() {
        return variables;
    }
    
    public void addVariable(String key, String value) {
        this.variables.put(key, value);
    }
    
    public String getVariable(String key) {
        return variables.get(key);
    }
    
    public List<ScriptAction> getActions() {
        return actions;
    }
    
    public void addAction(ScriptAction action) {
        this.actions.add(action);
    }
    
    public String getTargetPlayer() {
        return targetPlayer;
    }
    
    public void setTargetPlayer(String targetPlayer) {
        this.targetPlayer = targetPlayer;
    }
    
    public enum ScriptType {
        ON_PLAYER_JOIN,
        ON_PLAYER_FIRST_JOIN,
        ON_PLAYERNAME_JOINS,
        ON_FIRST_PLAYERNAME_JOINS
    }
}