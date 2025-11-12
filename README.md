# OnJoinEvent - Advanced Minecraft Join Event Plugin

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21+-green.svg)
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

A powerful and flexible Minecraft plugin for handling player join events with custom scripting support. Create personalized welcome experiences, starter kits, VIP greetings, and much more!

## ✨ Features

- 🎯 **Four Join Event Types**: 
  - `onPlayerJoin` - Every player join
  - `onPlayerFirstJoin` - New players only
  - `on<PlayerName>Joins` - Specific player joins
  - `onFirst<PlayerName>Joins` - Specific player's first join

- 📝 **Custom Scripting System**: Easy-to-write `.oje` script files with support for:
  - Custom variables
  - Timed delays
  - Console logging
  - Comments

- 🎮 **Rich Action Support**:
  - Messages, titles, and actionbars
  - Item giving
  - Teleportation
  - Sound effects
  - Potion effects
  - Gamemode changes
  - Console/Player commands
  - Broadcasts

- ⚡ **Performance Optimized**:
  - Async script execution
  - Minimal resource usage
  - No lag guaranteed

- 🛠️ **Admin Commands**:
  - Create scripts in-game
  - Reload scripts without restart
  - List all loaded scripts

## 📋 Requirements

- Minecraft Server 1.21+
- Java 21 or higher
- Paper/Spigot/Purpur server software

## 🚀 Installation

1. Download the latest `OnJoinEvent.jar` from [Releases](https://github.com/copilot-1627/On-Join-Event/releases)
2. Place the JAR file in your server's `plugins` folder
3. Restart or reload your server
4. Configure scripts in `plugins/OnJoinEvent/Configs/`

## 📖 Usage

### Commands

All commands require the `onjoinevent.admin` permission (default: OP)

```
/oje create <filename>     - Create a new script file
/oje reload [scriptname]   - Reload all scripts or specific script
/oje list                  - List all loaded scripts
/oje help                  - Show help menu
```

Aliases: `/onjoinevent`, `/ojevent`

### Script Syntax

Scripts are stored in `plugins/OnJoinEvent/Configs/` with the `.oje` extension.

#### Basic Structure

```oje
# Script settings
console-logging=true

# Custom variables
server-name=Flaxa MC
welcome-message=Welcome!

# Event type (choose one)
onPlayerJoin

# Actions
send message &eHi %player%!
wait 0.5
title &6Welcome!|&eEnjoy your stay
sound ENTITY_PLAYER_LEVELUP 1.0 1.0
give %player% DIAMOND 1
```

#### Event Types

```oje
# All players
onPlayerJoin

# First-time players only
onPlayerFirstJoin

# Specific player (replace Magic_Plays with actual name)
onMagic_PlaysJoins

# Specific player's first join
onFirstMagic_PlaysJoins
```

#### Available Actions

| Action | Syntax | Example |
|--------|--------|--------|
| Message | `send message <text>` | `send message &eWelcome %player%!` |
| Title | `title <title>\|<subtitle>` | `title &6Hello\|&eWorld` |
| Actionbar | `actionbar <text>` | `actionbar &aWelcome to the server` |
| Give Item | `give %player% <item> [amount]` | `give %player% DIAMOND 5` |
| Teleport | `tp %player% <x> <y> <z> [world]` | `tp %player% 0 100 0 world` |
| Sound | `sound <sound> [volume] [pitch]` | `sound ENTITY_PLAYER_LEVELUP 1.0 1.0` |
| Effect | `effect <type> [duration] [level]` | `effect SPEED 30 2` |
| Gamemode | `gamemode <mode>` | `gamemode SURVIVAL` |
| Console Cmd | `console <command>` | `console give %player% diamond 1` |
| Player Cmd | `player command <command>` | `player command spawn` |
| Broadcast | `broadcast <message>` | `broadcast &e%player% joined!` |
| Wait | `wait <seconds>` | `wait 1.5` |

#### Variables

**Built-in Variables:**
- `%player%` - Player name
- `%uuid%` - Player UUID
- `%world%` - Current world name

**Custom Variables:**
```oje
server-name=Flaxa MC
welcome-text=Welcome to server-name!

onPlayerJoin
send message welcome-text
```

**Global Variables** (from config.yml):
```yaml
global-variables:
  server-name: 'Flaxa MC'
  discord: 'discord.gg/flaxa'
```

## 📂 File Structure

```
plugins/
└── OnJoinEvent/
    ├── config.yml
    └── Configs/
        ├── player-join.example
        ├── first-join.example
        ├── specific-player.example
        ├── first-specific-player.example
        └── custom-script.oje
```

## 🎯 Examples

### Welcome Message Script

```oje
console-logging=false
server-name=Flaxa MC

onPlayerJoin
send message &8&m------------------------
send message &e&lWelcome to server-name
send message &7Hi &e%player%&7!
send message &8&m------------------------
sound ENTITY_PLAYER_LEVELUP 1.0 1.0
```

### Starter Kit Script

```oje
console-logging=true

onPlayerFirstJoin
broadcast &e&l[NEW] &7Welcome %player% to the server!
send message &aYou received a starter kit!
wait 1.0
give %player% WOODEN_SWORD 1
give %player% WOODEN_PICKAXE 1
give %player% BREAD 16
tp %player% 0 100 0 world
gamemode SURVIVAL
```

### VIP Player Script

```oje
console-logging=true
player-title=&6&lVIP

onMagic_PlaysJoins
broadcast player-title &e%player% &7has joined!
send message &6&lWelcome back, %player%!
give %player% DIAMOND 5
effect SPEED 300 1
sound ENTITY_ENDER_DRAGON_GROWL 0.5 2.0
```

## ⚙️ Configuration

### config.yml

```yaml
settings:
  debug: false
  prefix: '&8[&6OJE&8]&r'
  create-examples: true
  default-console-logging: false
  
  performance:
    max-execution-time: 5000
    async-execution: true

messages:
  reload-success: '&aSuccessfully reloaded all scripts!'
  script-created: '&aCreated new script file: &e{filename}'
  # ... more messages

global-variables:
  server-name: 'Flaxa MC'
  discord: 'discord.gg/flaxa'
  website: 'flaxa.tech'
```

## 🔧 Building from Source

```bash
git clone https://github.com/copilot-1627/On-Join-Event.git
cd On-Join-Event
mvn clean package
```

The compiled JAR will be in `target/OnJoinEvent-1.0.0.jar`

## 📝 Permissions

| Permission | Description | Default |
|------------|-------------|--------|
| `onjoinevent.admin` | Access to all commands | OP |

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 💬 Support

For support, bug reports, or feature requests:
- Open an issue on [GitHub](https://github.com/copilot-1627/On-Join-Event/issues)
- Join our Discord: discord.gg/flaxa

## 🙏 Credits

Developed by [Flaxa](https://github.com/copilot-1627)

---

⭐ If you like this plugin, please star the repository!