package com.shadeslayer.controller;

import java.util.ArrayList;
import java.util.List;

import com.shadeslayer.model.Command;
import com.shadeslayer.model.CommandWords;
import com.shadeslayer.model.ParsedCommand;

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
            arguments.add(tokens[i].toLowerCase());
        }
        return arguments;
    }

    public static void showCommands() {
        System.out.println("Available commands:");
        CommandWords.getAllCommandsWithDescriptions().forEach((cmd, desc) -> 
            System.out.println("  " + cmd + " - " + desc)
        );
    }
}
