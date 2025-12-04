package com.shadeslayer.controller;

import java.util.List;

import com.shadeslayer.model.DialogueChoice;
import com.shadeslayer.model.Direction;
import com.shadeslayer.model.Dungeon;
import com.shadeslayer.model.Exit;
import com.shadeslayer.model.GameState;
import com.shadeslayer.model.Item;
import com.shadeslayer.model.NPC;
import com.shadeslayer.model.ParsedCommand;
import com.shadeslayer.model.Player;
import com.shadeslayer.model.Room;
import com.shadeslayer.model.SavePoint;
import com.shadeslayer.model.Spell;
import com.shadeslayer.model.items.IronShortblade;
import com.shadeslayer.model.spells.ChronoSpell;
import com.shadeslayer.model.spells.GlimmerWard;
import com.shadeslayer.model.spells.StoneShove;
import com.shadeslayer.view.GameView;

import javafx.application.Platform;

public class GameController {
    private GameState gameState;
    private final CombatHandler combatHandler;
    private final SaveController saveController;
    private int currentSaveSlot;
    private String lastRoomId;

    public GameController() {
        this.combatHandler = new CombatHandler();
        this.saveController = new SaveController();
        this.currentSaveSlot = com.shadeslayer.Main.getSelectedSaveSlot();

        // Clear all existing saves in this slot when starting a new game
        saveController.deleteAllSavesInSlot(currentSaveSlot);

        initializeGame();
    }

    private void initializeGame() {
        Player player = new Player("Eragon");
        player.learnSpell(new ChronoSpell()); // Start with ChronoSpell

        Dungeon dungeon = new Dungeon();
        player.setCurrentRoom(dungeon.getRoom("WATCHTOWER_CELL"));

        this.gameState = new GameState(player, dungeon);
        // Don't set lastRoomId here - let it be set after initial save
    }

    public void startGame(GameView view) {
        new Thread(() -> {
            runGameLoop(view);
        }).start();
    }

