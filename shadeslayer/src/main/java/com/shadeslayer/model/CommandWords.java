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
        // Basic commands
        validCommands.put("go", new Command("go", "Move in a direction", ArgumentType.DIRECTION));
        validCommands.put("look", new Command("look", "Look around or examine something", ArgumentType.NONE));
        validCommands.put("help", new Command("help", "Show available commands", ArgumentType.NONE));
        validCommands.put("quit", new Command("quit", "Exit the game", ArgumentType.NONE));
        validCommands.put("inventory", new Command("inventory", "Show your inventory", ArgumentType.NONE));

        // Action commands
        validCommands.put("take", new Command("take", "Pick up an item", ArgumentType.ITEM));
        validCommands.put("drop", new Command("drop", "Drop an item", ArgumentType.ITEM));
        validCommands.put("use", new Command("use", "Use an item or spell", ArgumentType.ITEM_OR_SPELL));
        validCommands.put("examine", new Command("examine", "Examine something closely", ArgumentType.TARGET));

        // Interaction commands
        validCommands.put("talk", new Command("talk", "Talk to an NPC", ArgumentType.NPC));
        validCommands.put("attack", new Command("attack", "Attack an enemy", ArgumentType.TARGET));

        // Insert all commands into the Trie for autocomplete
        for (String command : validCommands.keySet()) {
            commandTrie.insert(command);
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
