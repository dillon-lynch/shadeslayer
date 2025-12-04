package com.shadeslayer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandWords {
    private static final Map<String, Command> validCommands = new HashMap<>();
    private static final Trie commandTrie = new Trie();

    static {
        // Movement
        validCommands.put("go", new Command("go", "Move in a direction", ArgumentType.DIRECTION));

        // Items
        validCommands.put("take", new Command("take", "Pick up an item", ArgumentType.ITEM));
        validCommands.put("drop", new Command("drop", "Drop an item", ArgumentType.ITEM));
        validCommands.put("use", new Command("use", "Use an item or spell", ArgumentType.ITEM_OR_SPELL));
        validCommands.put("examine", new Command("examine", "Examine something closely", ArgumentType.TARGET));

        // NPCs
        validCommands.put("talk", new Command("talk", "Talk to an NPC", ArgumentType.NPC));

        // Combat
        validCommands.put("attack", new Command("attack", "Attack an enemy", ArgumentType.TARGET));

        // Information
        validCommands.put("inventory", new Command("inventory", "Show your inventory", ArgumentType.NONE));
        validCommands.put("status", new Command("status", "Show your current status", ArgumentType.NONE));
        validCommands.put("help", new Command("help", "Show available commands", ArgumentType.NONE));

        // Game control
        validCommands.put("quit", new Command("quit", "Exit the game", ArgumentType.NONE));

        // Secret demo command (not added to Trie for autocomplete)
        validCommands.put("demokill",
                new Command("demokill", "Demo: Instantly defeat current enemy", ArgumentType.NONE));

        // Insert all commands into the Trie for autocomplete (except secret commands)
        for (String command : validCommands.keySet()) {
            if (!command.equals("demokill")) { // Don't add secret commands to autocomplete
                commandTrie.insert(command);
            }
        }
    }

    // Private constructor to prevent instantiation
    private CommandWords() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static boolean isValidCommand(String commandWord) {
        return validCommands.containsKey(commandWord.toLowerCase());
    }

    public static Command getCommand(String commandWord) {
        return validCommands.get(commandWord.toLowerCase());
    }

    public static Set<String> getAllCommandWords() {
        return new HashSet<>(validCommands.keySet());
    }

    public static List<String> getSuggestedCommands(String partial) {
        if (partial == null || partial.isEmpty()) {
            return new ArrayList<>(validCommands.keySet());
        }
        return commandTrie.autocomplete(partial.toLowerCase());
    }

    public static Map<String, String> getAllCommandsWithDescriptions() {
        Map<String, String> descriptions = new HashMap<>();
        for (Map.Entry<String, Command> entry : validCommands.entrySet()) {
            descriptions.put(entry.getKey(), entry.getValue().getDescription());
        }
        return descriptions;
    }
}