    private void runGameLoop(GameView view) {
        // Initial savepoint at game start
        String roomName = gameState.getPlayer().getCurrentRoom().getName();
        saveController.save(currentSaveSlot, gameState, roomName);
        lastRoomId = getCurrentRoomId(); // Update lastRoomId to prevent duplicate save on first command

        gameState.addOutput("=== SHADESLAYER ===");
        gameState.addOutput("You are Eragon, trapped in the depths beneath Durza's fortress.");
        gameState.addOutput("Saphira, your dragon, is chained somewhere below.");
        gameState.addOutput("Your task: escape... or die trying.\n");
        gameState.addOutput(gameState.getPlayer().getCurrentRoom().getFullDescription());
        gameState.addOutput("\nType 'help' for a list of commands.");

        Platform.runLater(() -> {
            view.updateGameState(gameState);
        });

        boolean running = true;
        while (running) {
            String input = view.getNextInput();

            gameState.addOutput("> " + input);

            ParsedCommand parsed = Parser.getParsedCommand(input);

            if (!parsed.isValid()) {
                gameState.addOutput("Error: " + parsed.getError());
                Platform.runLater(() -> {
                    view.updateGameState(gameState);
                });
                continue;
            }

            String result = executeCommand(parsed);
            gameState.addOutput(result);

            if ("quit".equals(parsed.getCommand().getName())) {
                running = false;
            }

            // Check for room change and autosave
            checkForRoomChange();

            // Check for death
            if (gameState.getPlayer().isDead() && !combatHandler.isInCombat()) {
                gameState.addOutput("\n=== YOU HAVE DIED ===");
                gameState.addOutput("Your vision fades to black...");
                gameState.addOutput("Perhaps you wasted too much energy along the way.");
                gameState.addOutput("Try using 'use ChronoSpell' to rewind to your last save.");
                running = false;
            }

            Platform.runLater(() -> {
                view.updateGameState(gameState);
            });
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public ParsedCommand processCommand(String input) {
        ParsedCommand parsed = Parser.getParsedCommand(input);

        if (!parsed.isValid()) {
            return parsed;
        }

        return parsed;
    }

    public Player getPlayer() {
        return gameState.getPlayer();
    }

    public Dungeon getDungeon() {
        return gameState.getDungeon();
    }

    private void checkForRoomChange() {
        String currentRoomName = getCurrentRoomId();
        if (!currentRoomName.equals(lastRoomId)) {
            lastRoomId = currentRoomName;
            String roomName = gameState.getPlayer().getCurrentRoom().getName();
            saveController.save(currentSaveSlot, gameState, roomName);
            gameState.addOutput("\n[Savepoint created]");
        }
    }

    private String getCurrentRoomId() {
        Room currentRoom = gameState.getPlayer().getCurrentRoom();
        for (String key : gameState.getDungeon().getAllRoomIds()) {
            if (gameState.getDungeon().getRoom(key) == currentRoom) {
                return key;
            }
        }
        return "UNKNOWN";
    }

    private String executeCommand(ParsedCommand parsed) {
        String commandName = parsed.getCommand().getName();
        String argument = parsed.getArguments().isEmpty() ? null : String.join(" ", parsed.getArguments());

        switch (commandName) {
            case "go":
                return handleGo(argument);
            case "examine":
                return handleExamine(argument);
            case "take":
                return handleTake(argument);
            case "drop":
                return handleDrop(argument);
            case "inventory":
                return handleInventory();
            case "talk":
                return handleTalk(argument);
            case "attack":
                return handleAttack(argument);
            case "use":
                return handleUse(argument);
            case "help":
                return handleHelp();
            case "status":
                return handleStatus();
            case "quit":
                return "Thanks for playing Shadeslayer!";
            case "demokill":
                return handleDemoKill();
            default:
                return "Unknown command: " + commandName;
        }
    }

    private String handleGo(String direction) {
        Player player = gameState.getPlayer();
        Room currentRoom = player.getCurrentRoom();

        Direction dir = Direction.fromString(direction);
        if (dir == null) {
            return "Invalid direction. Try: north, south, east, west, up, or down.";
        }

        Exit exit = currentRoom.getExit(dir);
        if (exit == null) {
            return "You can't go that way.";
        }

        if (!exit.canPassThrough(player)) {
            return "The path is blocked. " + exit.getDescription();
        }

        player.setCurrentRoom(exit.getTargetRoom());
        return exit.getTargetRoom().getFullDescription();
    }

    private String handleExamine(String target) {
        Player player = gameState.getPlayer();
        Room currentRoom = player.getCurrentRoom();

        if (target == null || target.isEmpty()) {
            return currentRoom.getFullDescription();
        }

        // Look at NPC
        NPC npc = currentRoom.getNPCByName(target);
        if (npc != null) {
            return npc.getDescription();
        }

        // Look at item in room
        Item item = currentRoom.getItemByName(target);
        if (item != null) {
            return item.getDescription();
        }

        // Look at item in inventory
        item = player.getItemByName(target);
        if (item != null) {
            return item.getDescription();
        }

        return "You don't see that here.";
    }

    private String handleTake(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            return "Take what?";
        }

        Player player = gameState.getPlayer();
        Room currentRoom = player.getCurrentRoom();
        Item item = currentRoom.getItemByName(itemName);

        if (item == null) {
            return "There's no " + itemName + " here.";
        }

        currentRoom.removeItem(item);
        player.addItem(item);
        return "You take the " + item.getName() + ".\n\n" + item.getDescription();
    }

    private String handleDrop(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            return "Drop what?";
        }

        Player player = gameState.getPlayer();
        Item item = player.getItemByName(itemName);

        if (item == null) {
            return "You don't have that item.";
        }

        player.removeItem(item);
        player.getCurrentRoom().addItem(item);
        return "You drop the " + item.getName() + ".";
    }

