package com.shadeslayer.controller;

import java.util.ArrayList;
import java.util.List;

import com.shadeslayer.model.Command;
import com.shadeslayer.model.CommandWords;
import com.shadeslayer.model.Item;
import com.shadeslayer.model.NPC;
import com.shadeslayer.model.ParsedCommand;
import com.shadeslayer.model.Player;
import com.shadeslayer.model.Spell;

public final class Parser {

    // This constructor is private to prevent the class from being instantiated
    private Parser() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static ParsedCommand getParsedCommand(String input) {
        String commandWord = getCommandWord(input);
        List<String> arguments = getArguments(input);

        if (commandWord == null) {
            return new ParsedCommand(null, arguments, "No command entered.");
        }

        Command command = CommandWords.getCommand(commandWord);
        if (command == null) {
            return new ParsedCommand(null, arguments, "Unknown command: '" + commandWord + "'");
        }

        return new ParsedCommand(command, arguments, null);
    }

    public static String getCommandWord(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        String[] tokens = input.trim().split(" ");
        return tokens[0].toLowerCase();
    }

    public static List<String> getArguments(String input) {
        List<String> arguments = new ArrayList<>();
        if (input == null || input.trim().isEmpty()) {
            return arguments;
        }

        String[] tokens = input.trim().split(" ");
        for (int i = 1; i < tokens.length; i++) {
            arguments.add(tokens[i]);
        }
        return arguments;
    }

    public static void showCommands() {
        System.out.println("Available commands:");
        CommandWords.getAllCommandsWithDescriptions()
                .forEach((cmd, desc) -> System.out.println("  " + cmd + " - " + desc));
    }

    public static List<Command> getAvailableCommands(Player player) {
        List<Command> availableCommands = new ArrayList<>();
        for (String commandName : CommandWords.getAllCommandWords()) {
            Command command = CommandWords.getCommand(commandName);
            if (commandIsAvailable(command, player)) {
                availableCommands.add(command);
            }
        }
        return availableCommands;
    }

    public static List<String> getCommandArgumentSuggestions(Command command, Player player) {
        switch (command.getArgumentType()) {
            case DIRECTION:
                if (player.getCurrentRoom() != null) {
                    return player.getCurrentRoom().getExitsList();
                }
                return new ArrayList<>();

            case ITEM:
                if (command.getName().equals("take")) {
                    if (player.getCurrentRoom() != null) {
                        List<String> itemNames = new ArrayList<>();
                        for (Item item : player.getCurrentRoom().getItems()) {
                            itemNames.add(item.getName());
                        }
                        return itemNames;
                    }
                } else {
                    List<String> itemNames = new ArrayList<>();
                    for (Item item : player.getInventory()) {
                        itemNames.add(item.getName());
                    }
                    return itemNames;
                }
                break;

            case ITEM_OR_SPELL:
                List<String> suggestions = new ArrayList<>();
                player.getInventory().forEach(item -> suggestions.add(item.getName()));
                player.getSpells().forEach(spell -> suggestions.add(spell.getName()));
                return suggestions;

            case SPELL:
                List<String> spellNames = new ArrayList<>();
                for (Spell spell : player.getSpells()) {
                    spellNames.add(spell.getName());
                }
                return spellNames;

            case NPC:
                if (player.getCurrentRoom() != null) {
                    List<String> npcNames = new ArrayList<>();
                    for (NPC npc : player.getCurrentRoom().getNPCs()) {
                        npcNames.add(npc.getName());
                    }
                    return npcNames;
                }
                break;

            case TARGET:
                if (player.getCurrentRoom() != null) {
                    List<String> targets = new ArrayList<>();
                    player.getCurrentRoom().getItems().forEach(item -> targets.add(item.getName()));
                    player.getCurrentRoom().getNPCs().forEach(npc -> targets.add(npc.getName()));
                    return targets;
                }
                break;

            case NONE:
            default:
                return new ArrayList<>();
        }

        return new ArrayList<>();
    }

    public static boolean commandIsAvailable(Command command, Player player) {
        switch (command.getArgumentType()) {
            case ITEM:
                if (command.getName().equals("take")) {
                    return player.getCurrentRoom() != null &&
                            !player.getCurrentRoom().getItems().isEmpty();
                } else if (command.getName().equals("drop") || command.getName().equals("use")) {
                    return !player.getInventory().isEmpty();
                }
                return true;

            case SPELL:
                return !player.getSpells().isEmpty();

            case ITEM_OR_SPELL:
                return !player.getInventory().isEmpty() || !player.getSpells().isEmpty();

            case NPC:
                return player.getCurrentRoom() != null &&
                        !player.getCurrentRoom().getNPCs().isEmpty();

            case DIRECTION:
                return player.getCurrentRoom() != null &&
                        !player.getCurrentRoom().getExitsList().isEmpty();

            case TARGET:
                if (player.getCurrentRoom() != null) {
                    return !player.getCurrentRoom().getItems().isEmpty() ||
                            !player.getCurrentRoom().getNPCs().isEmpty();
                }
                return false;

            case NONE:
            default:
                return true;
        }
    }
}
