package com.flaxa.onjoinevent.models;

public class ScriptAction {
    
    private final ActionType type;
    private final String command;
    private final double delay; // in seconds
    
    public ScriptAction(ActionType type, String command) {
        this(type, command, 0);
    }
    
    public ScriptAction(ActionType type, String command, double delay) {
        this.type = type;
        this.command = command;
        this.delay = delay;
    }
    
    public ActionType getType() {
        return type;
    }
    
    public String getCommand() {
        return command;
    }
    
    public double getDelay() {
        return delay;
    }
    
    public long getDelayTicks() {
        return (long) (delay * 20); // Convert seconds to ticks
    }
    
    public enum ActionType {
        GIVE,
        TELEPORT,
        MESSAGE,
        TITLE,
        ACTIONBAR,
        SOUND,
        EFFECT,
        GAMEMODE,
        CONSOLE_COMMAND,
        PLAYER_COMMAND,
        BROADCAST,
        WAIT
    }
}