    private String handleInventory() {
        Player player = gameState.getPlayer();
        StringBuilder sb = new StringBuilder();
        sb.append("=== INVENTORY ===\n");

        if (player.getInventory().isEmpty()) {
            sb.append("You're not carrying anything.\n");
        } else {
            sb.append("Items:\n");
            for (Item item : player.getInventory()) {
                sb.append("  - ").append(item.getName());
                if (item.getDurability() < item.getMaxDurability()) {
                    sb.append(String.format(" (%d/%d)", item.getDurability(), item.getMaxDurability()));
                }
                sb.append("\n");
            }
        }

        sb.append("\nSpells:\n");
        if (player.getSpells().isEmpty()) {
            sb.append("  (none)\n");
        } else {
            for (Spell spell : player.getSpells()) {
                sb.append("  - ").append(spell.getName())
                        .append(" (").append(spell.getEnergyCost()).append(" energy)\n");
            }
        }

        return sb.toString().trim();
    }

    private String handleTalk(String argument) {
        if (argument == null || argument.isEmpty()) {
            return "Talk to whom?";
        }

        Player player = gameState.getPlayer();
        Room currentRoom = player.getCurrentRoom();

        // Parse the argument - could be "npc name" or "npc name 1" (for choice
        // selection)
        String[] parts = argument.split("\\s+");
        String choiceNumStr = null;
        String npcName;

        // Check if last part is a number (dialogue choice)
        if (parts.length > 1) {
            String lastPart = parts[parts.length - 1];
            try {
                Integer.parseInt(lastPart);
                // It's a number - this is a choice selection
                choiceNumStr = lastPart;
                // NPC name is everything except the last part
                npcName = argument.substring(0, argument.lastIndexOf(lastPart)).trim();
            } catch (NumberFormatException e) {
                // Not a number - entire argument is NPC name
                npcName = argument;
            }
        } else {
            npcName = argument;
        }

        NPC npc = currentRoom.getNPCByName(npcName);

        if (npc == null) {
            return "There's no one here by that name.";
        }

        if (npc.isHostile() && !combatHandler.isInCombat()) {
            combatHandler.startCombat(npc);
            return npc.getName() + " attacks!\n" + npc.talk(player);
        }

        // If a choice number was provided, select that choice
        if (choiceNumStr != null && npc.hasDialogue()) {
            try {
                int choiceIndex = Integer.parseInt(choiceNumStr) - 1; // Convert to 0-based index
                if (npc.selectDialogueChoice(choiceIndex, player)) {
                    String dialogue = npc.talk(player);

                    // Show available dialogue choices after selection
                    List<DialogueChoice> choices = npc.getDialogueChoices(player);
                    if (!choices.isEmpty()) {
                        dialogue += "\n\n--- Dialogue Choices ---";
                        for (int i = 0; i < choices.size(); i++) {
                            dialogue += "\n" + (i + 1) + ". " + choices.get(i).getText();
                        }
                        dialogue += "\n\nType 'talk <npc> <number>' to select a choice.";
                    }

                    // Special handling for Wounded Prisoner teaching spell
                    if (npc.getName().equals("Wounded Prisoner") && !player.knowsSpell("Glimmer Ward")) {
                        if (npc.getDialogueTree().getCurrentNode().getId().equals("teach_spell")) {
                            player.learnSpell(new GlimmerWard());
                            return dialogue + "\n\n[You learned Glimmer Ward!]";
                        }
                    }

                    return dialogue;
                } else {
                    return "Invalid choice.";
                }
            } catch (NumberFormatException e) {
                return "Invalid choice number.";
            }
        }

        // No choice selected - just show current dialogue
        String dialogue = npc.talk(player);

        // Show available dialogue choices if this is a non-hostile NPC with dialogue
        if (!npc.isHostile() && npc.hasDialogue()) {
            List<DialogueChoice> choices = npc.getDialogueChoices(player);
            if (!choices.isEmpty()) {
                dialogue += "\n\n--- Dialogue Choices ---";
                for (int i = 0; i < choices.size(); i++) {
                    dialogue += "\n" + (i + 1) + ". " + choices.get(i).getText();
                }
                dialogue += "\n\nType 'talk <npc> <number>' to select a choice.";
            }
        }

        // Special handling for Wounded Prisoner teaching spell
        if (npc.getName().equals("Wounded Prisoner") && !player.knowsSpell("Glimmer Ward")) {
            if (npc.getDialogueTree().getCurrentNode().getId().equals("teach_spell")) {
                player.learnSpell(new GlimmerWard());
                return dialogue + "\n\n[You learned Glimmer Ward!]";
            }
        }

        return dialogue;
    }

