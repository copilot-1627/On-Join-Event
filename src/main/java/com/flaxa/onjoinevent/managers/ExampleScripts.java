package com.flaxa.onjoinevent.managers;

public class ExampleScripts {
    
    public static String getPlayerJoinExample() {
        return "# Example: Regular Player Join Event\n" +
               "# This script runs every time ANY player joins the server\n" +
               "# Perfect for: Welcome messages, daily rewards, server info\n" +
               "\n" +
               "# Enable console logging to see what's happening\n" +
               "console-logging=true\n" +
               "\n" +
               "# Define your custom variables\n" +
               "server-name=Flaxa MC\n" +
               "discord=discord.gg/flaxa\n" +
               "website=flaxa.tech\n" +
               "\n" +
               "# Event trigger - executes when any player joins\n" +
               "onPlayerJoin\n" +
               "\n" +
               "# Welcome the player\n" +
               "send message &8&m------------------------\n" +
               "send message &e&lWelcome to server-name\n" +
               "send message &7Hi &e%player%&7, enjoy your stay!\n" +
               "send message &7Discord: &bdiscord\n" +
               "send message &7Website: &bwebsite\n" +
               "send message &8&m------------------------\n" +
               "\n" +
               "# Play a welcome sound\n" +
               "sound ENTITY_PLAYER_LEVELUP 1.0 1.0\n" +
               "\n" +
               "# Wait half a second\n" +
               "wait 0.5\n" +
               "\n" +
               "# Show a title\n" +
               "title &6&lWelcome Back!|&eHave fun playing\n" +
               "\n" +
               "# Give a daily login reward (optional)\n" +
               "# give %player% GOLDEN_APPLE 1\n" +
               "\n" +
               "# Teleport to spawn (optional)\n" +
               "# tp %player% 0 100 0 world\n" +
               "\n" +
               "# Send actionbar message\n" +
               "wait 1.0\n" +
               "actionbar &aYou are now playing on server-name\n";
    }
    
    public static String getFirstJoinExample() {
        return "# Example: First Join Event\n" +
               "# This script runs ONLY when a NEW player joins for the first time\n" +
               "# Perfect for: Starter kits, tutorials, first-time rewards\n" +
               "\n" +
               "console-logging=true\n" +
               "\n" +
               "# Variables\n" +
               "server-name=Flaxa MC\n" +
               "starter-world=world\n" +
               "\n" +
               "# Event trigger - executes only on first join\n" +
               "onPlayerFirstJoin\n" +
               "\n" +
               "# Announce to everyone that a new player joined\n" +
               "broadcast &e&l[NEW] &7Welcome &e%player% &7to the server!\n" +
               "\n" +
               "# Welcome the new player\n" +
               "send message &8&m============================\n" +
               "send message &6&lWelcome to server-name!\n" +
               "send message &7This is your &efirst time &7here!\n" +
               "send message &7\n" +
               "send message &aYou have received a starter kit!\n" +
               "send message &8&m============================\n" +
               "\n" +
               "# Play celebration sound\n" +
               "sound UI_TOAST_CHALLENGE_COMPLETE 1.0 1.0\n" +
               "\n" +
               "# Show welcome title\n" +
               "title &6&lWELCOME!|&eYour adventure begins now\n" +
               "\n" +
               "# Give starter items\n" +
               "wait 1.0\n" +
               "give %player% WOODEN_SWORD 1\n" +
               "give %player% WOODEN_PICKAXE 1\n" +
               "give %player% WOODEN_AXE 1\n" +
               "give %player% BREAD 16\n" +
               "give %player% OAK_LOG 32\n" +
               "\n" +
               "send message &aStarter kit items added to your inventory!\n" +
               "\n" +
               "# Teleport to spawn\n" +
               "wait 0.5\n" +
               "tp %player% 0 100 0 starter-world\n" +
               "\n" +
               "# Set gamemode\n" +
               "gamemode SURVIVAL\n" +
               "\n" +
               "# Give starting effects\n" +
               "wait 1.0\n" +
               "effect REGENERATION 30 1\n" +
               "effect RESISTANCE 30 0\n" +
               "\n" +
               "# Final message\n" +
               "wait 2.0\n" +
               "send message &e&lTip: &7Type /help to see available commands!\n";
    }
    
