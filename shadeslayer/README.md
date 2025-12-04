# Shadeslayer

A text-based RPG built with JavaFX where you play as Eragon, trapped in Durza's fortress and fighting to escape.

## Overview

This is a dungeon crawler game featuring turn-based combat, spell casting, dialogue choices, and a time-rewind mechanic. Navigate through rooms, collect items, learn spells, and defeat enemies to escape the fortress.

## Features

- **Turn-based combat** with light and heavy attacks
- **Combat spells** (Glimmer Ward, Stone Shove, Shadow Lash)
- **Dialogue system** with NPC conversations and branching choices
- **Time rewind mechanic** (ChronoSpell) - return to your last save point
- **Save system** with 3 save slots
- **Medieval-themed UI** with custom CSS styling
- **Command palette** with autocomplete
- **Health and energy management**

## Technology Stack

- Java 17
- JavaFX 21.0.1
- Maven for build management
- MVC architecture
- Custom annotations for command metadata
- Generic containers with bounded type parameters
- Serialization for save/load functionality

## Building from Source

Requires Maven and Java 17+:

```bash
mvn clean package
```

This generates a standalone JAR at `target/shadeslayer-1.0-SNAPSHOT.jar` with all dependencies bundled.

## Running the Game

```bash
java -jar target/shadeslayer-1.0-SNAPSHOT.jar
```

Select a save slot (1-3) when prompted, then use text commands to play.

## Commands

- `go [direction]` - Move (north, south, east, west, up, down)
- `take [item]` - Pick up an item
- `drop [item]` - Drop an item from inventory
- `use [item/spell]` - Use an item or cast a spell
- `examine [target]` - Look at something more closely
- `talk [npc]` - Talk to an NPC
- `attack [light/heavy]` - Attack with your weapon
- `inventory` - Check your items and spells
- `status` - View health, energy, and equipped weapon
- `help` - List all commands

## Game Progression

1. Start in Watchtower Cell
2. Explore and collect the Iron Shortblade
3. Learn Glimmer Ward spell
4. Use Stone Shove to clear rubble
5. Talk to the Wounded Prisoner for information
6. Defeat the Shade Acolyte
7. Find the Bone Key
8. Unlock the gate and defeat Durza

## Project Structure

```
src/main/java/com/shadeslayer/
├── controller/     - Game logic and command handling
├── model/          - Game entities (Player, Room, Item, Spell, etc.)
├── view/           - JavaFX UI components
└── Main.java       - Entry point

src/main/resources/
├── icons/          - Item and spell images
├── medieval-theme.css - UI styling
└── Layout.fxml     - Main UI layout
```

## Design Patterns Used

- **MVC** (Model-View-Controller)
- **Generic containers** (`Container<T extends Usable>`)
- **Custom annotations** (`@GameCommand`, `@GameConfig`)
- **Factory pattern** (DialogueFactory)
- **Trie data structure** for command autocomplete
- **Observer-like pattern** for UI updates

## Demo

See the video walkthrough [here](https://github.com/dillon-lynch/shadeslayer/blob/main/GameOverviewVideo.mkv)

## Author

Dillon Lynch

## License

Educational project - see course requirements