    private String handleAttack(String argument) {
        Player player = gameState.getPlayer();

        if (!combatHandler.isInCombat()) {
            return "You're not in combat. Use 'talk <npc>' to interact with NPCs.";
        }

        // Find the BEST weapon in inventory (prioritize higher damage weapons)
        Item weapon = null;
        // Check for IronShortblade (only weapon in game)
        for (Item item : player.getInventory()) {
            if (item instanceof IronShortblade) {
                weapon = item;
                break;
            }
        }

        if (weapon == null) {
            return "You don't have a weapon!";
        }

        // Check if this is a heavy attack (argument contains "heavy")
        boolean heavyAttack = argument != null && argument.trim().equalsIgnoreCase("heavy");
        return combatHandler.attackWithWeapon(player, weapon, heavyAttack);
    }

    private String handleChronoSpell() {
        java.util.List<SavePoint> saves = saveController.getSavesForSlot(currentSaveSlot);
        if (saves.isEmpty()) {
            return "There's no saved state to return to.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== AVAILABLE SAVE POINTS ===\n");
        sb.append("Select a save to rewind to:\n\n");

        // Show in chronological order (oldest to newest, same order you entered rooms)
        for (int i = 0; i < saves.size(); i++) {
            SavePoint save = saves.get(i);
            sb.append(String.format("%d. %s\n", i + 1, save.getSaveName()));
        }

        sb.append("\nType: use ChronoSpell <number>");
        return sb.toString();
    }

    private String handleChronoSpellWithIndex(int index) {
        java.util.List<SavePoint> saves = saveController.getSavesForSlot(currentSaveSlot);
        if (saves.isEmpty() || index < 1 || index > saves.size()) {
            return "Invalid save selection.";
        }

        // Get save (index is 1-based, list is in chronological order)
        SavePoint save = saves.get(index - 1);

        gameState = save.getGameState();
        gameState.getPlayer().fullyRestore();

        // Delete saves newer than the selected one
        saveController.load(currentSaveSlot, save.getTimestamp());

        // Update last room to current room
        lastRoomId = getCurrentRoomId();

        // Clear old output history and add fresh rewind message
        gameState.clearOutput();
        gameState.addOutput("=== TIME REWINDS ===");
        gameState.addOutput("Your consciousness snaps back to the moment you entered " + save.getSaveName() + ".");
        gameState.addOutput("Your energy has been restored.\n");
        gameState.addOutput(gameState.getPlayer().getCurrentRoom().getFullDescription());
        gameState.addOutput("\nType 'help' for a list of commands.");

        return ""; // Return empty since we're adding directly to gameState
    }

    private String handleStoneShove(Player player, Spell spell) {
        Room currentRoom = player.getCurrentRoom();

        if (currentRoom.getName().equals("Supply Vault")) {
            if (!player.hasEnergy(spell.getEnergyCost())) {
                return "You don't have enough energy to cast that spell!";
            }

            player.consumeEnergy(spell.getEnergyCost());
            return "You focus on the rubble... The stones tremble and shift, remembering their original form.\n" +
                    "The archway to the south clears! The path deeper is now open.";
        }

        return "There's nothing here to move with magic.";
    }

    private String handleGlimmerWard(Player player, Spell spell) {
        // Glimmer Ward is now only used for defense in combat
        // No special room interactions
        return null; // Continue to combat handling if needed
    }

    private String handleUse(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            return "Use what?";
        }

        Player player = gameState.getPlayer();

        // Try to find item first
        Item item = player.getItemByName(itemName);
        if (item != null) {
            String result = item.onUse(gameState);
            return result != null ? result : "You use the " + item.getName() + ".";
        }

        // Try to find spell
        Spell spell = player.getSpellByName(itemName);
        if (spell != null) {
            // ChronoSpell special handling
            if (spell instanceof ChronoSpell) {
                return handleChronoSpell();
            }
        }