    public static String getSpecificPlayerExample() {
        return "# Example: Specific Player Join Event\n" +
               "# This script runs when a SPECIFIC player joins\n" +
               "# Replace 'Magic_Plays' with the actual player name\n" +
               "# Perfect for: VIP treatment, special welcomes, admin alerts\n" +
               "\n" +
               "console-logging=true\n" +
               "\n" +
               "# Variables\n" +
               "server-name=Flaxa MC\n" +
               "player-title=&6&lVIP\n" +
               "\n" +
               "# Event trigger - Replace 'Magic_Plays' with target player name\n" +
               "onMagic_PlaysJoins\n" +
               "\n" +
               "# Announce the special player joined\n" +
               "broadcast &8&m========================\n" +
               "broadcast player-title &e%player% &7has joined!\n" +
               "broadcast &8&m========================\n" +
               "\n" +
               "# Personal welcome\n" +
               "send message &6&lWelcome back, %player%!\n" +
               "send message &7Your VIP status is active.\n" +
               "\n" +
               "# Special effects\n" +
               "sound ENTITY_ENDER_DRAGON_GROWL 0.5 2.0\n" +
               "title &6&lVIP JOINED|&e%player% is here!\n" +
               "\n" +
               "# Give VIP perks\n" +
               "wait 1.0\n" +
               "give %player% DIAMOND 5\n" +
               "give %player% GOLDEN_APPLE 3\n" +
               "\n" +
               "# VIP effects\n" +
               "effect SPEED 300 1\n" +
               "effect NIGHT_VISION 300 0\n" +
               "\n" +
               "# Teleport to VIP area (optional)\n" +
               "# tp %player% 100 64 100 world\n" +
               "\n" +
               "# Send actionbar\n" +
               "actionbar &6&lVIP Status Active - Enjoy your perks!\n";
    }
    
    public static String getFirstSpecificPlayerExample() {
        return "# Example: First-Time Specific Player Join\n" +
               "# This runs ONLY the FIRST TIME a specific player joins\n" +
               "# Replace 'Magic_Plays' with the actual player name\n" +
               "# Perfect for: One-time rewards, special welcomes, streamer first join\n" +
               "\n" +
               "console-logging=true\n" +
               "\n" +
               "# Variables\n" +
               "server-name=Flaxa MC\n" +
               "special-player-title=&d&lSTREAMER\n" +
               "\n" +
               "# Event trigger - First time Magic_Plays joins\n" +
               "onFirstMagic_PlaysJoins\n" +
               "\n" +
               "# Major server announcement\n" +
               "broadcast &8&m================================\n" +
               "broadcast &d&l*** SPECIAL GUEST ***\n" +
               "broadcast special-player-title &e%player% &7has joined for the first time!\n" +
               "broadcast &7Let's give them a warm welcome!\n" +
               "broadcast &8&m================================\n" +
               "\n" +
               "# Personal greeting\n" +
               "send message &8&m================================\n" +
               "send message &d&lWelcome to server-name!\n" +
               "send message &7Thank you for joining us, &e%player%&7!\n" +
               "send message &7\n" +
               "send message &aYou've received a special welcome package!\n" +
               "send message &8&m================================\n" +
               "\n" +
               "# Epic entrance\n" +
               "sound ENTITY_WITHER_SPAWN 1.0 1.5\n" +
               "title &d&lSPECIAL GUEST|&e%player% has arrived!\n" +
               "\n" +
               "# Wait for dramatic effect\n" +
               "wait 2.0\n" +
               "\n" +
               "# Give exclusive items\n" +
               "give %player% DIAMOND_SWORD 1\n" +
               "give %player% DIAMOND_PICKAXE 1\n" +
               "give %player% DIAMOND 64\n" +
               "give %player% GOLDEN_APPLE 16\n" +
               "give %player% ENCHANTED_GOLDEN_APPLE 5\n" +
               "\n" +
               "send message &aSpecial items added to your inventory!\n" +
               "\n" +
               "# Teleport to VIP spawn\n" +
               "wait 1.0\n" +
               "tp %player% 0 100 0 world\n" +
               "\n" +
               "# Set to creative mode (optional)\n" +
               "# gamemode CREATIVE\n" +
               "\n" +
               "# Grant special effects\n" +
               "effect SPEED 600 2\n" +
               "effect JUMP_BOOST 600 1\n" +
               "effect NIGHT_VISION 600 0\n" +
               "effect REGENERATION 600 1\n" +
               "\n" +
               "# Firework effect sound\n" +
               "wait 1.5\n" +
               "sound ENTITY_FIREWORK_ROCKET_LAUNCH 1.0 1.0\n" +
               "\n" +
               "# Final messages\n" +
               "wait 2.0\n" +
               "send message &d&lThank you for being here!\n" +
               "send message &7Enjoy your time on server-name\n" +
               "actionbar &d&lWelcome Special Guest - Enjoy your exclusive perks!\n";
    }
}