        // Check if it's ChronoSpell with a number argument
        if (itemName.toLowerCase().startsWith("chronospell ")) {
            try {
                String[] parts = itemName.split(" ", 2);
                if (parts.length == 2) {
                    int saveIndex = Integer.parseInt(parts[1]);
                    return handleChronoSpellWithIndex(saveIndex);
                }
            } catch (NumberFormatException e) {
                return "Invalid save number. Use: use ChronoSpell <number>";
            }
        }

        if (spell != null) {

            // StoneShove special handling
            if (spell instanceof StoneShove) {
                return handleStoneShove(player, spell);
            }

            // GlimmerWard special handling (reveal runes)
            if (spell instanceof GlimmerWard) {
                String result = handleGlimmerWard(player, spell);
                if (result != null)
                    return result;
            }

            // Combat spells
            if (combatHandler.isInCombat()) {
                return combatHandler.castSpellInCombat(player, spell);
            }

            return "You can't use that spell right now.";
        }

        return "You don't have that item or spell.";
    }

    private String handleStatus() {
        Player player = gameState.getPlayer();
        StringBuilder sb = new StringBuilder();
        sb.append("=== STATUS ===\n");
        sb.append("Name: ").append(player.getName()).append("\n");
        sb.append("Health: ").append(player.getHealth()).append("/").append(player.getMaxHealth()).append("\n");
        sb.append("Energy: ").append(player.getEnergy()).append("/").append(player.getMaxEnergy()).append("\n");

        // Show equipped weapon
        String weaponInfo = "None";
        for (Item item : player.getInventory()) {
            if (item instanceof IronShortblade) {
                weaponInfo = item.getName() + " (15-30 damage)";
                break;
            }
        }
        sb.append("Equipped Weapon: ").append(weaponInfo).append("\n");
        sb.append("Location: ").append(player.getCurrentRoom().getName()).append("\n");

        if (combatHandler.isInCombat()) {
            NPC enemy = combatHandler.getCurrentEnemy();
            sb.append("\nIn combat with: ").append(enemy.getName());
            sb.append(" (").append(enemy.getHealth()).append(" HP)\n");
            sb.append("\nTip: Use 'attack' for light attack or 'attack heavy' for heavy attack.\n");
        }

        return sb.toString().trim();
    }

    private String handleHelp() {
        return "=== COMMANDS ===\n" +
                "go <direction>     - Move (north, south, east, west, up, down)\n" +
                "examine [target]   - Look around or inspect an item/NPC\n" +
                "take <item>        - Pick up an item\n" +
                "drop <item>        - Drop an item\n" +
                "inventory          - View your items and spells\n" +
                "talk <npc>         - Talk to someone\n" +
                "attack [heavy]     - Attack in combat (add 'heavy' for strong attack)\n" +
                "use <item/spell>   - Use an item or cast a spell\n" +
                "status             - View your current status\n" +
                "help               - Show this help\n" +
                "quit               - Exit the game";
    }

    private String handleDemoKill() {
        Player player = gameState.getPlayer();

        // Only works in Pit of Chains against Durza
        if (!player.getCurrentRoom().getName().equals("Pit of Chains")) {
            return "Nothing happens.";
        }

        if (!combatHandler.isInCombat()) {
            return "Nothing happens.";
        }

        NPC enemy = combatHandler.getCurrentEnemy();
        if (enemy == null || !enemy.getName().equals("Durza")) {
            return "Nothing happens.";
        }

        // Instantly kill Durza
        enemy.takeDamage(9999);
        combatHandler.endCombat();

        return "[DEMO MODE] You channel an impossible surge of power!\n" +
                "Durza's eyes widen in shock as reality itself bends to your will.\n" +
                "The Shade crumbles to ash, defeated instantly.\n\n" +
                "=== VICTORY ===\n" +
                "You have defeated Durza and freed Saphira!\n" +
                "The dragon's chains shatter, and she soars free.\n\n" +
                "Congratulations! You have completed Shadeslayer!";
    }
}
